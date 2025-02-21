<template>
  <div class="modal-overlay" @click="$emit('close')">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>{{ monthlyPayment.workerName }}</h2>
        <button class="close-button" @click="$emit('close')">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <form @submit.prevent="handleSave" class="settings-form">
        <div class="form-group">
          <label>시급</label>
          <div class="input-with-addon">
            <input
              type="number"
              v-model="formData.hourlyWage"
              min="9860"
              step="10"
              required
            >
            <span class="input-addon">원</span>
          </div>
        </div>

        <div class="form-group checkbox-group">
          <label class="checkbox-label">
            <input
              type="checkbox"
              v-model="formData.includeTax"
            >
            <span class="checkbox-text">세금 포함(3.3% 적용)</span>
          </label>

          <label class="checkbox-label">
            <input
              type="checkbox"
              v-model="formData.includeInsurance"
            >
            <span class="checkbox-text">4대보험 포함</span>
          </label>

          <label class="checkbox-label">
            <input
              type="checkbox"
              v-model="formData.includeHolidayPay"
            >
            <span class="checkbox-text">주휴수당 포함</span>
          </label>
        </div>

        <div class="button-group">
          <button type="button" class="cancel-button" @click="$emit('close')" :disabled="salaryStore.isLoading">취소</button>
          <button type="submit" class="save-button" :disabled="salaryStore.isLoading">
            <i :class="['fas', salaryStore.isLoading ? 'fa-spinner fa-spin' : 'fa-save']"></i>
            {{ salaryStore.isLoading ? '저장 중...' : '저장' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useSalaryStore } from '@/store/salary/salaryStore'

// 선택한 월급(worker) 정보
const props = defineProps({
  monthlyPayment: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'save'])

const salaryStore = useSalaryStore()

// 시급을 백에서 관리한다면?
const formData = ref({
  hourlyWage: 10030,
  workerAccount: props.monthlyPayment.workerAccount,
  salaryDate: props.monthlyPayment.salaryDate,
  bank: props.monthlyPayment.bank || '',
  includeTax: true,
  includeInsurance: true,
  includeHolidayPay: false
})

// 세금과 보험 비율을 백에서 관리 한다면?
const handleSave = async () => {
  salaryStore.isLoading = true

  try {
    const salaryData = {
      workerId: props.monthlyPayment.workerId,
      hourlySalary: formData.value.hourlyWage,
      tax: formData.value.includeTax ? 3.3 : 0,
      insurance: formData.value.includeInsurance ? 9.4 : 0,
      holidayPay: formData.value.includeHolidayPay
    }

    await salaryStore.updateWorkerSalarySettings(salaryData)
    emit('close')
  } catch (error) {
    console.error('설정 저장 중 오류 발생:', error)
  } finally {
    salaryStore.isLoading = false
  }
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
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  border-bottom: 1px solid #e2e8f0;
}

.modal-header h2 {
  margin: 0;
  font-size: 1.25rem;
  color: #2d3748;
}

.close-button {
  background: none;
  border: none;
  color: #718096;
  cursor: pointer;
  font-size: 1.25rem;
}

.settings-form {
  padding: 24px;
}

.form-group {
  margin-bottom: 20px;
}

.input-with-addon {
  display: flex;
  align-items: center;
}

.input-addon {
  padding: 8px 12px;
  background-color: #f7fafc;
  border: 1px solid #e2e8f0;
  border-left: none;
  border-radius: 0 4px 4px 0;
  color: #4a5568;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #4a5568;
}

input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  transition: border-color 0.2s;
}

input:focus {
  outline: none;
  border-color: #424769;
  box-shadow: 0 0 0 3px rgba(66, 71, 105, 0.1);
}

select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  background-color: white;
  color: #4a5568;
}

.input-hint {
  display: block;
  margin-top: 4px;
  color: #718096;
  font-size: 0.875rem;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 169px;
}

.cancel-button {
  padding: 8px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background-color: white;
  color: #4a5568;
  transition: all 0.2s;
}

.cancel-button:hover {
  background-color: #f7fafc;
}

.save-button {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background-color: #424769;
  color: white;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.save-button:hover {
  background-color: #2D3250;
}

.checkbox-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.checkbox-label input[type="checkbox"] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.checkbox-text {
  color: #4a5568;
  font-size: 0.95rem;
}
</style>
