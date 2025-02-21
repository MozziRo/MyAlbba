<template>
  <div class="salary-table">
    <table>
      <thead>
        <tr>
          <th>알바생</th>
          <th>급여지급일</th>
          <th>월급</th>
          <th>계좌번호</th>
          <th>정산현황</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="monthlyPayment in salaryStore.monthlyPayments"
          :key="monthlyPayment.id"
          :class="{ 'payment-due': isPaymentDue(monthlyPayment.salaryDate, monthlyPayment.status) }"
        >
          <td>{{ monthlyPayment.workerName }}</td>
          <td>{{ formatDate(monthlyPayment.salaryDate) }}</td>
          <td class="salary-cell">
            <SalaryTooltip
              :monthly-salary="monthlyPayment.monthlySalary"
              :total-hours="monthlyPayment.totalHours"
            />
          </td>
          <td
            @click="copyToClipboard(monthlyPayment.workerAccount, $event)"
            role="button"
            title="계좌번호 복사하기"
          >
            <span>{{ formatAccount(monthlyPayment.workerAccount, monthlyPayment.bank) }}</span>
            <i class="fas fa-clipboard" style="margin-left: 8px;"></i>
          </td>
          <td>
            <!-- 정산하기 버튼입니다~ -->
            <SalaryActionButton
              :status="monthlyPayment.status"
              :salaryId="monthlyPayment.salaryId"
              :workerName="monthlyPayment.workerName"
              :monthlySalary="monthlyPayment.monthlySalary"
            />
          </td>
          <td>
            <button class="settings-button" @click="openSettingsModal(monthlyPayment)">
              <i class="fas fa-cog"></i>
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <WorkerSettingsModal
      v-if="selectedMonthlyPayment"
      :monthlyPayment="selectedMonthlyPayment"
      @close="selectedMonthlyPayment = null"
    />
    <!-- 계좌 번호 복사시 생기는 메시지 -->
    <NotificationPopup
      :message="'계좌번호가 복사되었습니다'"
      :show="showNotification"
      :position="notificationPosition"
    />
  </div>
</template>

<script setup>
import { computed, ref, watch, nextTick, onMounted } from 'vue'
import SalaryActionButton from './SalaryActionButton.vue'
import WorkerSettingsModal from './WorkerSettingsModal.vue'
import SalaryTooltip from './SalaryTooltip.vue'
import NotificationPopup from '@/components/common/NotificationPopup.vue'
import { useSalaryStore } from '@/store/salary/salaryStore'
const salaryStore = useSalaryStore()

const selectedMonthlyPayment = ref(null)


const openSettingsModal = (monthlyPayment) => {
  selectedMonthlyPayment.value = monthlyPayment
}

const formatAccount = (account, bankName) => {
  return `${account.replace(/(\d{4})(\d{4})(\d{4})/, '$1-$2-$3')} (${bankName})`
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

const isPaymentDue = (date, status) => {
  const today = new Date()
  const paymentDate = new Date(date)
  return paymentDate < today && status === 'NOT_DONE'
}

const showNotification = ref(false)
const notificationPosition = ref({ x: 0, y: 0 })

const copyToClipboard = async (account, event) => {
  try {
    await navigator.clipboard.writeText(account)

    // 이전 알림이 표시 중이더라도 새로운 위치와 함께 다시 트리거
    showNotification.value = false
    nextTick(() => {
      notificationPosition.value = {
        x: event.clientX,
        y: event.clientY
      }
      showNotification.value = true
    })
  } catch (err) {
    console.error('클립보드 복사 실패:', err)
  }
}

onMounted(async () => {
  await salaryStore.fetchSalaryList()
})

</script>

<style scoped>
.salary-table {
  position: relative;
  width: 100%;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(66, 71, 105, 0.1);  /* 이전: rgba(0, 0, 0, 0.1) */
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 800px;
}

th, td {
  padding: 16px;
  text-align: left;
  border-bottom: 1px solid #edf2f7;
}

th {
  background-color: #f8fafc;
  font-weight: 600;
  color: #4a5568;
  white-space: nowrap;
}

td {
  color: #2d3748;
}

tbody tr:hover {
  background-color: rgba(66, 71, 105, 0.05);  /* 이전: #f7fafc */
}

tbody tr:last-child td {
  border-bottom: none;
}

.salary-cell {
  position: relative;
}

.salary-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-icon {
  color: #424769;  /* 이전: #4a5568 */
  font-size: 0.875rem;
  cursor: pointer;
}

.payment-due {
  animation: payment-due-flash 2s infinite;
}

@keyframes payment-due-flash {
  0% { background-color: #fff; }
  50% { background-color: rgba(249, 177, 122, 0.2); }  /* 이전: #fee2e2 */
  100% { background-color: #fff; }
}

.settings-button {
  background: none;
  border: none;
  color: #718096;
  cursor: pointer;
  padding: 8px;
  border-radius: 4px;
  transition: all 0.2s;
  font-size: 1.1rem;
}

.settings-button:hover {
  background-color: rgba(66, 71, 105, 0.1);  /* 이전: #f1f5f9 */
  color: #424769;  /* 이전: #2d3748 */
}
</style>
