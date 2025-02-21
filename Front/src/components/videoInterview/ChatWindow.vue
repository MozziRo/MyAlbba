<template>
  <div class="chat-window" :class="{ 'chat-window-open': isOpen }">
    <div class="chat-toggle" @click="toggleChat">
      <span :class="{ 'flash-icon': hasNewMessage }">💬</span>
      <span v-if="unreadCount > 0" class="notification-badge">{{ unreadCount }}</span>
    </div>
    <div class="chat-content">
      <div class="chat-header">
        <h3>채팅</h3>
      </div>
      <div class="chat-messages" ref="chatMessagesRef">
        <div
          v-for="(msg, index) in messages"
          :key="index"
          class="message"
          :class="{
            'message-system': msg.user === 'System',
            'message-mine': msg.user === '나',
            'message-other': msg.user !== '나' && msg.user !== 'System'
          }">
          <template v-if="msg.user !== 'System'">
            <span class="user" :class="{ 'user-mine': msg.user === '나', 'user-other': msg.user !== '나' && msg.user !== 'System' }">{{ msg.user }}:</span>
          </template>
          <span class="text" :class="{ 'text-mine': msg.user === '나', 'text-other': msg.user !== '나' && msg.user !== 'System' }">{{ msg.text }}</span>
        </div>
      </div>
      <div class="chat-input">
        <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="메시지를 입력하세요..."/>
        <button @click="sendMessage" style="width: 25%;">📨</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { webSocketInstance } from '@/common/apis/ws/webSocket'

const isOpen = ref(false)
const messages = ref([])
const newMessage = ref('')
const chatMessagesRef = ref(null)
const hasNewMessage = ref(false)
const unreadCount = ref(0)

const props = defineProps({
  nickname: {
    type: String,
    required: true
  }
})

onMounted(() => {
  webSocketInstance.on('chat', async (message) => {
    messages.value.push({ user: message.nickname, text: message.text })
    await scrollToBottom()
    if (!isOpen.value) {
      hasNewMessage.value = true
      unreadCount.value++
    }
  })
})

onUnmounted(() => {
  webSocketInstance.off('chat')
})

const toggleChat = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    hasNewMessage.value = false
    unreadCount.value = 0
  }
}

const scrollToBottom = async () => {
  await nextTick()
  if (chatMessagesRef.value) {
    chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
  }
}

const sendMessage = async () => {
  if (newMessage.value.trim() !== '') {
    messages.value.push({ user: '나', text: newMessage.value })

    // 메시지 전송
    webSocketInstance.send('chat', { nickname: props.nickname, text: newMessage.value })

    newMessage.value = ''
    await scrollToBottom()
  }
}

const addSystemMessage = async (message) => {
  messages.value.push({ user: 'System', text: message })
  await scrollToBottom()
}

defineExpose({
  addSystemMessage
})

</script>

<style scoped>
/* 채팅창 전체: 블루가 섞인 투명하고 미세한 그라데이션 효과 */
.chat-window {
  position: fixed;
  top: 70px;
  right: -320px;
  width: 320px;
  height: calc(100vh - 70px);
  background: linear-gradient(135deg, rgba(245, 247, 250, 0.5), rgba(195, 207, 226, 0.5));
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.3);
  transition: right 0.3s ease;
  display: flex;
  z-index: 100;
}

.chat-window-open {
  right: 0;
}

/* 채팅 토글 버튼 */
.chat-toggle {
  position: absolute;
  left: -40px;
  top: 35%;
  width: 40px;
  height: 40px;
  background-color: #424769;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 8px 0 0 8px;
}

/* 채팅 콘텐츠 영역 */
.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  color: #fff;
}

/* 헤더 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  color: var(--primary);
  padding: 0.5rem 1rem;
  background-color: rgba(255, 255, 255, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 8px 8px 0 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 채팅 메시지 영역: 미세한 투명도와 인세트 그림자로 경계 강화 */
.chat-messages {
  flex: 1;
  padding: 0.5rem;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px 8px 0 0;
  box-shadow: inset 0 -4px 8px rgba(0,0,0,0.15);
  margin-bottom: -1px;
}

/* 3D 효과의 무채색 투명 채팅 메시지 */
.message {
  margin-bottom: 0.8rem;
  padding: 0.75rem 1.25rem;
  border-radius: 22px;
  border: 1px solid rgba(255,255,255,0.2);
  max-width: 80%;
  transition: transform 0.2s ease, box-shadow 0.3s ease, background 0.3s ease;
  position: relative;
}

.message-mine:hover, .message-other:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.35);
}

/* 시스템 메시지: 진한 무채색 톤으로 구분 */
.message-system {
  border: none; /* 테두리 제거 */
  color: #f1f1f1; /* 텍스트 색상 */
  margin: 0.4rem auto; /* 여백 */
  text-align: center;
  font-size: 0.9rem;
  font-style: italic;
  padding: 0; /* padding 제거 */
}


/* 내 메시지: 오른쪽 정렬, 약간의 색상 변주로 3D 효과 강조 */
.message-mine {
  background: linear-gradient(135deg, rgba(240,240,240,0.25), rgba(220,220,220,0.15));
  margin-left: auto;
  border-bottom-right-radius: 10px;
  border: 1px solid rgba(240,240,240,0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

/* 상대 메시지: 왼쪽 정렬, 동일한 3D 무채색 투명 효과 */
.message-other {
  background: var(--primary);
  margin-right: auto;
  border-bottom-left-radius: 10px;
  border: 1px solid rgba(245,245,245,0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
}

/* 유저 이름 및 메시지 텍스트 */
.user {
  font-weight: bold;
  margin-right: 0.5rem;
}

.user-mine {
  color: #424769;
}

.user-other {
  color: #edeef0;
}

.text {
  color: #333;
  word-break: break-word;
}

.text-mine {
  color: #424769;
}

.text-other {
  color: #edeef0;
}

/* 채팅 입력 영역: 명확한 경계와 글라스 느낌 */
.chat-input {
  display: flex;
  padding: 0.75rem;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-top: none;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.25);
  backdrop-filter: blur(8px);
}

.chat-input input {
  flex: 1;
  padding: 0.5rem 0.75rem;
  border: none;
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.2);
  color: #333;
  font-size: 1rem;
  outline: none;
}

.chat-input input::placeholder {
  color: rgba(51, 51, 51, 0.7);
}

.chat-input button {
  background-color: rgba(255, 255, 255, 0.25);
  border: none;
  margin-left: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  color: #333;
  font-size: 1.2rem;
  transition: background-color 0.3s ease;
}

.chat-input button:hover {
  background-color: rgba(255, 255, 255, 0.35);
}

/* 알림 배지 애니메이션 */
@keyframes flash {
  0% { opacity: 1; }
  50% { opacity: 0.3; }
  100% { opacity: 1; }
}

.flash-icon {
  animation: flash 1s infinite;
}

/* 알림 배지 스타일 */
.notification-badge {
  position: absolute;
  top: -8px;
  right: 24px;
  background-color: #FF4444;
  color: white;
  border-radius: 50%;
  min-width: 20px;
  height: 20px;
  padding: 0 6px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  border: 2px solid #2D3250;
}
</style>
