# src/views/Schedule/components/WeekCalendar.vue
<template>
  <div class="calendar-container">
    <!-- 필터 섹션 -->
    <div class="filter-section">
      <div class="filter-group">
        <button
          class="filter-button"
          :class="{ active: timeFilter === 'all' }"
          @click="timeFilter = 'all'"
        >
          전체 시간
        </button>
        <button
          class="filter-button"
          :class="{ active: timeFilter === 'morning' }"
          @click="timeFilter = 'morning'"
        >
          오전
        </button>
        <button
          class="filter-button"
          :class="{ active: timeFilter === 'afternoon' }"
          @click="timeFilter = 'afternoon'"
        >
          오후
        </button>
      </div>
    </div>

    <!-- 캘린더 그리드 -->
    <div class="calendar-grid">
      <!-- 요일 헤더 -->
      <div class="header-row">
        <div class="time-column"></div>
        <div
          v-for="day in weekDays"
          :key="day.date"
          class="day-header"
        >
          <div class="day-name">{{ day.name }}</div>
          <div class="day-date">{{ formatDate(day.date) }}</div>
        </div>
      </div>

      <!-- 시간 그리드 -->
      <div class="time-grid">
        <div
          v-for="hour in filteredHours"
          :key="hour"
          class="time-row"
        >
          <div class="time-label">{{ formatTime(`${hour}:00`) }}</div>
          <div
            v-for="day in weekDays"
            :key="day.date"
            class="time-cell"
            @click="handleCellClick(day, hour)"
          >
            <div
              v-for="schedule in getSchedulesForTimeSlot(day.date, hour)"
              :key="schedule.confirmedScheduleId"
              class="schedule-item"
              :class="{
                'is-my-schedule': isMySchedule(schedule.confirmedScheduleId),
                'has-substitute-request': hasSubstituteRequest(schedule.confirmedScheduleId)
              }"
              :style="getScheduleStyle(schedule)"
            >
              <div class="schedule-content">
                <span class="worker-name">{{ schedule.workerName }}</span>
                <span class="schedule-time">
                  {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
                </span>
              </div>
              <div
                v-if="isMySchedule(schedule.confirmedScheduleId)"
                class="schedule-actions"
              >
                <button
                  v-if="store.canRequestSubstitute(schedule.confirmedScheduleId)"
                  @click.stop="handleSubstituteRequest(schedule.confirmedScheduleId)"
                  class="substitute-button"
                >
                  대타 신청
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useScheduleStore } from '@/store/member/scheduleStore'
import { useWorkerStore } from '@/store/worker/workerStore'
import { formatDate, formatTime } from '@/common/apis/utils/date'

const store = useScheduleStore()
const workerStore = useWorkerStore()

// 필터 상태
const timeFilter = ref('all')

// Props
const props = defineProps({
  currentWeek: {
    type: Date,
    default: () => new Date()
  }
})

const emit = defineEmits(['select-slot', 'request-substitute'])

// 요일 계산
const weekDays = computed(() => {
  const days = []
  const curr = new Date(props.currentWeek)
  const first = curr.getDate() - curr.getDay()

  for (let i = 0; i < 7; i++) {
    const date = new Date(curr.setDate(first + i))
    days.push({
      name: ['일', '월', '화', '수', '목', '금', '토'][i],
      date: new Date(date)
    })
  }
  return days
})

// 필터링된 시간
const filteredHours = computed(() => {
  if (timeFilter.value === 'morning') {
    return Array.from({ length: 12 }, (_, i) => i)
  }
  if (timeFilter.value === 'afternoon') {
    return Array.from({ length: 12 }, (_, i) => i + 12)
  }
  return Array.from({ length: 24 }, (_, i) => i)
})

// 스케줄 조회
const getSchedulesForTimeSlot = (date, hour) => {
  const formattedDate = formatDate(date)
  return store.schedules.filter(schedule => {
    const [scheduleHour] = schedule.startTime.split(':').map(Number)
    return schedule.date === formattedDate && scheduleHour === hour
  })
}

// 내 스케줄 확인
const isMySchedule = (confirmedScheduleId) => {
  const schedule = store.schedules.find(s => s.confirmedScheduleId === confirmedScheduleId)
  return schedule?.workerId === workerStore.worker?.workerId
}

// 대타 요청 확인
const hasSubstituteRequest = (confirmedScheduleId) => {
  return store.substituteRequests.some(request =>
    request.confirmedScheduleId === confirmedScheduleId &&
    request.status === 'requested'
  )
}

// 스케줄 스타일
const getScheduleStyle = (schedule) => {
  const [startHour, startMinute] = schedule.startTime.split(':').map(Number)
  const [endHour, endMinute] = schedule.endTime.split(':').map(Number)

  const duration = (endHour - startHour) + (endMinute - startMinute) / 60
  const style = {
    height: `${Math.max(duration * 60, 40)}px`,
  }

  return style
}

// 셀 클릭 핸들러
const handleCellClick = (day, hour) => {
  emit('select-slot', {
    date: formatDate(day.date),
    hour: hour
  })
}

// 대타 신청 핸들러
const handleSubstituteRequest = async (confirmedScheduleId) => {
  try {
    await store.requestSubstituteSchedule(confirmedScheduleId)
    alert('대타 신청이 완료되었습니다.')
  } catch (error) {
    console.error('대타 신청 실패:', error)
    alert('대타 신청에 실패했습니다.')
  }
}
</script>

<style scoped>
.calendar-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.filter-section {
  padding: 16px;
  border-bottom: 1px solid #E9EFE6;
  display: flex;
  gap: 16px;
  background: #FAFDF7;
}

.filter-group {
  display: flex;
  gap: 8px;
}

.filter-button {
  padding: 6px 12px;
  border: 1px solid #E9EFE6;
  border-radius: 6px;
  background: white;
  color: var(--primary);
  font-size: 0.875rem;
  cursor: pointer;
  transition: all 0.2s;
}

.filter-button:hover {
  background: #E9EFE6;
}

.filter-button.active {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

.calendar-grid {
  display: flex;
  flex-direction: column;
}

.header-row {
  display: flex;
  border-bottom: 1px solid #E9EFE6;
  background: #FAFDF7;
}

.time-column {
  width: 60px;
  flex-shrink: 0;
}

.day-header {
  flex: 1;
  padding: 12px;
  text-align: center;
  border-left: 1px solid #E9EFE6;
}

.day-name {
  font-weight: 600;
  color: var(--primary);
  margin-bottom: 4px;
}

.day-date {
  font-size: 0.75rem;
  color: #07702A;
  opacity: 0.8;
}

.time-grid {
  display: flex;
  flex-direction: column;
}

.time-row {
  display: flex;
  height: 60px;
  border-bottom: 1px solid #E9EFE6;
}

.time-label {
  width: 60px;
  flex-shrink: 0;
  padding: 8px;
  color: #07702A;
  font-size: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #FAFDF7;
}

.time-cell {
  flex: 1;
  border-left: 1px solid #E9EFE6;
  position: relative;
  transition: background-color 0.2s;
}

.time-cell:hover {
  background-color: #FAFDF7;
}

.schedule-item {
  position: absolute;
  left: 4px;
  right: 4px;
  min-height: 40px;
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 0.75rem;
  background: var(--primary-light);
  color: var(--primary);
  border-left: 3px solid var(--primary);
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: all 0.2s;
  z-index: 1;
  cursor: pointer;
}

.schedule-item:hover {
  transform: scale(1.02);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.schedule-item.is-my-schedule {
  background: var(--primary);
  color: white;
  border-left: 3px solid var(--primary-dark);
}

.schedule-item.has-substitute-request {
  border: 2px solid #F59E0B;
}

.schedule-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.worker-name {
  font-weight: 600;
}

.schedule-time {
  font-size: 0.7rem;
  opacity: 0.9;
}

.schedule-actions {
  margin-top: 4px;
  display: flex;
  justify-content: flex-end;
}

.substitute-button {
  padding: 2px 6px;
  border: none;
  border-radius: 4px;
  background: #F59E0B;
  color: white;
  font-size: 0.7rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.substitute-button:hover {
  background: #D97706;
}

.substitute-button:before {
  content: '↻';
  margin-right: 2px;
}

@media (max-width: 768px) {
  .day-header {
    padding: 8px;
  }

  .time-label {
    width: 50px;
  }

  .schedule-item {
    padding: 2px 4px;
    font-size: 0.7rem;
  }

  .schedule-time {
    font-size: 0.65rem;
  }

  .substitute-button {
    padding: 1px 4px;
    font-size: 0.65rem;
  }
}
</style>
