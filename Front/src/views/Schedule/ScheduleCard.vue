<!-- ScheduleCard.vue -->
<template>
  <div class="mini-calendar">
    <div class="calendar-header">
      {{ currentMonth }}월
    </div>
    
    <div class="calendar-grid">
      <div v-for="day in weekDays" :key="day" class="week-day" :style="{ color: day === '일' ? '#ff4444' : day === '토' ? '#4444ff' : '#333' }">
        {{ day }}
      </div>
      
      <div 
        v-for="date in calendarDates" 
        :key="date.day"
        class="date"
        :class="{
          'other-month': !date.isCurrentMonth,
          'today': date.isToday,
          'has-schedule': hasSchedule(date),
          'sunday': isWeekend(date.fullDate) && date.fullDate.getDay() === 0,
          'saturday': isWeekend(date.fullDate) && date.fullDate.getDay() === 6
        }"
      >
        {{ date.day }}
        <div v-if="hasSchedule(date) && date.isCurrentMonth" class="schedule-tooltip">
          {{ getScheduleTime(date) }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScheduleStore } from '@/store/member/scheduleStore'
import { useMemberStore } from '@/store/member/memberStore';
import { storeToRefs } from 'pinia';
import { isToday, isWeekend, isSameDay, formatTime } from '@/common/apis/utils/date'; // 유틸 함수 임포트

const store = useScheduleStore()
const memberStore = useMemberStore();

// 2025년 2월로 초기값 변경
const currentDate = ref(new Date(2025, 1, 1))
const weekDays = ['일', '월', '화', '수', '목', '금', '토']
const currentMonth = computed(() => currentDate.value.getMonth() + 1)
const { isOwner } = storeToRefs(memberStore);

onMounted(async () => {
  if(!isOwner.value)
  await store.fetchMySchedules()
})

/**
 * 해당 날짜에 스케줄이 있는지 확인
 * @param {Object} date - 날짜 객체
 * @returns {boolean} 스케줄 존재 여부
 */
const hasSchedule = (date) => {
  if (!date.isCurrentMonth) return false
  return store.mySchedules.some(schedule => 
    isSameDay(new Date(schedule.date), date.fullDate)
  )
}

/**
 * 해당 날짜의 스케줄 시간을 반환
 * @param {Object} date - 날짜 객체
 * @returns {string} 스케줄 시간 (시작-종료)
 */
const getScheduleTime = (date) => {
  const schedule = store.mySchedules.find(schedule => 
    isSameDay(new Date(schedule.date), date.fullDate)
  )
  return schedule ? `${formatTime(schedule.startTime)} - ${formatTime(schedule.endTime)}` : ''
}

/**
 * 달력에 표시할 날짜 데이터 계산
 */
const calendarDates = computed(() => {
  const dates = []
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()

  const firstDay = new Date(year, month, 1)
  const lastDay = new Date(year, month + 1, 0)

  // 이전 달의 날짜들
  const prevMonthDays = firstDay.getDay()
  for (let i = prevMonthDays - 1; i >= 0; i--) {
    const date = new Date(firstDay)
    date.setDate(date.getDate() - i)
    dates.push({
      day: date.getDate(),
      isCurrentMonth: false,
      isToday: isToday(date),
      fullDate: date
    })
  }

  // 현재 달의 날짜들
  for (let i = 1; i <= lastDay.getDate(); i++) {
    const date = new Date(year, month, i)
    dates.push({
      day: i,
      isCurrentMonth: true,
      isToday: isToday(date),
      fullDate: date
    })
  }
 
  // 다음 달의 날짜들
  const remainingDays = 42 - dates.length
  for (let i = 1; i <= remainingDays; i++) {
    const date = new Date(year, month + 1, i)
    dates.push({
      day: i,
      isCurrentMonth: false,
      isToday: false,
      fullDate: date
    })
  }

  return dates
})
</script>

<style scoped>
.mini-calendar {
  font-size: 0.75rem;
  padding: 0.5rem;
  background: white;
  max-width: 280px;
  margin: 0 auto;
}

.calendar-header {
  text-align: center;
  font-weight: 500;
  margin-bottom: 0.5rem;
  color: #333;
  font-size: 1rem;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  padding: 8px;
}

.week-day {
  text-align: center;
  padding: 0.5rem 0;
  font-size: 0.75rem;
}

.date {
  position: relative;
  text-align: center;
  padding: 0.5rem 0;
  font-size: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: auto;
}

.date:hover {
  background: #FCDDBD;
}

.other-month {
  color: #ccc;
}

.has-schedule {
  background: #e3e3e3;
}

.today.has-schedule {
  background: #e3f2fd;
}

.sunday {
  color: #ff4444;
}

.saturday {
  color: #4444ff;
}

.today {
  background:  #F9B17A;
  color: white;
  font-weight: 600;
}

.other-month.sunday,
.other-month.saturday {
  color: #ccc;
}

.schedule-tooltip {
  display: none;
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: #333;
  color: white;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.7rem;
  white-space: nowrap;
  z-index: 10;
}

.schedule-tooltip::after {
  content: '';
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border-width: 4px;
  border-style: solid;
  border-color: #333 transparent transparent transparent;
}

.date:hover .schedule-tooltip {
  display: block;
}
</style>