<template>
  <div class="salary-container">
    <!-- 이번 달 급여 -->
    <div class="salary-header">
      <h2>이번 달 급여</h2>
      <p class="salary-amount">{{ formattedTotal }}원</p>
    </div>

    <!-- 달력 -->
    <div class="salary-calendar">
      <div class="calendar-header">
        <button @click="prevMonth" class="month-btn">&lt;</button>
        <h3 style="color: black;">{{ currentDate.toLocaleString('ko-KR', { year: 'numeric', month: 'long' }) }}</h3>
        <button @click="nextMonth" class="month-btn">&gt;</button>
      </div>

      <div class="calendar-weekdays">
        <div v-for="day in ['일', '월', '화', '수', '목', '금', '토']" :key="day" class="weekday">
          {{ day }}
        </div>
      </div>

      <div class="calendar-grid">
        <div
          v-for="(date, index) in daysInMonth"
          :key="index"
          class="calendar-day"
        >
          <span class="date">{{ date.getDate() }}</span>

          <!-- 급여 정보 표시 -->
          <div v-if="salaryData[formatDate(date)]" class="salary-info">
            <span class="salary-value">{{ salaryData[formatDate(date)].amount }}원</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";

// 현재 날짜
const currentDate = ref(new Date());

// 이번 달 급여 정보 (샘플 데이터)
const salaryData = {
  "2025-01-28": { amount: "60,000" },
  "2025-01-30": { amount: "62,867" },
  "2025-01-29": { amount: "72,867" },
  "2025-02-10": { amount: "52,867" },
  "2025-02-12": { amount: "122,867" },
  "2025-02-14": { amount: "122,867" },
};

// ✅ 이번 달 총 급여 계산
const formattedTotal = computed(() => {
  let total = 0;
  Object.values(salaryData).forEach((data) => {
    total += parseInt(data.amount.replace(",", ""), 10);
  });
  return total.toLocaleString("ko-KR");
});

// ✅ 달력 날짜 계산
const daysInMonth = computed(() => {
  const year = currentDate.value.getFullYear();
  const month = currentDate.value.getMonth();
  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);
  const days = [];

  // 빈 칸 채우기
  for (let i = 0; i < firstDay.getDay(); i++) {
    days.unshift(new Date(year, month, -i));
  }

  // 현재 달의 날짜들
  for (let i = 1; i <= lastDay.getDate(); i++) {
    days.push(new Date(year, month, i));
  }

  return days;
});

// ✅ 날짜 포맷 변환
const formatDate = (date) => {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, "0")}-${String(date.getDate()).padStart(2, "0")}`;
};

// ✅ 이전 달 이동
const prevMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1);
};

// ✅ 다음 달 이동
const nextMonth = () => {
  currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1);
};
</script>

<style scoped>
/* ✅ 컨테이너 스타일 */
.salary-container {
  width: 100%;
  max-width: 700px;
  max-height: 430px;
  margin: 0 auto;
  text-align: center;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* ✅ 급여 헤더 */
.salary-header {
  margin-bottom: 1rem;
}

.salary-header h2 {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

.salary-amount {
  font-size: 2rem;
  font-weight: bold;
  color: #007a5e;
}

/* ✅ 달력 헤더 */
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding: 0.5rem;
  background: #f8f9fa;
  border-radius: 5px;
}

.month-btn {
  background: none;
  border: none;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-size: 1.2rem;
  color: #007a5e;
}

.month-btn:hover {
  background-color: #e0f2f1;
  border-radius: 5px;
}

/* ✅ 요일 스타일 */
.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 5px;
  padding: 0.5rem;
  font-weight: bold;
  color: #666;
  background: #f8f9fa;
  border-radius: 5px;
}

/* ✅ 달력 스타일 */
.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 1px;
  background: #f5f5f5;
  margin-top: 1px;
}

.calendar-day {
  background: white;
  min-height: 80px;
  padding: 0.5rem;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.calendar-day:hover {
  background: #e0f2f1;
}

/* ✅ 날짜 */
.date {
  font-size: 1rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

/* ✅ 급여 정보 */
.salary-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #e0f8ea;
  border-radius: 8px;
  padding: 0.5rem;
  width: 80%;
}

.hours {
  font-size: 0.85rem;
  color: #007a5e;
  font-weight: bold;
}

.salary-value {
  font-size: 1rem;
  font-weight: bold;
  color: #007a5e;
}

/* ✅ 반응형 스타일 */
@media (max-width: 768px) {
  .calendar-day {
    min-height: 60px;
  }

  .salary-container {
    padding: 1rem;
  }

  .salary-amount {
    font-size: 1.5rem;
  }

  .salary-info {
    width: 90%;
  }
}
</style>
