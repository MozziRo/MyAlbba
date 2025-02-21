<template>
  <div class="video-interview-container">
    <!-- 방 생성 모달 (면접관일 때만 표시) -->
    <CreateRoomModal
      v-if="isInterviewer && !isRoomCreated"
      :interview-url="interviewUrl"
      @start="showMediaSettings"
    />

    <!-- 면접 시작 모달 -->
    <MediaSettingsModal
      v-if="((isRoomCreated) || !isInterviewer) && !isEnteredRoom"
      @start="startInterview"
    />

    <!-- 면접 화면 -->
    <div class="interview-content">
      <!-- 면접 영상 -->

      <div class="video-grid" :class="{ 'with-memo': isMemoOpen }">
        <!-- 메모장 컴포넌트 추가 -->
        <MemoPad
          v-if="isMemoOpen"
          class="memo-grid-item"
          @close="isMemoOpen = false"
        />

        <!-- 나 -->
        <VideoParticipant
          v-if="localVideoStore.localStream"
          :stream="localVideoStore.localStream"
          :label="nickname"
          :is-local="true"
          :is-enlarged="'local' === enlargedParticipantId"
          @toggle-enlarge="toggleEnlargeParticipant('local')"
        />

        <!-- 상대 -->
        <VideoParticipant
          v-for="participant in participants"
          :key="participant.id"
          :stream="participant.stream"
          :label="participant.name"
          :is-local="participant.isLocal"
          :is-enlarged="participant.id === enlargedParticipantId"
          @toggle-enlarge="toggleEnlargeParticipant(participant.id)"
        />
      </div>

      <!-- 면접 제어 버튼 -->
      <div class="controls-wrapper">
        <div class="controls">
          <button @click="toggleVideo" :class="{ active: localVideoStore.isVideoOn }">
            {{ localVideoStore.isVideoOn ? '🎥' : '📵' }}
          </button>
          <button @click="toggleAudio" :class="{ active: localVideoStore.isAudioOn }">
            {{ localVideoStore.isAudioOn ? '🎤' : '🔇' }}
          </button>
          <button @click="toggleMemo" :class="{ active: isMemoOpen }">
            💬
          </button>
          <button @click="toggleEmoticonPicker" :class="{ active: showEmoticonPicker }">
            😊
          </button>
          <button @click="endCall" class="end-call">
            📞
          </button>
          <!-- <button @click="addParticipant">
            +
          </button>
          <button @click="removeParticipant">
            -
          </button> -->
        </div>
      </div>

      <!-- 참가자 목록 -->
      <ParticipantsList :participants="participants" />

      <!-- 채팅창 -->
      <ChatWindow
        ref="chatWindowRef"
        :nickname="nickname"
      />
      <!-- 이모티콘 피커 -->
      <div v-show="showEmoticonPicker" class="emoticon-picker-wrapper">
        <EmoticonPicker @select="sendEmoticon" />
      </div>

      <!-- 떠다니는 이모티콘들 -->
      <FloatingEmoticon
        v-for="emote in floatingEmoticons"
        :key="emote.id"
        :emoticon="emote.emoticon"
        :start-position="emote.position"
        @remove="removeEmoticon(emote.id)"
      />
    </div>

    <!-- 메모장 컴포넌트 추가 -->
    <MemoPad
      v-show="isMemoOpen"
      @close="isMemoOpen = false"
      :auto-save="autoSave"
    />

    <!-- 메모 툴팁: 메모가 열렸을 때 보여줌 -->
    <div v-if="memoTooltipMessage" class="memo-tooltip">
      <div class="tooltip-content">
        {{ memoTooltipMessage }}
      </div>
    </div>

    <!-- 로딩 모달 -->
    <SuccessFailureModal
      :isVisible="isLoading"
      :isSuccess="true"
      message="면접이 종료되었습니다..."
      @close="() => {}"
      :isLoading="true"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from 'vue'
import { useLocalVideoStore } from '@/store/video/localVideoStore.js'
import { useWebRTCStore } from '@/store/video/webRTCStore.js'
import MediaSettingsModal from '@/components/videoInterview/MediaSettingsModal.vue'
import ChatWindow from '@/components/videoInterview/ChatWindow.vue'
import EmoticonPicker from '@/components/videoInterview/EmoticonPicker.vue'
import FloatingEmoticon from '@/components/videoInterview/FloatingEmoticon.vue'
import VideoParticipant from '@/components/videoInterview/VideoParticipant.vue'
import ParticipantsList from '@/components/videoInterview/ParticipantsList.vue'
import CreateRoomModal from '@/components/videoInterview/CreateRoomModal.vue'
import { useRoute, useRouter } from 'vue-router'
import { webSocketInstance } from '@/common/apis/ws/webSocket'
import MemoPad from '@/components/videoInterview/MemoPad.vue'
import { checkInterviewRoom } from '@/common/apis/api/webrtc'
import SuccessFailureModal from '@/components/common/SuccessFailureModal.vue'

// 멤버 관련
const route = useRoute()
const router = useRouter()
const roomId = ref(null)
const interviewUrl = ref(null)

// 비디오 관련
const localVideoStore = useLocalVideoStore()
const webRTCStore = useWebRTCStore()
const isInterviewStarted = ref(false)
const isRoomCreated = ref(false)
const isEnteredRoom = ref(false)
const isMemoOpen = ref(false)
const showEmoticonPicker = ref(false)
const nickname = ref('')
const otherNickname = ref('') // 임시

// 채팅
const chatWindowRef = ref(null)

const memoTooltipMessage = ref("")
const memoTooltipShown = ref(false)

onMounted(async () => {
  // URL에서 roomId 추출
  const pathSegments = route.path.split('/')
  if (pathSegments[2]) {  // /video-interview/:roomId 형식일 경우
    roomId.value = pathSegments[2]
    interviewUrl.value = `/video-interview/${roomId.value}`

    // 방이 존재하는지 확인
    try {
      const response = await checkInterviewRoom(roomId.value)
      if (!response) {
        alert('해당 방이 존재하지 않습니다.')
        router.push('/')  // 존재하지 않는 경우 홈 화면으로 이동
      }
    } catch (error) {
      console.error('방 존재 확인 실패:', error)
      router.push('/')
    }
  }
})

// 멤버 관련
const isInterviewer = computed(() => {
  const isInterviewPath = route.path.split('/')[2] ? false : true;
  return isInterviewPath
})

const autoSave = ref(false)

const enlargedParticipantId = ref(null)
const participants = ref([])

const showMediaSettings = (roomData) => {
  roomId.value = roomData.roomId
  interviewUrl.value = roomData.url
  isRoomCreated.value = true
}

// JOIN
const handleJoinMessage = async (message) => {
  try {
    const sender = message.sender
    const remoteNickname = message.nickname
    otherNickname.value = remoteNickname // 임시

    const systemMessage = `${remoteNickname}님이 면접방에 참가했습니다.`
    chatWindowRef.value.addSystemMessage(systemMessage)

    await webRTCStore.createAndSendOffer()
  } catch (error) {
    console.error('WebRTC 연결 실패:', error)
  }
}
// LEAVE
const handleLeaveMessage = (message) => {
  const remoteNickname = message.nickname
  const systemMessage = `${remoteNickname}님이 면접방을 나갔습니다.`

  if (chatWindowRef.value) {
    chatWindowRef.value.addSystemMessage(systemMessage)
  }
}

// EMOTICON
const handleEmoticonMessage = (message) => {
  displayEmoticon(message.emoticon)
}

watch(() => webRTCStore.isConnected, (isConnected) => {
  if (isConnected) {
    isInterviewStarted.value = true
  } else {
    participants.value.pop() // 임시로 제거 1대1이므로
  }
})

const startInterview = async (name) => {
  isEnteredRoom.value = true
  nickname.value = name

  try {
    await webSocketInstance.connect()
    webSocketInstance.on('join', handleJoinMessage)
    webSocketInstance.on('leave-interview', handleLeaveMessage)
    webSocketInstance.on('emoticon', handleEmoticonMessage)
    webRTCStore.initializeSignaling()

    webRTCStore.createPeerConnection()
    await webRTCStore.initializeWebRTC()

    await webSocketInstance.send('join', {
      roomId: roomId.value,
      nickname: nickname.value
    })
  } catch (error) {
    console.error('면접 시작 준비 중 에러 발생:', error)
  }
}

const toggleVideo = () => {
  localVideoStore.toggleVideo()
}

const toggleAudio = () => {
  localVideoStore.toggleAudio()
}

const toggleMemo = () => {
  isMemoOpen.value = !isMemoOpen.value
  if (isMemoOpen.value && !memoTooltipShown.value) {
    memoTooltipMessage.value = "메모장을 더블클릭하면 고정/투명 전환됩니다."
    memoTooltipShown.value = true
    setTimeout(() => {
      memoTooltipMessage.value = ""
    }, 3000)
  }
}

const toggleEmoticonPicker = () => {
  showEmoticonPicker.value = !showEmoticonPicker.value
}

const sendLeaveMessage = async () => {
  await webSocketInstance.send('leave-interview', {
    nickname: nickname.value
  })
}

const isLoading = ref(false)

const endCall = async () => {
  try {
    autoSave.value = true // 메모 자동 저장

    isLoading.value = true // 로딩 시작

    await sendLeaveMessage()

    webSocketInstance.disconnect()
    await localVideoStore.stopLocalStream()
    await webRTCStore.closePeerConnection()
    isInterviewStarted.value = false
    isEnteredRoom.value = false

    setTimeout(() => {
      gotoResultPage()
    }, 3000)

  } catch (error) {
    console.error('면접 종료 실패:', error)
  }
}

const gotoResultPage = () => {
  router.push({
    name: 'interview-result',
    params: { id: roomId.value }
  })
}

const floatingEmoticons = ref([])
let emoticonId = 0

const displayEmoticon = (emoticon) => {
  const position = {
    x: window.innerWidth / 2,
    y: window.innerHeight - 100
  }
  floatingEmoticons.value.push({
    id: emoticonId++,
    emoticon,
    position
  })
}

const removeEmoticon = (id) => {
  floatingEmoticons.value = floatingEmoticons.value.filter(
    emote => emote.id !== id
  )
}

const sendEmoticon = async (emoticon) => {
  displayEmoticon(emoticon)
  await webSocketInstance.send('emoticon', {
    emoticon: emoticon
  })
}

const toggleEnlargeParticipant = (id) => {
  enlargedParticipantId.value = enlargedParticipantId.value === id ? null : id
}

watch(() => webRTCStore.remoteStream, (newStream) => {
  if (newStream) {
    participants.value.push({
      id: newStream.id,
      name: otherNickname.value == "" ? "면접관" : otherNickname.value,
      stream: newStream,
      isLocal: false
    })
  }
})

onUnmounted(async () => {
  webSocketInstance.off('join')
  webSocketInstance.off('leave-interview')
  webSocketInstance.off('emoticon')

  webSocketInstance.disconnect()
  await localVideoStore.stopLocalStream()
  await webRTCStore.closePeerConnection()
})
</script>

<style scoped>
.video-interview-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  color: white;
}

.interview-content {
  padding: 2rem;
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
  flex: 1;
  align-items: start;
  justify-content: center;
}

.video-grid.with-memo {
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
}

.video-wrapper {
  position: relative;
  aspect-ratio: 16/9;
  background: #000;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 16px rgba(0,0,0,0.3);
}

.video-wrapper video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-label {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  background: rgba(0,0,0,0.6);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
}

.controls-wrapper {
  position: fixed;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  z-index: 100;
}

.controls {
  display: flex;
  gap: 1rem;
  background: var(--primary);
  padding: 1rem;
  border-radius: 50px;
  backdrop-filter: blur(10px);
}

.controls button {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: none;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  transition: all 0.3s;
}

.controls button:hover {
  background: rgba(255,255,255,0.3);
  transform: scale(1.1);
}

.controls button.active {
  background: var(--primary-dark);
}

/* .controls button.end-call {
  /* background: #ff4444; */
/* } */

.emoticon-picker-wrapper {
  position: fixed;
  bottom: 100px;
  right: 20px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

@media (max-width: 768px) {
  .video-grid {
    grid-template-columns: 1fr;
    max-width: 500px;
    margin: 0 auto;
  }

  .controls {
    padding: 0.5rem;
    gap: 0.5rem;
  }

  .controls button {
    width: 40px;
    height: 40px;
    font-size: 1.2rem;
  }
}

.url-share-modal {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  background: white;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  z-index: 1000;
}

.url-container {
  display: flex;
  gap: 10px;
  margin: 15px 0;
}

.url-container input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.url-container button {
  padding: 8px 16px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.help-text {
  color: #666;
  font-size: 14px;
  margin: 0;
}

.waiting-room {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a1a1a, #2d2d2d);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.waiting-content {
  text-align: center;
  width: 90%;
  max-width: 800px;
}

.waiting-content h2 {
  margin-bottom: 1rem;
  font-size: 1.8rem;
}

.waiting-content p {
  color: #999;
  margin-bottom: 2rem;
}

.local-preview {
  width: 100%;
  max-width: 600px;
  margin: 0 auto 2rem;
}

.cancel-button {
  padding: 0.8rem 2rem;
  background: #dc3545;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s;
}

.cancel-button:hover {
  background: #c82333;
}

.memo-grid-item {
  position: relative !important;
  top: 0 !important;
  left: 0 !important;
  transform: none !important;
}

.memo-tooltip {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.85);
  color: #fff;
  padding: 15px 20px;
  border-radius: 8px;
  z-index: 1001;
  text-align: center;
  max-width: 80%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.tooltip-content {
  font-size: 1.2rem;
  font-weight: 500;
  line-height: 1.4;
}
</style>
