<template>
  <div class="card salary-card">
    <div class="card-header">
      <div class="card-icon">
        <div class="avatar" :class="isOwner ? 'owner-avatar' : 'worker-avatar'">
          <i class="icon" :class="isOwner ? 'mdi mdi-account-group' : 'mdi mdi-cash-multiple'"></i>
        </div>
      </div>
      <div class="card-title-section">
        <h3 class="card-title">
          {{ isOwner ? '미정산 급여' : '이번 달 급여' }}
        </h3>
        <span class="card-date">{{ currentMonth }}</span>
      </div>
    </div>
    <div class="card-body">
      <!-- 알바생 모드 -->
      <div v-if="!isOwner">
        <div v-if="monthlyPayment" class="salary-info">
          <div class="info-row">
            <span>예상 근무시간</span>
            <span class="value">{{ monthlyPayment.totalHours }}시간</span>
          </div>
          <div class="info-row">
            <span>예상 급여</span>
            <span class="value highlight">{{ formatCurrency(monthlyPayment.monthlySalary) }}원</span>
          </div>
        </div>
        <div v-else class="no-data">
          급여 정보가 없습니다
        </div>
      </div>
      <!-- 사장님 모드 -->
      <div v-else>
        <div v-if="monthlyPayments.length > 0" class="salary-info">
          <div class="info-row">
            <span>미정산 인원</span>
            <span class="value">{{ unsettledWorkersCount }}명</span>
          </div>
          <div class="info-row">
            <span>총 지급 예정액</span>
            <span class="value highlight">{{ formatCurrency(totalUnsettledSalary) }}원</span>
          </div>
        </div>
        <div v-else class="no-data">
          미정산 급여 정보가 없습니다
        </div>
      </div>
    </div>
    <div class="card-footer">
      <span class="chip" :class="isOwner ? ownerStatusColor : workerStatusColor">
        {{ isOwner ? getOwnerPaymentStatus : getWorkerPaymentStatus }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMyMonthlyPayment, getAllMonthlyPayments } from '@/common/apis/api/salary'
import { getCurrentUser } from '@/common/apis/utils/tokenUtils'

const monthlyPayment = ref(null)
const monthlyPayments = ref([])

const currentUser = getCurrentUser()
const isOwner = ref(currentUser && currentUser.role === 'owner')

onMounted(async () => {
  try {
    if (isOwner.value) {
      const response = await getAllMonthlyPayments()
      // 정산 상태가 DONE이 아닌 항목들을 필터링
      monthlyPayments.value = response.filter(payment => payment.status !== 'DONE')
    } else {
      const response = await getMyMonthlyPayment()
      monthlyPayment.value = response[0]
    }
  } catch (error) {
    console.error('급여 정보 로딩 실패:', error)
  }
})

const currentMonth = computed(() => {
  return new Date().toLocaleString('ko-KR', {
    month: 'long'
  })
})

const formatCurrency = (value) => {
  return value?.toLocaleString() ?? '0'
}

// === 알바생(Worker) 관련 computed ===
const getWorkerPaymentStatus = computed(() => {
  if (!monthlyPayment.value) return '급여 정보 없음'
  if (monthlyPayment.value.status === 'DONE') return '정산 완료'
  const payDate = new Date(monthlyPayment.value.salaryDate)
  const today = new Date()
  const diffDays = Math.ceil((payDate - today) / (1000 * 60 * 60 * 24))
  return `급여일까지 ${diffDays}일`
})

const getWorkerDayColor = computed(() => {
  if (!monthlyPayment.value) return 'grey'
  if (monthlyPayment.value.status === 'DONE') return 'success'
  const payDate = new Date(monthlyPayment.value.salaryDate)
  const today = new Date()
  const diffDays = Math.ceil((payDate - today) / (1000 * 60 * 60 * 24))
  return diffDays <= 3 ? 'error' : 'primary'
})

// === 사장님(Owner) 관련 computed ===
const unsettledWorkersCount = computed(() => monthlyPayments.value.length)

const totalUnsettledSalary = computed(() => {
  return monthlyPayments.value.reduce((sum, payment) => sum + payment.monthlySalary, 0)
})

const getOwnerPaymentStatus = computed(() => {
  if (!monthlyPayments.value || monthlyPayments.value.length === 0)
    return '모든 급여 정산 완료'
  const today = new Date()
  const diffDaysArr = monthlyPayments.value.map(payment => {
    const payDate = new Date(payment.salaryDate)
    return Math.ceil((payDate - today) / (1000 * 60 * 60 * 24))
  })
  const nearestDays = Math.min(...diffDaysArr)
  return `급여일까지 ${nearestDays}일`
})

const getOwnerDayColor = computed(() => {
  if (!monthlyPayments.value || monthlyPayments.value.length === 0)
    return 'success'
  const today = new Date()
  const diffDaysArr = monthlyPayments.value.map(payment => {
    const payDate = new Date(payment.salaryDate)
    return Math.ceil((payDate - today) / (1000 * 60 * 60 * 24))
  })
  const nearestDays = Math.min(...diffDaysArr)
  return nearestDays <= 3 ? 'error' : 'primary'
})

const workerStatusColor = computed(() => getWorkerDayColor.value)
const ownerStatusColor = computed(() => getOwnerDayColor.value)
</script>

<style scoped>
.card {
  background-color: #fff;
  border-radius: 16px;
  padding: 24px;
  font-family: 'Pretendard', sans-serif;
  transition: transform 0.2s ease;
  margin-bottom: 16px;
  height: 320px;
}

.card:hover {
  transform: translateY(-4px);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.card-icon {
  margin-right: 16px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.owner-avatar {
  background-color: rgba(66, 71, 105, 0.1);
}

.worker-avatar {
  background-color: rgba(66, 71, 105, 0.1);
}

.avatar .icon {
  font-size: 24px;
  color: #F9B17A;
}

.card-title-section {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.2rem;
  margin: 0;
  font-weight: 600;
}

.card-date {
  font-size: 0.875rem;
  color: #999;
}

.card-body {
  min-height: 160px;
  padding: 20px 0;
  font-size: 1rem;
  color: #333;
}

.salary-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
}

.value {
  font-weight: 500;
}

.highlight {
  color: #424769;
  font-size: 1.1em;
}

.no-data {
  color: #666;
  font-style: italic;
  text-align: center;
  padding: 1rem 0;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 24px;
}

.chip {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 0.875rem;
  color: #fff;
}

.chip.primary {
  background-color: #424769;
}

.chip.error {
  background-color: #F9B17A;
}

.chip.success {
  background-color: #424769;
}
</style>
