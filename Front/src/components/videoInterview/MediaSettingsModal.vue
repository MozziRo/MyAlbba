<template>
  <div class="modal-overlay">
    <div class="modal-content">
      <div class="modal-header">
        <button class="back-button" @click="goBack">
          ← 뒤로가기
        </button>
        <h2>면접 설정</h2>
      </div>

      <div class="preview-section">
        <video ref="previewVideo" autoplay class="preview-video"></video>
      </div>

      <div class="settings-section">
        <div class="input-group">
          <label>닉네임</label>
          <input
            v-model="nickname"
            type="text"
            placeholder="닉네임을 입력하세요"
            class="nickname-input"
          >
        </div>

        <div class="device-controls">
        <!-- 비디오 토글 -->
          <button
            @click="toggleVideo"
            :class="{ active: isVideoEnabled }"
            class="device-button"
          >
            {{ isVideoEnabled ? '🎥' : '❌' }}
            카메라 {{ isVideoEnabled ? 'ON' : 'OFF' }}
          </button>

          <!-- 오디오 토글 -->
          <button
            @click="toggleAudio"
            :class="{ active: isAudioEnabled }"
            class="device-button"
          >
            {{ isAudioEnabled ? '🎤' : '🚫' }}
            마이크 {{ isAudioEnabled ? 'ON' : 'OFF' }}
          </button>
        </div>
      </div>

      <!-- 면접 시작하기 버튼 -->
      <button
        @click="startInterview"
        class="start-button"
        :disabled="!nickname || (!isVideoEnabled && !isAudioEnabled)"
      >
        면접 시작하기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useLocalVideoStore } from '@/store/video/localVideoStore'
import { useRouter } from 'vue-router'
import { getCurrentUser } from '@/common/apis/utils/tokenUtils'

const emit = defineEmits(['start'])
const localVideoStore = useLocalVideoStore()
const router = useRouter()

const previewVideo = ref(null)
const nickname = ref('')
const isVideoEnabled = ref(false)
const isAudioEnabled = ref(false)

// 이 모달이 마운트 되었을 때
onMounted(async () => {
    // 닉네임을 현재의 이름으로 세팅
    const user = getCurrentUser()
    nickname.value = user === null ? 'guest' : user.name

    // 비디오 및 오디오 활성화 여부 업데이트
    isVideoEnabled.value = localVideoStore.isVideoOn
    isAudioEnabled.value = localVideoStore.isAudioOn

    const isStreamStarted = await localVideoStore.startLocalStream()

    if (isStreamStarted) {
        previewVideo.value.srcObject = localVideoStore.localStream
    }
})


const toggleVideo = () => {
    isVideoEnabled.value = localVideoStore.toggleVideo()
}

const toggleAudio = () => {
    isAudioEnabled.value = localVideoStore.toggleAudio()
}

const startInterview = () => {
  emit('start', nickname.value)
}

const goBack = async () => {
  await localVideoStore.stopLocalStream() // 스트림 정지 후 이동
  router.back() // 라우터 히스토리를 이용한 뒤로가기
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.preview-section {
  margin: 1.5rem 0;
  border-radius: 12px;
  overflow: hidden;
  background: #2D3250;  /* 진한 네이비 블루 */
  aspect-ratio: 16/9;
}

.preview-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.settings-section {
  margin: 1.5rem 0;
}

.input-group {
  margin-bottom: 1.5rem;
}

.nickname-input {
  width: 100%;
  padding: 0.8rem;
  border: 2px solid #424769;  /* 중간 톤 네이비 블루 */
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.nickname-input:focus {
  border-color: #F9B17A;  /* 따뜻한 오렌지 */
  outline: none;
}

.device-controls {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.device-button {
  flex: 1;
  padding: 1rem;
  border: none;
  border-radius: 8px;
  background: #424769;  /* 중간 톤 네이비 블루 */
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: white;
}

.device-button.active {
  background: #F9B17A;  /* 따뜻한 오렌지 */
  color: #2D3250;  /* 진한 네이비 블루 */
}

.start-button {
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 8px;
  background: #F9B17A;  /* 따뜻한 오렌지 */
  color: #2D3250;  /* 진한 네이비 블루 */
  font-size: 1.1rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.start-button:hover:not(:disabled) {
  background: #FCDDBD;  /* 밝은 오렌지 */
}

.start-button:disabled {
  background: #E0E0E0;  /* 중립 색상 */
  cursor: not-allowed;
}

.modal-header {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.back-button {
  background: none;
  border: none;
  color: #424769;  /* 중간 톤 네이비 블루 */
  padding: 0.5rem;
  cursor: pointer;
  font-size: 1rem;
  transition: color 0.3s;
  text-align: left;
}

.back-button:hover {
  color: #2D3250;  /* 진한 네이비 블루 */
}

.modal-header h2 {
  margin: 0 auto;
  color: #2D3250;  /* 진한 네이비 블루 */
}
</style>
