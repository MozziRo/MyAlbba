<template>
    <div class="calendar">
      <!-- 월 변경 -->
      <div class="month">
        <button @click="prevMonth">‹</button>
        <span>{{ currentMonth }}</span>
        <button @click="nextMonth">›</button>
      </div>
  
      <!-- 요일 헤더 -->
      <div class="weekdays">
        <div v-for="(weekday, index) in weekdays" :key="index" class="weekday">
          {{ weekday }}
        </div>
      </div>
  
      <!-- 일자 -->
      <div class="days">
        <div
          v-for="(day, index) in daysInMonth"
          :key="index"
          class="day"
          :class="{ today: isToday(day) }"
        >
          {{ day }}
        </div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, computed } from 'vue';
  import dayjs from 'dayjs';
  
  // 현재 날짜 상태 관리
  const currentDate = ref(dayjs());
  
  // 요일 데이터
  const weekdays = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
  
  // 현재 월 이름과 연도를 계산
  const currentMonth = computed(() => currentDate.value.format('MMMM YYYY'));
  
  // 해당 월의 일자 리스트 생성
  const daysInMonth = computed(() => {
    const days: number[] = [];
    const daysInCurrentMonth = currentDate.value.daysInMonth(); // 이번 달 총 일 수
    for (let day = 1; day <= daysInCurrentMonth; day++) {
      days.push(day);
    }
    return days;
  });
  
  // 오늘 날짜인지 확인하는 함수
  function isToday(day: number): boolean {
    return (
      day === dayjs().date() &&
      currentDate.value.month() === dayjs().month() &&
      currentDate.value.year() === dayjs().year()
    );
  }
  
  // 이전/다음 월로 변경하는 함수
  function prevMonth() {
    currentDate.value = currentDate.value.subtract(1, 'month');
  }
  
  function nextMonth() {
    currentDate.value = currentDate.value.add(1, 'month');
  }
  </script>
  
  <style scoped>
  /* 스타일링 */
  .calendar {
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
  }
  
  .month {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    font-size: 18px;
    margin-bottom: 10px;
  }
  
  .weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .weekday {
    text-align: center;
    color: #666;
  }
  
  .days {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    gap: 5px;
  }
  
  .day {
    text-align: center;
    padding: 10px;
    border-radius: 50%;
    background-color: #f0f0f0;
  }
  
  .day.today {
    background-color: #ffeeba;
    font-weight: bold;
  }
  </style>
  