// components/schedule/DateSelection.vue
<template>
  <section class="section date-section">
    <h4 class="section-title">
      <i class="icon-calendar"></i> 날짜 선택
    </h4>
    <div class="calendar-container">
      <div class="calendar-header">
        <button class="nav-button" @click="changeMonth(-1)">&lt;</button>
        <span class="current-month">{{ currentYear }}년 {{ currentMonth }}월</span>
        <button class="nav-button" @click="changeMonth(1)">&gt;</button>
      </div>
      
      <table class="calendar-table">
        <thead>
          <tr>
            <th v-for="day in weekDays" :key="day">{{ day }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="week in calendarWeeks" :key="week[0]?.date || week">
            <td v-for="day in week" :key="day?.date || day">
              <button
                v-if="day?.date"
                @click="selectDay(day.date)"
                class="date-button"
                :class="{
                  'selected': selectedDays.includes(day.date),
                  'today': checkIsToday(day.date)
                }"
              >
                {{ day.date }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="selected-dates">
        <span 
          v-for="day in selectedDays" 
          :key="day"
          class="date-chip"
        >
          {{ day }}일
          <button 
            class="remove-date"
            @click="removeDay(day)"
          >
            ×
          </button>
        </span>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed } from 'vue'
import { formatDate, isToday } from '@/common/apis/utils/date'

const props = defineProps({
  selectedDays: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['update:selectedDays'])

const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth() + 1)

const weekDays = ['일', '월', '화', '수', '목', '금', '토']

const calendarWeeks = computed(() => {
  const weeks = []
  const firstDay = new Date(currentYear.value, currentMonth.value - 1, 1)
  const lastDay = new Date(currentYear.value, currentMonth.value, 0)
  
  let currentWeek = Array(firstDay.getDay()).fill(null)
  
  for (let day = 1; day <= lastDay.getDate(); day++) {
    currentWeek.push({ date: day })
    
    if (currentWeek.length === 7) {
      weeks.push(currentWeek)
      currentWeek = []
    }
  }
  
  if (currentWeek.length > 0) {
    currentWeek = currentWeek.concat(Array(7 - currentWeek.length).fill(null))
    weeks.push(currentWeek)
  }
  
  return weeks
})

const changeMonth = (increment) => {
  const newMonth = currentMonth.value + increment
  if (newMonth > 12) {
    currentMonth.value = 1
    currentYear.value++
  } else if (newMonth < 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value = newMonth
  }
  emit('update:selectedDays', []) // 월 변경 시 선택 초기화
}

const checkIsToday = (day) => {
  const checkDate = new Date(currentYear.value, currentMonth.value - 1, day)
  return isToday(checkDate)
}

const selectDay = (day) => {
  const newSelection = [...props.selectedDays]
  const index = newSelection.indexOf(day)
  
  if (index > -1) {
    newSelection.splice(index, 1)
  } else {
    newSelection.push(day)
  }
  
  emit('update:selectedDays', newSelection)
}

const removeDay = (day) => {
  const newSelection = props.selectedDays.filter(d => d !== day)
  emit('update:selectedDays', newSelection)
}
</script>

<style scoped>
.date-section {
  background-color: var(--color-white);
  border-radius: var(--border-radius);
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--color-primary);
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
}

.calendar-container {
  width: 100%;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.current-month {
  font-size: 16px;
  font-weight: 600;
}

.nav-button {
  padding: 8px 12px;
  border: none;
  background: none;
  color: var(--color-primary);
  cursor: pointer;
  font-size: 18px;
}

.nav-button:hover {
  background-color: rgba(7, 112, 42, 0.08);
  border-radius: var(--border-radius);
}

.calendar-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 4px;
}

.calendar-table th {
  padding: 8px;
  font-size: 14px;
  color: var(--color-gray);
  font-weight: 600;
}

.calendar-table td {
  padding: 0;
  height: 40px;
  vertical-align: middle;
  text-align: center;
}

.date-button {
  width: 36px;
  height: 36px;
  border: none;
  border-radius: 50%;
  background: none;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  font-weight: 500;
}

.date-button:hover {
  background-color: rgba(7, 112, 42, 0.08);
}

.date-button.selected {
  background-color: rgba(7, 112, 42, 0.15);
  color: var(--color-primary);
  transform: scale(1.15);
  font-weight: 600;
}

.date-button.selected::after {
  content: '';
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border: 2px solid var(--color-primary);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.date-button.today {
  border: 2px solid var(--color-primary);
  color: var(--color-primary);
}

.selected-dates {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 16px;
}

.date-chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  background-color: rgba(7, 112, 42, 0.08);
  color: var(--color-primary);
  border-radius: 16px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.date-chip:hover {
  background-color: rgba(7, 112, 42, 0.15);
}

.remove-date {
  border: none;
  background: none;
  color: var(--color-primary);
  cursor: pointer;
  padding: 2px 4px;
  font-size: 16px;
  opacity: 0.8;
  transition: all 0.2s ease;
}

.remove-date:hover {
  opacity: 1;
}

@keyframes pulse {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.4;
  }
  100% {
    transform: scale(1);
    opacity: 0.8;
  }
}

@media (max-width: 900px) {
  .calendar-table {
    border-spacing: 2px;
  }

  .date-button {
    width: 32px;
    height: 32px;
    font-size: 14px;
  }
}
</style>