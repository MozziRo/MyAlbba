<template>
  <div class="notice-bar" :class="noticeType">
    <div class="notice-box">
      <i :class="['fas', noticeIcon, 'bouncing-icon']"></i>
      <v-slide-x-transition>
        <div class="notice-content">
          {{ noticeMessage }}
        </div>
      </v-slide-x-transition>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useSalaryStore } from '@/store/salary/salaryStore'

const salaryStore = useSalaryStore()

const today = new Date()

const hasSalaryDueToday = computed(() => {
  return salaryStore.monthlyPayments.some(payment => {
    const paymentDate = new Date(payment.salaryDate)
    return today.toDateString() === paymentDate.toDateString() &&
           payment.status === 'NOT_DONE'
  })
})

const noticeType = computed(() => {
  return hasSalaryDueToday.value ? 'urgent' : 'normal'
})

const noticeIcon = computed(() => {
  return hasSalaryDueToday.value ? 'fa-bell' : 'fa-smile'
})

const noticeMessage = computed(() => {
  if (hasSalaryDueToday.value) {
    return '💡 사장님, 오늘은 기분 좋은 월급날입니다! 정산하기를 통해 쉽고 간편하게 송금해보세요.'
  }

  const messages = [
    '💪 오늘도 열심히 일하는 직원들과 함께 성장하는 하루 보내세요!',
    '📊 직원들의 근무 현황을 확인하고 효율적으로 관리해보세요.',
    '🌟 좋은 사장님이 되는 길, 저희가 함께 하겠습니다.',
    '📈 직원들과 함께 성장하는 비즈니스를 만들어가요.'
  ]

  return messages[Math.floor(Math.random() * messages.length)]
})
</script>

<style scoped>
.notice-bar {
  padding: 0.8rem 1.5rem;
  background: rgba(66, 71, 105, 0.05);
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.notice-box {
  display: flex;
  align-items: center;
  gap: 12px;
}

.notice-content {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 80%;
  color: #202020;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

.bouncing-icon {
  font-size: 1.2em;
  animation: bounce 2s infinite;
}

.normal {
  background-color: rgba(66, 71, 105, 0.05);
}

.normal i {
  color: #424769;
}

@media (max-width: 600px) {
  .notice-content {
    max-width: 60%;
  }

  .notice-bar {
    padding: 0.8rem 1rem;
  }
}
</style>
