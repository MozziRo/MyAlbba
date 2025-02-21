# components/SubstituteRequestModal.vue
<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>대타 신청</h3>
        <button class="close-button" @click="closeModal">×</button>
      </div>

      <div class="modal-body">
        <!-- 날짜 선택 -->
        <div class="form-section">
          <h4>날짜 선택</h4>
          <input
            type="date"
            v-model="selectedDate"
            :min="minDate"
            class="date-input"
          >
        </div>

        <!-- 시간 선택 -->
        <div class="form-section">
          <h4>시간 선택</h4>
          <div class="time-selector">
            <div class="time-input">
              <label>시작 시간</label>
              <select v-model="startTime">
                <option v-for="time in availableTimes" :key="time" :value="time">
                  {{ formatTime(time) }}
                </option>
              </select>
            </div>
            <div class="time-input">
              <label>종료 시간</label>
              <select v-model="endTime">
                <option v-for="time in availableTimes" :key="time" :value="time">
                  {{ formatTime(time) }}
                </option>
              </select>
            </div>
          </div>
        </div>

        <!-- 사유 입력 -->
        <div class="form-section">
          <h4>신청 사유</h4>
          <textarea
            v-model="reason"
            placeholder="대타 신청 사유를 입력해주세요."
            rows="3"
            required
          ></textarea>
        </div>

        <!-- 긴급도 선택 -->
        <div class="form-section">
          <h4>긴급도</h4>
          <div class="urgency-selector">
            <label class="urgency-option">
              <input type="radio" v-model="urgency" value="normal">
              <span>일반</span>
            </label>
            <label class="urgency-option">
              <input type="radio" v-model="urgency" value="urgent">
              <span>긴급</span>
            </label>
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <button class="cancel-button" @click="closeModal">취소</button>
        <button class="submit-button" @click="submitRequest" :disabled="!isValid">신청</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const emit = defineEmits(['close', 'submit'])

// 상태 관리
const selectedDate = ref('')
const startTime = ref('09:00')
const endTime = ref('18:00')
const reason = ref('')
const urgency = ref('normal')

// 최소 선택 가능 날짜 (오늘)
const minDate = computed(() => {
  const today = new Date()
  return today.toISOString().split('T')[0]
})

// 선택 가능한 시간 목록
const availableTimes = Array.from({ length: 24 }, (_, i) => {
  const hour = String(i).padStart(2, '0')
  return `${hour}:00`
})

// 시간 포맷팅
const formatTime = (time) => {
  return time
}

// 폼 유효성 검사
const isValid = computed(() => {
  return selectedDate.value && startTime.value && endTime.value && reason.value.trim()
})

// 모달 닫기
const closeModal = () => {
  emit('close')
}

// 대타 신청 제출
const submitRequest = () => {
  const requestData = {
    date: selectedDate.value,
    startTime: startTime.value,
    endTime: endTime.value,
    reason: reason.value,
    urgency: urgency.value,
  }
  emit('submit', requestData)
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 1.25rem;
  color: #333;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 1.5rem;
}

.form-section {
  margin-bottom: 1.5rem;
}

.form-section h4 {
  margin-bottom: 0.75rem;
  color: #333;
  font-size: 1rem;
}

.date-input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.time-selector {
  display: flex;
  gap: 1rem;
}

.time-input {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.time-input label {
  color: #666;
  font-size: 0.9rem;
}

.time-input select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  background: white;
}

textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  min-height: 80px;
}

.urgency-selector {
  display: flex;
  gap: 2rem;
}

.urgency-option {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.modal-footer {
  padding: 1.5rem;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.cancel-button,
.submit-button {
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.cancel-button {
  background: #f0f0f0;
  border: none;
  color: #666;
}

.submit-button {
  background: #4285f4;
  color: white;
  border: none;
}

.submit-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.submit-button:not(:disabled):hover {
  background: #3367d6;
}

@media (max-width: 768px) {
  .modal-content {
    width: 95%;
  }

  .time-selector {
    flex-direction: column;
  }

  .urgency-selector {
    gap: 1rem;
  }
}
</style>