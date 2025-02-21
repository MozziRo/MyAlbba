<template>
  <div class="worker-view">
    <div class="salary-header">
      <h2 class="month-total">이번 달 급여</h2>
      <div class="total-amount">{{ formatCurrency(totalAmount) }}원</div>
    </div>
    <div class="calendar-container">
      <table class="calendar-table">
        <thead>
          <tr>
            <th v-for="day in weekDays" :key="day">{{ day }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(week, wIndex) in calendarWeeks" :key="wIndex">
            <td v-for="(day, dIndex) in week" :key="dIndex">
              <div v-if="day" class="day-cell">
                <div class="day-content">
                  <div class="day-number">{{ day.date }}</div>
                  <div v-if="day.salary" class="salary-info">
                    <div class="work-hours">{{ day.salary.hours }}시간</div>
                    <div class="daily-amount">{{ formatCurrency(day.salary.dailyAmount) }}원</div>
                  </div>
                  <DayScheduleTooltip
                    v-if="day.salary"
                    :schedules="getSchedulesForDate(day.fullDate)"
                    :hours="day.salary.hours"
                    :daily-amount="day.salary.dailyAmount"

                  />
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getMyDailyPayments } from '@/common/apis/api/salary'
import { getMySchedules } from '@/common/apis/api/schedule'
import { isSameDay } from '@/common/apis/utils/date'
import DayScheduleTooltip from './DayScheduleTooltip.vue'



const dailyPayments = ref([
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-03",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-04",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-05",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-06",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-07",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-10",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-11",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-12",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-13",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-14",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-17",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-18",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-19",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-20",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-21",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-24",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-25",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-26",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-27",
"hours": 5.0,
"dailyAmount": 50150
},
{
"workerId": 2,
"workerName": "헬로키티",
"date": "2025-02-28",
"hours": 5.0,
"dailyAmount": 50150
}
])
const schedules = ref([
  // 초기 더미 데이터
  {
    confirmedScheduleid: 1,
    workerId: 2,
    workerName: "헬로키티",
    startTime: "13:00:00",
    endTime: "18:00:00",
    date: "2024-02-03"
  },
  {
    confirmedScheduleid: 2,

    workerId: 2,
    workerName: "헬로키티",
    startTime: "09:00:00",
    endTime: "14:00:00",
    date: "2024-02-04"

  },
  {
    confirmedScheduleid: 3,
    workerId: 2,
    workerName: "헬로키티",
    startTime: "15:00:00",
    endTime: "20:00:00",
    date: "2024-02-05"
  }
])

const weekDays = ['일', '월', '화', '수', '목', '금', '토']

// API 호출 함수들
const fetchData = async () => {
  try {
    const [payments, scheduleData] = await Promise.all([
      getMyDailyPayments(),
      getMySchedules()
    ])
    console.log('스케줄 데이터:', scheduleData) // 데이터 확인용 로그
    dailyPayments.value = payments
    schedules.value = scheduleData
  } catch (error) {
    console.error('데이터 조회 실패:', error)
  }
}

onMounted(() => {
  fetchData()
})

// 시간 포맷 함수
const formatTime = (time) => {
  return time.substring(0, 5) // "HH:MM:SS" -> "HH:MM"
}

// 날짜별 스케줄 그룹화 함수
const getSchedulesForDate = (date) => {
  console.log('날짜 확인:', date) // 날짜 확인용 로그
  console.log('현재 스케줄:', schedules.value) // 스케줄 확인용 로그
  return schedules.value.filter(schedule => 
    isSameDay(new Date(schedule.date), date)
  ).sort((a, b) => a.startTime.localeCompare(b.startTime))
}

// 현재 날짜 기준 연, 월 정보 설정
const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth() + 1)
const today = new Date()

// 이번 달 총 급여 계산
const totalAmount = computed(() => {
  return dailyPayments.value
    .filter(payment => {
      const paymentDate = new Date(payment.date)
      return paymentDate.getMonth() === currentMonth.value - 1 &&
             paymentDate.getFullYear() === currentYear.value &&
             paymentDate <= today
    })
    .reduce((sum, payment) => sum + payment.dailyAmount, 0)
})

// 달력 데이터 생성
const calendarWeeks = computed(() => {
  const weeks = []
  const firstDayDate = new Date(currentYear.value, currentMonth.value - 1, 1)
  const lastDayDate = new Date(currentYear.value, currentMonth.value, 0)
  const firstWeekDay = firstDayDate.getDay() // 0(일) ~ 6(토)

  let currentWeek = []

  // 첫 주 빈 칸 채우기
  for (let i = 0; i < firstWeekDay; i++) {
    currentWeek.push(null)
  }

  // 날짜 채우기
  for (let day = 1; day <= lastDayDate.getDate(); day++) {
    const fullDate = new Date(currentYear.value, currentMonth.value - 1, day)
    
    // 해당 날짜의 급여 정보 찾기
    const dayPayment = dailyPayments.value.find(payment => 
      isSameDay(new Date(payment.date), fullDate)
    )

    let salary = null
    if (dayPayment && fullDate <= today) {
      salary = {
        hours: dayPayment.hours,
        dailyAmount: dayPayment.dailyAmount
      }
    }

    currentWeek.push({
      date: day,
      fullDate,
      salary
    })

    if (currentWeek.length === 7) {
      weeks.push(currentWeek)
      currentWeek = []
    }
  }

  // 마지막 주 빈 칸 채우기
  if (currentWeek.length > 0) {
    while (currentWeek.length < 7) {
      currentWeek.push(null)
    }
    weeks.push(currentWeek)
  }

  return weeks
})

/**
 * 숫자를 천단위 콤마로 포맷팅하는 헬퍼 함수
 */
const formatCurrency = (value) => {
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}
</script>

<style scoped>
.worker-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.salary-header {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  margin-bottom: 20px;
}

.month-total {
  font-size: 20px;
  color: #495057;
  margin-bottom: 10px;
}

.total-amount {
  font-size: 32px;
  font-weight: bold;
  color: #424769;
}

.calendar-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.calendar-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.calendar-table th,
.calendar-table td {
  text-align: center;
  border: 1px solid #e9ecef;
  width: calc(100% / 7);
  position: relative;
  padding: 0;
}

.calendar-table td::after {
  content: '';
  display: block;
  padding-bottom: 100%;
}

.day-cell {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 8px;
}

.day-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.day-number {
  font-weight: bold;
  margin-bottom: 4px;
}

.salary-info {
  font-size: 0.875rem;
  background-color: rgba(66, 71, 105, 0.1);
  padding: 4px;
  border-radius: 4px;
  margin-top: auto;
}

.work-hours {
  color: #495057;
  margin-bottom: 2px;
}

.daily-amount {
  color: #424769;
  font-weight: bold;
}

.day-cell:hover .day-tooltip {
  display: block;
}
</style>
