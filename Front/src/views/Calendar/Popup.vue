<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import { formatDate, formatTime } from '@/common/apis/utils/date';

const props = defineProps({
  schedules: {
    type: Array,
    required: true
  },
  date: {
    type: Date,
    required: true
  }
});

const showPopup = ref(false);
let hideTimeout = null;

// 시간대별 스케줄 수 계산
const summary = computed(() => {
  const morning = props.schedules.filter(s => {
    const hour = parseInt(s.startTime.split(':')[0]);
    return hour < 12;
  }).length;

  const afternoon = props.schedules.filter(s => {
    const hour = parseInt(s.startTime.split(':')[0]);
    return hour >= 12;
  }).length;

  return { morning, afternoon };
});

// 스케줄 정렬
const sortedSchedules = computed(() => {
  return {
    morning: props.schedules
      .filter(s => parseInt(s.startTime.split(':')[0]) < 12)
      .sort((a, b) => {
        const timeA = a.startTime.split(':').map(Number);
        const timeB = b.startTime.split(':').map(Number);
        if (timeA[0] !== timeB[0]) return timeA[0] - timeB[0];
        return timeA[1] - timeB[1];
      }),
    afternoon: props.schedules
      .filter(s => parseInt(s.startTime.split(':')[0]) >= 12)
      .sort((a, b) => {
        const timeA = a.startTime.split(':').map(Number);
        const timeB = b.startTime.split(':').map(Number);
        if (timeA[0] !== timeB[0]) return timeA[0] - timeB[0];
        return timeA[1] - timeB[1];
      })
  };
});

const handleMouseEnter = () => {
  if (hideTimeout) {
    clearTimeout(hideTimeout);
    hideTimeout = null;
  }
  showPopup.value = true;
};

const handleMouseLeave = () => {
  hideTimeout = setTimeout(() => {
    showPopup.value = false;
  }, 300);
};

onMounted(() => {
  const parent = document.querySelector('.calendar-cell');
  if (parent) {
    parent.addEventListener('mouseenter', handleMouseEnter);
    parent.addEventListener('mouseleave', handleMouseLeave);
  }
});

onUnmounted(() => {
  const parent = document.querySelector('.calendar-cell');
  if (parent) {
    parent.removeEventListener('mouseenter', handleMouseEnter);
    parent.removeEventListener('mouseleave', handleMouseLeave);
  }
  if (hideTimeout) {
    clearTimeout(hideTimeout);
  }
});

// Props 변경 시 팝업 상태 초기화
watch(() => props.date, () => {
  showPopup.value = false;
});
</script>

<template>
  <div v-if="showPopup" class="schedule-popup">
    <div class="popup-header">
      {{ formatDate(date) }} 스케줄
    </div>

    <div class="popup-content">
      <!-- 오전 스케줄 -->
      <div v-if="summary.morning > 0" class="popup-section">
        <div class="popup-section-title">오전</div>
              <div
        v-for="schedule in sortedSchedules.morning"
        :key="schedule.confirmedScheduleId"
        class="popup-schedule morning"
      >
        <span class="schedule-name">{{ schedule.workerName }}</span>
        <span class="schedule-time">
          {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
        </span>
      </div>
      </div>

      <!-- 오후 스케줄 -->
      <div v-if="summary.afternoon > 0" class="popup-section">
        <div class="popup-section-title">오후</div>
              <div
        v-for="schedule in sortedSchedules.afternoon"
        :key="schedule.confirmedScheduleId"
        class="popup-schedule afternoon"
      >
        <span class="schedule-name">{{ schedule.workerName }}</span>
        <span class="schedule-time">
          {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
        </span>
      </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.schedule-popup {
  position: absolute;
  z-index: 20;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 0.75rem;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  min-width: 200px;
  left: 100%;
  top: 0;
  animation: fadeIn 0.2s ease-out;
}

.popup-header {
  font-weight: 600;
  padding-bottom: 0.5rem;
  margin-bottom: 0.5rem;
  border-bottom: 1px solid #e2e8f0;
  color: #07702A;
}

.popup-section {
  margin-bottom: 0.75rem;
}

.popup-section-title {
  font-weight: 500;
  color: #4a5568;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
}

.popup-schedule {
  display: flex;
  justify-content: space-between;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  margin-bottom: 0.25rem;
  font-size: 0.75rem;
  transition: transform 0.2s ease;
}

.popup-schedule:hover {
  transform: translateX(2px);
}

.popup-schedule.morning {
  background: #E9EFE6;
  color: #07702A;
}

.popup-schedule.afternoon {
  background: #FDF5E6;
  color: #07702A;
}

.schedule-name {
  font-weight: 500;
  margin-right: 1rem;
}

.schedule-time {
  color: #4a5568;
  font-size: 0.7rem;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateX(-10px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@media (max-width: 768px) {
  .schedule-popup {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 90%;
    max-width: 300px;
  }
}
</style>
