# components/ScheduleRegistrationModal.vue
<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h3>희망 근무 시간 등록</h3>
        <button class="close-button" @click="closeModal">×</button>
      </div>

      <div class="modal-body">
        <div class="form-section">
          <h4>반복 요일 선택</h4>
          <div class="weekday-selector">
            <label v-for="(day, index) in weekDays" :key="index" class="weekday-label">
              <input
                type="checkbox"
                v-model="selectedDays[index]"
                :value="index"
              >
              <span>{{ day }}</span>
            </label>
          </div>
        </div>

        <div class="form-section">
          <h4>근무 시간 설정</h4>
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

        <div class="form-section">
          <h4>메모</h4>
          <textarea
            v-model="memo"
            placeholder="추가 요청사항이 있다면 입력해주세요."
            rows="3"
          ></textarea>
        </div>
      </div>

      <div class="modal-footer">
        <button class="cancel-button" @click="closeModal">취소</button>
        <button class="submit-button" @click="submitSchedule">등록</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['close', 'submit'])

const weekDays = ['일', '월', '화', '수', '목', '금', '토']
const selectedDays = ref(Array(7).fill(false))
const startTime = ref('09:00')
const endTime = ref('18:00')
const memo = ref('')

const availableTimes = Array.from({ length: 24 }, (_, i) => {
  const hour = String(i).padStart(2, '0')
  return `${hour}:00`
})

const formatTime = (time) => {
  return time
}

const closeModal = () => {
  emit('close')
}

const submitSchedule = () => {
  const scheduleData = {
    days: selectedDays.value.map((selected, index) => selected ? index : -1).filter(day => day !== -1),
    startTime: startTime.value,
    endTime: endTime.value,
    memo: memo.value
  }
  emit('submit', scheduleData)
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
  margin-bottom: 1rem;
  color: #333;
}

.weekday-selector {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.weekday-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
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

.time-input select {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
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
}

.cancel-button {
  background: #f0f0f0;
  border: none;
}

.submit-button {
  background: #4285f4;
  color: white;
  border: none;
}

.submit-button:hover {
  background: #3367d6;
}

@media (max-width: 768px) {
  .weekday-selector {
    gap: 0.5rem;
  }

  .time-selector {
    flex-direction: column;
  }
}
</style>