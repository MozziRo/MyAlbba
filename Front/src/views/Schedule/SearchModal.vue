<script setup>
import { ref, computed, onMounted } from 'vue';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { Search, Calendar } from 'lucide-vue-next';
import { formatTime, formatDate } from '@/common/apis/utils/date';
import DateSelection from './DateSelection.vue';

const store = useScheduleStore();
const searchQuery = ref('');
const searchType = ref('name');
const showCalendar = ref(false);
const selectedDays = ref([]);

onMounted(async () => {
  try {
    await store.fetchAllSchedules();
  } catch (error) {
    console.error('스케줄 조회 실패:', error);
  }
});

const filteredSchedules = computed(() => {
  if (!searchQuery.value) return [];
  
  return store.schedules.filter(schedule => {
    if (searchType.value === 'name') {
      return schedule.workerName?.toLowerCase().includes(searchQuery.value.toLowerCase());
    } else {
      const searchTerm = searchQuery.value.trim();
      
      if (/^\d{1,2}$/.test(searchTerm)) {
        const day = new Date(schedule.date).getDate();
        return day === parseInt(searchTerm);
      }
      
      return schedule.date?.includes(searchQuery.value);
    }
  });
});

const emit = defineEmits(['close', 'select-date']);

const handleSelect = (schedule) => {
  emit('select-date', new Date(schedule.date));
  emit('close');
};

const handleDateSelect = (days) => {
  if (days.length > 0) {
    const selectedDay = days[0];
    const currentDate = new Date();
    const selectedDate = new Date(
      currentDate.getFullYear(),
      currentDate.getMonth(),
      selectedDay
    );
    searchQuery.value = formatDate(selectedDate);
    showCalendar.value = false;
    selectedDays.value = [];
  }
};

const handleClickOutside = () => {
  showCalendar.value = false;
  selectedDays.value = [];
};

const displayDate = (dateString) => {
  const date = new Date(dateString);
  return `${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일`;
};

const getPlaceholder = computed(() => {
  if (searchType.value === 'name') {
    return '직원 이름으로 검색';
  }
  return '날짜로 검색 또는 달력에서 선택';
});
</script>

<template>
  <div class="search-modal">
    <div class="modal-content">
      <div class="search-header">
        <div class="search-input-wrapper">
          <Search class="search-icon" />
          <input
            v-model="searchQuery"
            :placeholder="getPlaceholder"
            class="search-input"
          />
          <button 
            v-if="searchType === 'date'" 
            class="calendar-picker-button"
            @click="showCalendar = !showCalendar"
          >
            <Calendar class="calendar-icon" />
          </button>
          
          <div v-if="showCalendar" class="calendar-picker">
            <DateSelection 
              v-model:selectedDays="selectedDays"
              @update:selectedDays="handleDateSelect"
              @click:outside="handleClickOutside"
            />
          </div>
        </div>
        <div class="search-type">
          <button
            :class="['type-button', { active: searchType === 'name' }]"
            @click="searchType = 'name'"
          >
            이름
          </button>
          <button
            :class="['type-button', { active: searchType === 'date' }]"
            @click="searchType = 'date'"
          >
            날짜
          </button>
        </div>
      </div>

      <div class="search-results">
        <div
          v-for="schedule in filteredSchedules"
          :key="`${schedule.date}-${schedule.workerName}`"
          class="result-item"
          @click="handleSelect(schedule)"
        >
          <div class="result-date">{{ displayDate(schedule.date) }}</div>
          <div class="result-info">
            <span class="result-name">{{ schedule.workerName }}</span>
            <span class="result-time">
              {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
            </span>
          </div>
        </div>
      </div>

      <button class="close-button" @click="emit('close')">닫기</button>
    </div>
  </div>
</template>

<style scoped>
.search-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.search-header {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 1rem;
  color: var(--color-primary, #424769);
}

.calendar-picker-button {
  position: absolute;
  right: 1rem;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--color-primary, #424769);
  display: flex;
  align-items: center;
  padding: 4px;
  border-radius: 4px;
  transition: all 0.2s;
}

.calendar-picker-button:hover {
  background: rgba(197, 202, 232, 0.1);
}

.calendar-picker {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 3rem;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
  padding-right: 3rem;
}

.search-input:focus {
  outline: none;
  border-color: var(--color-primary, #424769);
  box-shadow: 0 0 0 2px rgba(7, 112, 42, 0.1);
}

.search-type {
  display: flex;
  gap: 0.5rem;
}

.type-button {
 padding: 0.5rem 1rem;
 border: 1px solid #e2e8f0;
 border-radius: 6px;
 background: white;
 cursor: pointer;
 transition: all 0.2s;
}

.type-button.active {
 background: var(--color-primary, #424769);
 color: white;
 border-color: var(--color-primary, #424769);
}

.search-results {
 flex: 1;
 overflow-y: auto;
 display: flex;
 flex-direction: column;
 gap: 0.5rem;
 max-height: 400px;
}

.result-item {
 padding: 1rem;
 border: 1px solid #e2e8f0;
 border-radius: 8px;
 cursor: pointer;
 transition: all 0.2s;
 background: white;
}

.result-item:hover {
 background: rgba(7, 112, 42, 0.05);
 border-color: var(--color-primary, #424769);
}

.result-date {
 font-size: 0.875rem;
 color: var(--color-primary, #424769);
 margin-bottom: 0.25rem;
 font-weight: 500;
}

.result-info {
 display: flex;
 justify-content: space-between;
 align-items: center;
}

.result-name {
 font-weight: 600;
 color: #111827;
}

.result-time {
 color: #64748b;
 font-size: 0.875rem;
}

.close-button {
 padding: 0.75rem;
 background: var(--color-primary, #424769);
 color: white;
 border: none;
 border-radius: 8px;
 cursor: pointer;
 transition: all 0.2s;
 font-weight: 500;
}

.close-button:hover {
 background: #2D3250;
}

.search-results::-webkit-scrollbar {
 width: 8px;
}

.search-results::-webkit-scrollbar-track {
 background: #f1f1f1;
 border-radius: 4px;
}

.search-results::-webkit-scrollbar-thumb {
 background: #c1c1c1;
 border-radius: 4px;
}

.search-results::-webkit-scrollbar-thumb:hover {
 background: #a8a8a8;
}
</style>