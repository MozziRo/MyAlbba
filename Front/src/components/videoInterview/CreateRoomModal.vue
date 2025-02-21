<template>
  <div class="create-room-modal">
    <div class="modal-content">
      <h3>면접방 생성</h3>
      <div v-if="!isCreating">
        <button class="create-button" @click="createRoom">면접방 생성하기</button>
      </div>
      <div v-else-if="interviewUrl" class="url-container">
        <input type="text" :value="interviewUrl" readonly ref="urlInput" />
        <button class="copy-button" @click="copyUrl($event)">복사</button>
        <p class="help-text">이 URL을 지원자와 공유하세요</p>
        <button class="start-button" @click="$emit('start', roomData)">
          면접 참가하기
        </button>
      </div>
      <div v-else class="loading">
        <p>면접방을 생성중입니다...</p>
      </div>
    </div>

    <!-- copy 알림 팝업 -->
    <NotificationPopup
      :message="message"
      v-model:show="showNotification"
      :position="notificationPosition"
      :duration="500"
    />


  </div>


</template>

<script setup>
import { ref, nextTick } from 'vue'
import { createInterviewRoom } from '@/common/apis/api/webrtc'
import NotificationPopup from '@/components/common/NotificationPopup.vue'
const props = defineProps({
  interviewUrl: String
})

const emit = defineEmits(['start'])
const urlInput = ref(null)
const isCreating = ref(false)
const interviewUrl = ref(null)
const roomData = ref(null)

const showNotification = ref(false)
const notificationPosition = ref({ x: 0, y: 0 })
const message = ref('')

const createRoom = async () => {
  try {
    isCreating.value = true
    const { roomId, url } = await createInterviewRoom()
    interviewUrl.value = url
    roomData.value = { roomId, url }
  } catch (error) {
    console.error('면접방 생성 실패:', error)
  }
}

const copyUrl = async (event) => {
  try {
    await navigator.clipboard.writeText(interviewUrl.value)
    message.value = 'URL이 클립보드에 복사되었습니다.'
    notificationPosition.value = {
      x: event.clientX,
      y: event.clientY
    }
    showNotification.value = false
    nextTick(() => {
      showNotification.value = true
    })
  } catch (err) {
    urlInput.value.select()
    message.value = 'URL이 클립보드에 복사되었습니다.'
    notificationPosition.value = {
      x: event.clientX,
      y: event.clientY
    }
  }
}
</script>

<style scoped>
.create-room-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: #2c2c2c;
  padding: 2rem;
  border-radius: 16px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
  color: white;
}

h3 {
  margin: 0 0 1.5rem 0;
  color: #fff;
  text-align: center;
  font-size: 1.5rem;
}

.url-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin: 1.5rem 0;
}

.url-container input {
  padding: 0.8rem;
  border: 1px solid #424769;  /* 중간 톤 네이비 블루 */
  border-radius: 8px;
  background: #2D3250;  /* 진한 네이비 블루 */
  color: #fff;
  font-size: 1rem;
}

.url-container input:focus {
  outline: none;
  border-color: #676F9D;  /* 밝은 네이비 블루 */
}

button {
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #424769;  /* 중간 톤 네이비 블루 */
  color: white;
}

button:hover {
  background: #676F9D;  /* 밝은 네이비 블루 */
}

.create-button {
  width: 100%;
  background: #424769;  /* 중간 톤 네이비 블루 */
  font-size: 1.1rem;
}

.create-button:hover {
  background: #676F9D;  /* 밝은 네이비 블루 */
}

.copy-button {
  background: #424769;  /* 중간 톤 네이비 블루 */
}

.copy-button:hover {
  background: #676F9D;  /* 밝은 네이비 블루 */
}

.start-button {
  margin-top: 1rem;
  background: #F9B17A;  /* 따뜻한 오렌지 */
  font-size: 1.1rem;
}

.start-button:hover {
  background: #FCDDBD;  /* 밝은 오렌지 */
}

.help-text {
  color: #E0E0E0;  /* 중립 색상 */
  font-size: 0.9rem;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 1rem;
  color: #9E9E9E;  /* 어두운 중립 색상 */
}
</style>
