<template>
  <section class="time-section">
    <h4 class="section-title">시간대 선택</h4>
    
    <div class="time-container">
      <!-- Selected Times Display -->
      <div class="selected-times" v-if="selectedTimes.length > 0">
        <div 
          v-for="time in selectedTimes" 
          :key="time"
          class="time-capsule"
          @click="removeTime(time)"
        >
          {{ formatTime(time) }}
          <span class="remove-icon">×</span>
        </div>
      </div>

      <!-- Time Selection -->
      <div class="time-columns">
        <!-- Hours Column -->
        <div class="time-column">
          <div class="column-header">시</div>
          <div class="column-content">
            <div 
              v-for="hour in 24" 
              :key="hour-1"
              class="time-item"
              :class="{ 
                selected: selectedHour === (hour-1),
                selectable: isHourSelectable(hour-1)
              }"
              @click="selectHour(hour-1)"
            >
              {{ String(hour-1).padStart(2, '0') }}
            </div>
          </div>
        </div>

        <!-- Minutes Column -->
        <div class="time-column">
          <div class="column-header">분</div>
          <div class="column-content">
            <div 
              v-for="minute in [0, 15, 30, 45]" 
              :key="minute"
              class="time-item"
              :class="{ 
                selected: selectedMinute === minute,
                selectable: isMinuteSelectable(minute)
              }"
              @click="selectMinute(minute)"
            >
              {{ String(minute).padStart(2, '0') }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed } from 'vue'
import { formatTime } from '@/common/apis/utils/date'

const props = defineProps({
  selectedStartTime: {
    type: String,
    default: ''
  },
  selectedEndTime: {
    type: String,
    default: ''
  }
})

const emit = defineEmits([
  'update:selectedStartTime',
  'update:selectedEndTime',
  'time-selected'
])

// State
const selectedTimes = ref([])
const startTime = ref('')
const endTime = ref('')
const selectedHour = ref(null)
const selectedMinute = ref(null)
const selectingStartTime = ref(true)

// Methods
const createTimeString = (hour, minute) => {
  return `${String(hour).padStart(2, '0')}:${String(minute).padStart(2, '0')}`
}

const isHourSelectable = (hour) => {
  if (selectingStartTime.value) return true
  const [startHour] = startTime.value.split(':').map(Number)
  return hour > startHour
}

const isMinuteSelectable = (minute) => {
  if (!selectedHour.value) return false
  
  if (!selectingStartTime.value) {
    const [startHour, startMinute] = startTime.value.split(':').map(Number)
    if (selectedHour.value === startHour) {
      return minute > startMinute
    }
  }
  
  return true
}

const selectHour = (hour) => {
  if (!isHourSelectable(hour)) return
  selectedHour.value = hour
  selectedMinute.value = null
}

const selectMinute = (minute) => {
  if (!isMinuteSelectable(minute)) return
  selectedMinute.value = minute
  
  const timeString = createTimeString(selectedHour.value, minute)
  
  if (selectingStartTime.value) {
    startTime.value = timeString
    selectedTimes.value = [timeString]
    emit('update:selectedStartTime', timeString)
    selectingStartTime.value = false
    selectedHour.value = null
    selectedMinute.value = null
  } else {
    endTime.value = timeString
    selectedTimes.value = [startTime.value, timeString]
    emit('update:selectedEndTime', timeString)
    emit('time-selected')
  }
}

const removeTime = (time) => {
  if (time === startTime.value) {
    startTime.value = ''
    endTime.value = ''
    selectedTimes.value = []
    selectedHour.value = null
    selectedMinute.value = null
    selectingStartTime.value = true
    emit('update:selectedStartTime', '')
    emit('update:selectedEndTime', '')
  } else if (time === endTime.value) {
    endTime.value = ''
    selectedTimes.value = [startTime.value]
    selectedHour.value = null
    selectedMinute.value = null
    emit('update:selectedEndTime', '')
  }
}
</script>

<style scoped>
.time-section {
  padding: 20px 0;
}

.section-title {
  font-size: 15px;
  font-weight: 500;
  color: #1a1a1a;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eeeeee;
}

.time-container {
  padding: 4px 0;
}

.selected-times {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 16px;
}

.time-capsule {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  background-color: rgba(7, 112, 42, 0.1);
  color: var(--color-primary);
  border-radius: 100px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.time-capsule:hover {
  background-color: rgba(7, 112, 42, 0.15);
}

.remove-icon {
  margin-left: 6px;
  font-size: 16px;
}

.time-columns {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 4px;
}

.time-column {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.column-header {
  padding: 8px;
  font-size: 13px;
  color: var(--color-primary);
  font-weight: 500;
  text-align: center;
}

.column-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 300px;
  overflow-y: auto;
  padding: 4px;
}

.column-content::-webkit-scrollbar {
  width: 6px;
}

.column-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.column-content::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 3px;
}

.time-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 36px;
  padding: 0 12px;
  background-color: #f5f5f5;
  border-radius: 100px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
  opacity: 0.6;
}

.time-item.selectable {
  opacity: 1;
  cursor: pointer;
}

.time-item.selectable:hover {
  background-color: rgba(7, 112, 42, 0.05);
}

.time-item.selected {
  background-color: rgba(7, 112, 42, 0.1);
  color: var(--color-primary);
  font-weight: 500;
  opacity: 1;
}

@media (max-width: 900px) {
  .time-item {
    height: 32px;
    font-size: 13px;
  }
}
</style>