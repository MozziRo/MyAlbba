<template>
  <Transition name="modal">
    <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
      <div class="modal-container" :class="{ success: isSuccess, failure: !isSuccess }">
        <div class="modal-header">
          <div class="modal-icon">
            <div class="icon-circle">
              <i v-if="isSuccess" class="fas fa-check"></i>
              <i v-else class="fas fa-exclamation"></i>
            </div>
          </div>
          <h2>{{ modalTitle }}</h2>
        </div>
        <div class="modal-body">
          <p style="text-align: center;">{{ message }}</p>
        </div>
        <div class="modal-footer">
          <div>
            <button v-if="!isLoading" class="confirm-btn" @click="closeModal">
              확인
            </button>
            <div v-else class="spinner"></div>
          </div>
        </div>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { computed } from 'vue'

// props 정의
const props = defineProps({
  isVisible: {
    type: Boolean,
    required: true
  },
  isSuccess: {
    type: Boolean,
    required: true
  },
  message: {
    type: String,
    required: true
  },
  isLoading: {
    type: Boolean,
    default: false
  }
})

// emit 정의
const emit = defineEmits(['close'])

const modalTitle = computed(() => (props.isSuccess ? '성공' : '실패'))

// 모달 닫기
const closeModal = () => {
  emit('close')
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background: #fff;
  width: 90%;
  max-width: 400px;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.2);
}

.modal-header {
  padding: 2rem 2rem 1rem;
  text-align: center;
}

.modal-icon {
  margin-bottom: 1rem;
}

.icon-circle {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  font-size: 2rem;
  color: white;
  transition: transform 0.3s ease;
}

.success .icon-circle {
  background: #424769;
  box-shadow: 0 4px 12px rgba(66, 71, 105, 0.3);
}

.failure .icon-circle {
  background: #F9B17A;
  box-shadow: 0 4px 12px rgba(249, 177, 122, 0.3);
}

.modal-container h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #2D3436;
}

.modal-body {
  padding: 1rem 2rem;
}

.modal-body p {
  margin: 0;
  color: #636E72;
  line-height: 1.6;
  font-size: 1.1rem;
}

.modal-footer {
  padding: 1rem 2rem 2rem;
  text-align: center;
}

.confirm-btn {
  padding: 0.8rem 2.5rem;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
}

.success .confirm-btn {
  background: #424769;
}

.success .confirm-btn:hover {
  background: #2D3250;
}

.failure .confirm-btn {
  background: #F9B17A;
}

.failure .confirm-btn:hover {
  background: #FCDDBD;
}

/* 트랜지션 애니메이션 */
.modal-enter-active,
.modal-leave-active {
  transition: all 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
  transform: scale(0.9);
}

.modal-enter-to,
.modal-leave-from {
  opacity: 1;
  transform: scale(1);
}

.spinner {
  width: 50px;
  height: 50px;
  border: 5px solid #f3f3f3;
  border-top: 5px solid #424769;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
