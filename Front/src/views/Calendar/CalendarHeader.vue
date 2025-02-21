<script setup>
import { ChevronLeft, ChevronRight } from 'lucide-vue-next';
import { computed } from 'vue';
import { formatDetailDate } from '@/common/apis/utils/date';

const props = defineProps({
  currentDate: {
    type: Date,
    required: true
  }
});

const emit = defineEmits(['prev-month', 'next-month']);

const formattedYearMonth = computed(() => {
  return `${props.currentDate.getFullYear()}년 ${props.currentDate.getMonth() + 1}월`;
});
</script>

<template>
  <div class="calendar-header">
    <div class="header-wrapper">
      <div class="header-container">
        <button
          class="nav-button group"
          @click="emit('prev-month')"
          aria-label="이전 달"
        >
          <ChevronLeft size="20" class="group-hover:stroke-2"/>
        </button>

        <h2 class="month-text">
          {{ formattedYearMonth }}
        </h2>

        <button
          class="nav-button group"
          @click="emit('next-month')"
          aria-label="다음 달"
        >
          <ChevronRight size="20" class="group-hover:stroke-2"/>
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.calendar-header {
  margin-bottom: 1.5rem;
}

.header-wrapper {
  background: linear-gradient(to bottom, #ffffff, #F5F5F5);
  border: 1px solid #E0E0E0;
  border-radius: 12px;
  padding: 0.5rem;
  box-shadow: 0 1px 3px rgba(45, 50, 80, 0.05);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0.75rem 0.5rem;
  background-color: white;
  border-radius: 8px;
}

.month-text {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2D3250;
  padding: 0 1rem;
  flex-grow: 0;
  min-width: 160px;
  text-align: center;
  letter-spacing: -0.025em;
}

.nav-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 8px;
  color: #424769;
  background-color: white;
  transition: all 0.2s ease;
  cursor: pointer;
  border: 1px solid transparent;
  padding: 0;
}

.nav-button:hover {
  background-color: white;
  border-color: #424769;
  color: #424769;
}

.nav-button:focus {
  outline: none;
  border-color: #424769;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.1);
}

.nav-button:active {
  background-color: #2D3250;
  color: white;
  transform: scale(0.98);
}

@media (max-width: 640px) {
  .month-text {
    font-size: 1.125rem;
    min-width: 140px;
  }

  .nav-button {
    width: 28px;
    height: 28px;
  }
}
</style>
