<template>
    <div class="day-tooltip">
      <div class="tooltip-header">근무 상세</div>
      <div class="tooltip-content">
        <div class="schedule-list">
          <template v-if="schedules.length > 0">
            <div v-for="schedule in schedules" 
                 :key="schedule.confirmedScheduleid"
                 class="schedule-item">
              {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
            </div>
          </template>
          <div v-else class="no-schedule">
            등록된 스케줄이 없습니다.
          </div>
        </div>
        <div class="tooltip-divider"></div>
        <div class="tooltip-row">
          <span>총 근무시간:</span>
          <span>{{ hours }}시간</span>
        </div>
        <div class="tooltip-row">
          <span>일급:</span>
          <span>{{ formatCurrency(dailyAmount) }}원</span>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
    schedules: {
      type: Array,
      default: () => []
    },
    hours: {
      type: Number,
      required: true
    },
    dailyAmount: {
      type: Number,
      required: true
    }
  })
  
  const formatTime = (time) => {
    return time.substring(0, 5)
  }
  
  const formatCurrency = (value) => {
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
  }
  </script>
  
  <style scoped>
  .day-tooltip {
  display: none;
  position: absolute;
  background: rgba(66, 71, 105, 0.95);  /* 이전 rgba(0, 0, 0, 0.8) */
  color: white;
  padding: 12px;
  border-radius: 6px;
  z-index: 1000;
  min-width: 220px;
  left: 50%;
  top: 100%;
  transform: translateX(-50%);
  margin-top: 8px;
  box-shadow: 0 4px 6px rgba(66, 71, 105, 0.2);  /* 그림자 색상 변경 */
}

.tooltip-header {
  font-weight: bold;
  margin-bottom: 8px;
  padding-bottom: 4px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.tooltip-content {
  max-height: 300px;
  overflow-y: auto;
  padding-right: 4px;
}

.schedule-list {
  margin-bottom: 8px;
}

.schedule-item {
  background-color: rgba(249, 177, 122, 0.2);  /* accent 컬러 활용 */
  padding: 6px 10px;
  border-radius: 4px;
  margin: 6px 0;
  font-size: 0.875rem;
}

.tooltip-divider {
  height: 1px;
  background-color: rgba(255, 255, 255, 0.2);
  margin: 8px 0;
}

.tooltip-row {
  display: flex;
  justify-content: space-between;
  margin: 4px 0;
}

.no-schedule {
  color: #FCDDBD;  /* accent-light 컬러 사용 */
  font-size: 0.875rem;
  text-align: center;
  padding: 8px;
}

.day-tooltip::before {
  content: '';
  position: absolute;
  top: -6px;
  left: 50%;
  transform: translateX(-50%);
  border-width: 0 6px 6px 6px;
  border-style: solid;
  border-color: transparent transparent rgba(66, 71, 105, 0.95) transparent;
}

.tooltip-content::-webkit-scrollbar {
  width: 4px;
}

.tooltip-content::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
}

.tooltip-content::-webkit-scrollbar-track {
  background-color: rgba(0, 0, 0, 0.1);
}
  </style>