<template>
  <div
    class="calendar-cell"
    :class="{
      'other-month': !dayInfo.isCurrentMonth,
      'is-selected': isSelected,
      'is-today': isTodayDate,
      'is-weekend': isWeekendDate,
      'is-in-range': isInRange
    }"
    @click="handleClick"
    @drop="handleDrop"
    @dragover="handleDragOver"
  >
    <div class="cell-content">
      <div class="cell-header">
        <span class="date-number" :class="{ 'today-circle': isTodayDate }">{{ dayInfo.date.getDate() }}</span>
        <Bell
          v-if="memberStore.isWorker && hasSubstituteRequests(dayInfo.date)"
          class="notification-icon"
          size="16"
        />
      </div>

      <!-- Visible Schedules -->
      <div
        v-for="schedule in visibleSchedules"
        :key="schedule.confirmedScheduleId"
        class="schedule-item"
        :class="parseInt(schedule.startTime) < 12 ? 'morning' : 'afternoon'"
        :draggable="memberStore.isOwner"
        @dragstart="memberStore.isOwner && handleDragStart(schedule, $event)"
      >
        <span class="schedule-name">{{ schedule.workerName }}</span>
        <span class="schedule-time">{{ formatTime(schedule.startTime) }}</span>
      </div>

      <!-- Expand/Collapse Button -->
      <button
        v-if="hasMoreSchedules"
        class="expand-button"
        @click="toggleExpanded"
        :aria-expanded="isExpanded"
      >
        <span class="count-badge">
          {{ isExpanded ? '접기' : `+${hiddenSchedulesCount}` }}
        </span>
      </button>

      <!-- Hidden Schedules -->
      <div v-if="isExpanded" class="expanded-schedules">
        <div
          v-for="schedule in hiddenSchedules"
          :key="schedule.confirmedScheduleId"
          class="schedule-item"
          :class="parseInt(schedule.startTime) < 12 ? 'morning' : 'afternoon'"
          :draggable="memberStore.isOwner"
          @dragstart="memberStore.isOwner && handleDragStart(schedule, $event)"
        >
          <span class="schedule-name">{{ schedule.workerName }}</span>
          <span class="schedule-time">{{ formatTime(schedule.startTime) }}</span>
        </div>
      </div>
    </div>

    <Popup
      v-if="daySchedules.length"
      :schedules="daySchedules"
      :date="dayInfo.date"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { formatDate, formatTime, isToday, isWeekend } from '@/common/apis/utils/date';
import { useMemberStore } from '@/store/member/memberStore';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { useWorkerStore } from '@/store/worker/workerStore';
import { Bell } from 'lucide-vue-next';
import Popup from './Popup.vue';

const VISIBLE_SCHEDULES_LIMIT = 3;
const isExpanded = ref(false);
const memberStore = useMemberStore();
const scheduleStore = useScheduleStore();
const workerStore = useWorkerStore();

const props = defineProps({
  dayInfo: {
    type: Object,
    required: true
  },
  isSelected: Boolean,
  isInRange: Boolean,
  schedules: {
    type: Array,
    required: true,
    default: () => []
  }
});

const emit = defineEmits(['select-date', 'range-select', 'dragstart', 'drop', 'dragover']);

// Computed
const daySchedules = computed(() => {
  const dateStr = formatDate(props.dayInfo.date);
  const filteredSchedules = props.schedules.filter(schedule => schedule.date === dateStr);

  // 시간순 정렬
  return filteredSchedules.sort((a, b) => {
    const timeA = a.startTime.split(':')[0];
    const timeB = b.startTime.split(':')[0];
    return parseInt(timeA) - parseInt(timeB);
  });
});

const hasSubstituteRequests = (date) => {
  const formattedDate = formatDate(date);
  return scheduleStore.substituteRequests.some(request =>
    request.date === formattedDate &&
    request.requesterId !== workerStore.worker?.workerId
  );
};

const visibleSchedules = computed(() => {
  return daySchedules.value.slice(0, VISIBLE_SCHEDULES_LIMIT);
});

const hiddenSchedules = computed(() => {
  return daySchedules.value.slice(VISIBLE_SCHEDULES_LIMIT);
});

const hiddenSchedulesCount = computed(() => {
  return daySchedules.value.length - VISIBLE_SCHEDULES_LIMIT;
});

const hasMoreSchedules = computed(() => {
  return daySchedules.value.length > VISIBLE_SCHEDULES_LIMIT;
});

const isTodayDate = computed(() => {
  return isToday(props.dayInfo.date);
});

const isWeekendDate = computed(() => {
  return isWeekend(props.dayInfo.date);
});

// Event Handlers
const toggleExpanded = () => {
  isExpanded.value = !isExpanded.value;
};

const handleDragStart = (schedule, event) => {
  event.dataTransfer.effectAllowed = 'move';
  emit('dragstart', schedule);
};

const handleDrop = (event) => {
  event.preventDefault();
  if (memberStore.isOwner) {
    emit('drop', props.dayInfo.date);
  }
};

const handleDragOver = (event) => {
  event.preventDefault();
  if (memberStore.isOwner) {
    emit('dragover', event);
  }
};

const handleClick = (event) => {
  // 확장 버튼 클릭시 이벤트 전파 중단
  if (event.target.closest('.expand-button')) {
    return;
  }
  emit(event.shiftKey ? 'range-select' : 'select-date', props.dayInfo.date);
};

onMounted(async () => {
  if (memberStore.isWorker && !workerStore.worker) {
    await workerStore.fetchWorkerInfo();
  }
});

</script>

<style scoped>
.calendar-cell {
  position: relative;
  min-height: 120px;
  background: white;
  cursor: pointer;
  border: 1px solid #E0E0E0;
  transition: all 0.2s ease;
  border-radius: 4px;
  overflow: hidden;
}

.cell-content {
  height: 100%;
  padding: 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.cell-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.25rem;
}

.date-number {
  font-size: 1rem;
  font-weight: 500;
  color: #2D3250;
  position: relative;
}

/* 오늘 날짜 표시를 동그라미로 변경 */
.date-number.today-circle {
  width: 28px;
  height: 28px;
  background-color: #ff9747;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 350;
}

.notification-icon {
  color: #ff1c1c;
  animation: gentle-swing 2s ease-in-out infinite;
  filter: drop-shadow(0 2px 4px rgba(249, 177, 122, 0.3));
}

.schedule-container {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

/* 오전/오후 스케줄 스타일 수정 */
.schedule-item {
  padding: 0.375rem 0.5rem;
  border-radius: 4px;
  font-size: 0.75rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.2s ease;
  margin: 2px 0;
}

.schedule-item.afternoon {
  background: rgba(144, 197, 238, 0.15);  /* 연한 네이비 */
  color: #424769;
  border-left: 2px solid #424769;
}

.schedule-item.morning {
  background: rgba(255, 137, 46, 0.12);  /* 연한 코랄 */
  color: #424769;
  border-left: 2px solid #F9B17A;
}

.schedule-item:hover {
  transform: translateX(2px);
}

.expand-button {
  width: 100%;
  padding: 0.25rem;
  background: rgba(66, 71, 105, 0.05);
  border: 1px dashed #424769;
  border-radius: 4px;
  color: #424769;
  font-size: 0.75rem;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-top: 0.25rem;
}

.expand-button:hover {
  background: rgba(66, 71, 105, 0.1);
}

.expanded-schedules {
  margin-top: 0.25rem;
  animation: slideDown 0.2s ease-out;
}

.other-month {
  background: #F8F9FA;
  color: #9E9E9E;
}

.is-selected {
  background: rgba(66, 71, 105, 0.05);
  border-color: #424769;
}

.is-today {
  border: 2px solid #424769;
}

.is-weekend {
  background: #F8F9FA;
}

.is-in-range {
  background: rgba(66, 71, 105, 0.08);
}

.schedule-name {
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 70%;
}

.schedule-time {
  font-size: 0.7rem;
  opacity: 0.8;
}

@keyframes gentle-swing {
  0%, 100% {
    transform: rotate(0deg);
    filter: drop-shadow(0 2px 4px rgba(249, 177, 122, 0.2));
  }
  25% {
    transform: rotate(8deg);
    filter: drop-shadow(2px 2px 4px rgba(249, 177, 122, 0.3));
  }
  75% {
    transform: rotate(-8deg);
    filter: drop-shadow(-2px 2px 4px rgba(249, 177, 122, 0.3));
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .calendar-cell {
    min-height: 100px;
  }

  .cell-content {
    padding: 0.5rem;
  }

  .date-number {
    font-size: 0.875rem;
  }

  .date-number.today-circle {
    width: 24px;
    height: 24px;
    font-size: 0.875rem;
  }

  .schedule-item {
    padding: 0.25rem;
    font-size: 0.7rem;
  }
}

@media (max-width: 480px) {
  .calendar-cell {
    min-height: 80px;
  }

  .date-number.today-circle {
    width: 22px;
    height: 22px;
    font-size: 0.8rem;
  }

  .schedule-name {
    max-width: 60%;
  }
}
</style>