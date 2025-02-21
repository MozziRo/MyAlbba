<template>
  <div class="salary-info">
    {{ formatCurrency(monthlySalary) }}원
    <span class="info-icon">
      <i class="fas fa-info-circle"></i>
    </span>
    <div class="salary-tooltip">
      <div class="tooltip-content">
        <div class="tooltip-row">
          <span class="tooltip-label">시급</span>
          <span class="tooltip-value">{{ formatCurrency(hourlyWage) }}원</span>
        </div>
        <div class="tooltip-row">
          <span class="tooltip-label">총 근무시간</span>
          <span class="tooltip-value">{{ totalHours }}시간</span>
        </div>
        <div class="tooltip-row">
          <span class="tooltip-label">세전 급여</span>
          <span class="tooltip-value">{{ formatCurrency(Math.floor(monthlySalary * 1.033)) }}원</span>
        </div>
        <div class="tooltip-row">
          <span class="tooltip-label">세금 (3.3%)</span>
          <span class="tooltip-value">{{ formatCurrency(Math.floor(monthlySalary * 0.033)) }}원</span>
        </div>
        <div class="tooltip-row">
          <span class="tooltip-label">실수령액</span>
          <span class="tooltip-value">{{ formatCurrency(monthlySalary) }}원</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  monthlySalary: {
    type: Number,
    required: true
  },
  totalHours: {
    type: Number,
    required: true
  },
  hourlyWage: {
    type: Number,
    default: 10030
  }
})

const formatCurrency = (value) => {
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
}
</script>

<style scoped>
.salary-info {
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
}

.info-icon {
  color: #424769;  /* 이전: #4a5568 */
  font-size: 0.875rem;
  cursor: pointer;
}

.salary-tooltip {
  display: none;
  position: absolute;
  background: rgba(66, 71, 105, 0.95);  /* 이전: rgba(0, 0, 0, 0.8) */
  color: white;
  padding: 12px;
  border-radius: 6px;
  font-size: 14px;
  z-index: 1000;
  min-width: 250px;
  right: 0;
  top: calc(100% + 8px);
  transform-origin: top right;
  max-width: calc(100vw - 48px);
  box-shadow: 0 4px 6px rgba(66, 71, 105, 0.2);  /* 이전: rgba(0, 0, 0, 0.1) */
}

.tooltip-content {
  line-height: 1.5;
}

.tooltip-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin: 4px 0;
  white-space: nowrap;
}

.tooltip-label {
  color: #FCDDBD;  /* 이전: #e2e8f0 */
}

.tooltip-value {
  color: white;
  font-weight: 500;
}

.salary-info:hover .salary-tooltip {
  display: block;
}

@media (max-width: 768px) {
  .salary-tooltip {
    left: auto;
    right: 0;
  }
}
</style> 