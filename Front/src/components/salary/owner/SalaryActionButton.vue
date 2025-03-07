<template>
  <button
    :class="['salary-action-button', status.toLowerCase()]"
    :disabled="status === 'DONE' || isLoading"
    @click="openModal"
  >
    <i v-if="isLoading" class="fas fa-spinner fa-spin"></i>
    <span v-else>{{ buttonText }}</span>
  </button>

  <!-- 커스텀 모달 -->
  <ConfirmModal 
    :isOpen="isModalOpen" 
    :message="message"
    @confirm="handleCalculate"
    @close="closeModal"
  />
</template>

<script setup>
import { computed, ref } from 'vue'
import { useSalaryStore } from '@/store/salary/salaryStore'
import ConfirmModal from './ConfirmModal.vue'

const salaryStore = useSalaryStore()

const props = defineProps({
  status: {
    type: String,
    required: true,
    validator: value => ['DONE', 'NOT_DONE'].includes(value)
  },
  salaryId: {
    type: Number,
    required: true,
  },
  workerName: {
    type: String,
    required: true
  },
  monthlySalary: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['click'])
const isLoading = ref(false)
const isModalOpen = ref(false)

const buttonText = computed(() => {
  return props.status === 'DONE' ? '메일발송완료' : '정산완료'
})


// 모달 열기
const openModal = () => {
  isModalOpen.value = true
}

// 모달 닫기
const closeModal = () => {
  isModalOpen.value = false
}

// 정산하기 api 호출
const handleCalculate = async () => {
  closeModal()
  isLoading.value = true
  try {
    await salaryStore.completeSalaryCalculation(props.salaryId)
    alert('급여 정산이 완료되었습니다.')
  } catch (error) {
    alert('급여 정산 중 오류가 발생했습니다.')
  } finally {
    isLoading.value = false
  }
}

const message = computed(() => {
  return `<strong>${props.workerName}</strong> 님에게 <strong>${props.monthlySalary.toLocaleString()}원</strong>을 <br> 보낼까요?`
})


</script>

<style scoped>
.salary-action-button {
  padding: 8px 16px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s;
  min-width: 90px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.salary-action-button.not_done {
  background-color: #424769;
  color: white;
}

.salary-action-button.not_done:hover:not(:disabled) {
  background-color: #2D3250;
}

.salary-action-button.done {
  background-color: #9E9E9E;
  color: white;
  cursor: not-allowed;
}

.salary-action-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.fa-spin {
  animation: spin 1s linear infinite;
}
</style>
