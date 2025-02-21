<!-- Calendar.vue -->
<template>
  <div class="calendar">
    <CalendarHeader
      :current-date="currentDate"
      @prev-month="currentDate = new Date(currentDate.setMonth(currentDate.getMonth() - 1))"
      @next-month="currentDate = new Date(currentDate.setMonth(currentDate.getMonth() + 1))"
    />

    <!-- 필터 컨트롤 -->
    <div class="filter-controls">
      <select
        v-model="selectedWorker"
        class="worker-filter"
      >
        <option value="all">전체 직원</option>
        <option
          v-for="worker in workers"
          :key="worker.workerId"
          :value="worker.workerId"
        >
          {{ worker.name }}
        </option>
      </select>

      <div class="time-filters">
        <button
          v-for="filter in ['all', 'morning', 'afternoon']"
          :key="filter"
          :class="['filter-button', { active: scheduleFilter === filter }]"
          @click="setFilter(filter)"
        >
          {{ getFilterLabel(filter) }}
        </button>
      </div>
    </div>

    <!-- 캘린더 그리드 -->
    <div class="calendar-grid">
      <!-- 요일 헤더 -->
      <div class="calendar-header-row">
        <div
          v-for="day in ['일', '월', '화', '수', '목', '금', '토']"
          :key="day"
          class="calendar-header-cell"
          :class="{ 'weekend': day === '일' || day === '토' }"
        >
          {{ day }}
        </div>
      </div>

      <!-- 날짜 그리드 -->
      <div
        v-for="(week, weekIndex) in weeks"
        :key="weekIndex"
        class="calendar-week"
      >
        <div class="calendar-row">
          <DateCell
            v-for="dayInfo in week"
            :key="dayInfo.date"
            :day-info="dayInfo"
            :is-selected="selectedDate?.getTime() === dayInfo.date?.getTime()"
            :is-in-range="selectionStart && dayInfo.date >= selectionStart && dayInfo.date <= (selectionEnd || selectionStart)"
            :schedules="filteredSchedules"
            @select-date="handleDateSelect"
            @range-select="handleDateRangeSelect"
            @dragstart="handleDragStart"
            @drop="handleDrop"
            @dragover="handleDragOver"
          />
        </div>

        <!-- 해당 주에 선택된 날짜가 있을 경우 상세 정보 표시 -->
        <DayDetail
          v-if="selectedDate && isInCurrentWeek(week, selectedDate)"
          :date="selectedDate"
          class="week-detail"
          @close="handleDetailClose"
          @refresh="handleRefresh"
        />
      </div>
    </div>

    <!-- 로딩 오버레이 -->
    <div v-if="isLoading" class="loading-overlay">
      <div class="loading-spinner"></div>
      <span class="loading-text">로딩 중...</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useWorkerStore } from '@/store/worker/workerStore';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { useMemberStore } from '@/store/member/memberStore';
import { formatDate } from '@/common/apis/utils/date';
import CalendarHeader from '@/views/Calendar/CalendarHeader.vue';
import DateCell from '@/views/Calendar/DateCell.vue';
import DayDetail from '@/views/Calendar/DayDetail.vue';

const memberStore = useMemberStore();
const store = useScheduleStore();
const currentDate = ref(new Date());
const selectedDate = ref(null);
const selectionStart = ref(null);
const selectionEnd = ref(null);
const draggedSchedule = ref(null);
const workerStore = useWorkerStore();
const selectedWorker = ref('all');
const scheduleFilter = ref('all');
const isLoading = ref(false);

const getFilterLabel = (filter) => {
  switch(filter) {
    case 'morning': return '오전';
    case 'afternoon': return '오후';
    default: return '전체';
  }
};

// Computed
const workers = computed(() => {
  return workerStore.workers || [];
});

const daysInMonth = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth();
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const days = [];

  // 이전 달의 날짜
  for (let i = 0; i < firstDay.getDay(); i++) {
    const prevDate = new Date(year, month, -i);
    days.unshift({ date: prevDate, isCurrentMonth: false });
  }

  // 현재 달의 날짜
  for (let i = 1; i <= lastDay.getDate(); i++) {
    days.push({
      date: new Date(year, month, i),
      isCurrentMonth: true
    });
  }

  // 다음 달의 날짜
  const remainingDays = 7 - (days.length % 7);
  if (remainingDays < 7) {
    for (let i = 1; i <= remainingDays; i++) {
      const nextDate = new Date(year, month + 1, i);
      days.push({ date: nextDate, isCurrentMonth: false });
    }
  }

  return days;
});

const weeks = computed(() => {
  const days = daysInMonth.value;
  const weeks = [];
  for (let i = 0; i < days.length; i += 7) {
    weeks.push(days.slice(i, i + 7));
  }
  return weeks;
});

const filteredSchedules = computed(() => {
  let schedules = store.schedules || [];

  // 직원 필터링
  if (selectedWorker.value && selectedWorker.value !== 'all') {
    schedules = schedules.filter(schedule => {
      const scheduleWorkerId = typeof schedule.workerId === 'string' ?
        parseInt(schedule.workerId) : schedule.workerId;
      const selectedId = typeof selectedWorker.value === 'string' ?
        parseInt(selectedWorker.value) : selectedWorker.value;
      return scheduleWorkerId === selectedId;
    });
  }

  // 시간대 필터링
  if (scheduleFilter.value !== 'all') {
    schedules = schedules.filter(schedule => {
      if (!schedule.startTime) return false;
      const hour = parseInt(schedule.startTime.split(':')[0]);
      return scheduleFilter.value === 'morning' ? hour < 12 : hour >= 12;
    });
  }

  return schedules;
});

// Methods
const isInCurrentWeek = (week, date) => {
  if (!date) return false;

  const selectedDateStr = formatDate(date);
  if (!selectedDateStr) return false;

  return week.some(dayInfo => {
    const dayDateStr = formatDate(dayInfo.date);
    return dayDateStr === selectedDateStr;
  });
};

const setFilter = (filter) => {
  scheduleFilter.value = filter;
};

const handleDateSelect = (date) => {
  if (!date) return;
  selectedDate.value = selectedDate.value?.getTime() === date.getTime() ? null : date;
};

const handleDetailClose = () => {
  selectedDate.value = null;
};

const handleDateRangeSelect = (date) => {
  if (!date) return;

  if (!selectionStart.value) {
    selectionStart.value = date;
  } else if (!selectionEnd.value) {
    if (date < selectionStart.value) {
      selectionEnd.value = selectionStart.value;
      selectionStart.value = date;
    } else {
      selectionEnd.value = date;
    }
  } else {
    selectionStart.value = date;
    selectionEnd.value = null;
  }
};

const handleDragStart = (schedule) => {
  draggedSchedule.value = schedule;
};

const handleDrop = async (targetDate) => {
  if (!draggedSchedule.value || !targetDate) return;

  try {
    // 드래그된 스케줄의 정보로 새 스케줄 등록
    const newSchedule = await store.addConfirmedSchedule({
      workerId: draggedSchedule.value.workerId,
      startTime: draggedSchedule.value.startTime,
      endTime: draggedSchedule.value.endTime,
      date: formatDate(targetDate)
    });

    // 기존 스케줄 삭제
    await store.deleteConfirmedSchedule(draggedSchedule.value.confirmedScheduleId);
    await refreshData();

  } catch (error) {
    console.error('스케줄 이동 실패:', error);
  } finally {
    draggedSchedule.value = null;
  }
};

const handleDragOver = (e) => {
  e.preventDefault();
};

const handleRefresh = async () => {
  await refreshData();
};

const refreshData = async () => {
  if (isLoading.value) return;

  try {
    isLoading.value = true;
    // 워커 정보를 먼저 가져온 후 다른 데이터를 가져오도록 수정
    await workerStore.fetchAllWorkers();
    if (memberStore.isWorker) {
      await workerStore.fetchWorkerInfo();
    }
    await Promise.all([
      store.fetchAllSchedules(),
      store.fetchSubstituteRequests()
    ]);
  } catch (error) {
    console.error('데이터 새로고침 실패:', error);
  } finally {
    isLoading.value = false;
  }
};

// Lifecycle
onMounted(async () => {
  await refreshData();
});
</script>

<style scoped>
.calendar {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 2px 4px rgba(66, 71, 105, 0.1);
}

.calendar-grid {
  border: 1px solid #E0E0E0;
  border-radius: 8px;
  overflow: hidden;
}

.calendar-header-row {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  background: #F5F5F5;
  border-bottom: 1px solid #E0E0E0;
}

.calendar-header-cell {
  padding: 0.75rem;
  text-align: center;
  font-weight: 600;
  color: #2D3250;
}

.calendar-header-cell.weekend {
  color: #424769;
}

.calendar-week {
  position: relative;
}

.calendar-row {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  border-bottom: 1px solid #E0E0E0;
}

.week-detail {
  position: relative;
  margin-top: -1px;
  background: white;
  border-top: 2px solid #2D3250;
  animation: slideDown 0.3s ease-out;
  z-index: 10;
}

.filter-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1.5rem;
  padding: 1rem;
  background: #f8fafc;
  border-radius: 8px;
  align-items: center;
}

.worker-filter {
  min-width: 140px;
  padding: 0.5rem 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background-color: white;
  color: #2D3250;
  font-size: 0.95rem;
  outline: none;
  transition: all 0.2s ease;
  cursor: pointer;
}

.worker-filter:hover {
  border-color: #424769;
}

.worker-filter:focus {
  border-color: #424769;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.1);
}

.time-filters {
  display: flex;
  gap: 0.5rem;
}

.filter-button {
  padding: 0.5rem 1.25rem;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background: white;
  color: #424769;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-button:hover {
  border-color: #2D3250;
  color: #2D3250;
}

.filter-button.active {
  background: #2D3250;
  color: white;
  border-color: #2D3250;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  z-index: 100;
  gap: 1rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #2D3250;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-text {
  color: #2D3250;
  font-size: 0.9rem;
  font-weight: 500;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .calendar {
    padding: 0.5rem;
  }

  .filter-controls {
    padding: 0.75rem;
    gap: 0.5rem;
  }

  .worker-filter {
    width: 100%;
    min-width: auto;
  }

  .time-filters {
    width: 100%;
    justify-content: space-between;
  }

  .filter-button {
    flex: 1;
    padding: 0.5rem;
    font-size: 0.875rem;
  }
}

@media (max-width: 480px) {
  .calendar {
    padding: 0.25rem;
  }

  .calendar-header-cell {
    padding: 0.5rem;
    font-size: 0.875rem;
  }
}
</style>
