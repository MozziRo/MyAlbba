<script setup>
import { ref, computed } from 'vue'

const currentDate = ref(new Date())
const selectedDate = ref(null)

// 샘플 스케줄 데이터
const schedules = {
 "2025-01-01": [
   { time: "09:00-15:00", type: "주간", name: "김주원" },
   { time: "15:00-21:00", type: "야간", name: "김동빈" }
 ],
 "2025-01-02": [
   { time: "10:00-16:00", type: "주간", name: "김유정" },
   { time: "16:00-22:00", type: "야간", name: "권신범" }
 ],
 "2025-01-03": [
   { time: "09:00-15:00", type: "주간", name: "김유정" },
   { time: "15:00-21:00", type: "야간", name: "김주원" }
 ],
 "2025-01-06": [
   { time: "10:00-16:00", type: "주간", name: "김동빈" },
   { time: "16:00-22:00", type: "야간", name: "김유정" }
 ],
 "2025-01-07": [
   { time: "09:00-15:00", type: "주간", name: "권신범" },
   { time: "15:00-21:00", type: "야간", name: "서원준" }
 ],
 "2025-01-10": [
   { time: "10:00-16:00", type: "주간", name: "김주원" },
   { time: "16:00-22:00", type: "야간", name: "김동빈" }
 ],
 "2025-01-13": [
   { time: "09:00-15:00", type: "주간", name: "김유정" },
   { time: "15:00-21:00", type: "야간", name: "권신범" }
 ],
 "2025-01-15": [
   { time: "10:00-16:00", type: "주간", name: "서원준" },
   { time: "16:00-22:00", type: "야간", name: "김주원" }
 ],
 "2025-01-17": [
   { time: "09:00-15:00", type: "주간", name: "김동빈" },
   { time: "15:00-21:00", type: "야간", name: "김유정" }
 ],
 "2025-01-20": [
   { time: "10:00-16:00", type: "주간", name: "권신범" },
   { time: "16:00-22:00", type: "야간", name: "서원준" }
 ],
 "2025-01-22": [
   { time: "09:00-15:00", type: "주간", name: "김주원" },
   { time: "15:00-21:00", type: "야간", name: "권신범" }
 ],
 "2025-01-24": [
   { time: "10:00-16:00", type: "주간", name: "김유정" },
   { time: "16:00-22:00", type: "야간", name: "김동빈" }
 ],
 "2025-01-27": [
   { time: "09:00-15:00", type: "주간", name: "서원준" },
   { time: "15:00-21:00", type: "야간", name: "권신범" }
 ],
 "2025-01-29": [
   { time: "10:00-16:00", type: "주간", name: "김동빈" },
   { time: "16:00-22:00", type: "야간", name: "김유정" }
 ],
 "2025-01-31": [
   { time: "09:00-15:00", type: "주간", name: "권신범" },
   { time: "15:00-21:00", type: "야간", name: "서원준" }
 ]
}

const daysInMonth = computed(() => {
 const year = currentDate.value.getFullYear()
 const month = currentDate.value.getMonth()
 const firstDay = new Date(year, month, 1)
 const lastDay = new Date(year, month + 1, 0)
 const days = []

 // 이전 달의 날짜들을 채움
 for (let i = 0; i < firstDay.getDay(); i++) {
   const prevDate = new Date(year, month, -i)
   days.unshift(prevDate)
 }

 // 현재 달의 날짜들
 for (let i = 1; i <= lastDay.getDate(); i++) {
   days.push(new Date(year, month, i))
 }

 return days
})

const formatDate = (date) => {
 return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
}

const prevMonth = () => {
 currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() - 1)
}

const nextMonth = () => {
 currentDate.value = new Date(currentDate.value.getFullYear(), currentDate.value.getMonth() + 1)
}

const selectDate = (date) => {
 selectedDate.value = formatDate(date)
}
</script>

<template>
 <div class="calendar">
   <!-- 달력 헤더 -->
   <div class="calendar-header">
     <button @click="prevMonth" class="month-btn">&lt;</button>
     <h3 style="color: black;">{{ currentDate.toLocaleString('ko-KR', { year: 'numeric', month: 'long' }) }}</h3>
     <button @click="nextMonth" class="month-btn">&gt;</button>
   </div>

   <!-- 요일 표시 -->
   <div class="calendar-weekdays">
     <div v-for="day in ['일', '월', '화', '수', '목', '금', '토']" :key="day" class="weekday">
       {{ day }}
     </div>
   </div>

   <!-- 날짜 그리드 -->
   <div class="calendar-grid">
     <div
       v-for="(date, index) in daysInMonth"
       :key="index"
       class="calendar-day"
       :class="{
         'other-month': date.getMonth() !== currentDate.getMonth(),
         'selected': formatDate(date) === selectedDate
       }"
       @click="selectDate(date)"
     >


       <span class="date">{{ date.getDate() }}</span>


       <!-- 스케줄 표시 -->
       <div class="schedules" v-if="schedules[formatDate(date)]">
         <div
           v-for="(schedule, idx) in schedules[formatDate(date)]"
           :key="idx"
           class="schedule"
           :class="schedule.type"
         >
           <span class="schedule-name">{{ schedule.name }}</span>
           <span class="schedule-time">{{ schedule.time }}</span>
         </div>
       </div>
     </div>
   </div>

   <!-- 선택된 날짜 상세 정보 -->
   <div v-if="selectedDate && schedules[selectedDate]" class="schedule-details">
     <h4>{{ selectedDate }} 스케줄</h4>
     <div
       v-for="(schedule, idx) in schedules[selectedDate]"
       :key="idx"
       class="schedule-item"
     >
       <span class="schedule-type" :class="schedule.type">{{ schedule.type }}</span>
       <span class="schedule-name">{{ schedule.name }}</span>
       <span class="schedule-time">{{ schedule.time }}</span>
     </div>
   </div>
 </div>
</template>

<style scoped>
.calendar {
 background: rgb(255, 255, 255);
 border-radius: 10px;
 padding: 1rem;
 box-shadow: 0 2px 4px rgba(236, 232, 232, 0.1);
 max-height: 400px;
}

.calendar-header {
 display: flex;
 justify-content: space-between;
 align-items: center;
 margin-bottom: 1rem;
 padding: 0.5rem;
}

.month-btn {
 background: none;
 border: none;
 padding: 0.5rem 1rem;
 cursor: pointer;
 font-size: 1.2rem;
 color: #4285f4;
}

.month-btn:hover {
 background-color: #f0f0f0;
 border-radius: 5px;
}

.calendar-weekdays {
 display: grid;
 grid-template-columns: repeat(7, 1fr);
 gap: 1px;
 background: #f5f5f5;
 padding: 0.5rem;
 border-radius: 5px;
}

.weekday {
 text-align: center;
 font-weight: bold;
 color: #666;
}

.calendar-grid {
 display: grid;
 grid-template-columns: repeat(7, 1fr);
 gap: 1px;
 background: #f5f5f5;
 margin-top: 1px;
}

.calendar-day {
 background: white;
 min-height: 100px;
 padding: 0.5rem;
 cursor: pointer;
 display: flex;
 flex-direction: column;
}

.calendar-day:hover {
 background: #f8f9fa;
}

.calendar-day.other-month {
 background: #f8f9fa;
 color: #aaa;
}

.calendar-day.selected {
 background: #e8f0fe;
}

.date {
 font-size: 0.9rem;
 margin-bottom: 0.5rem;
}

.schedules {
 display: flex;
 flex-direction: column;
 gap: 0.25rem;
}

.schedule {
 font-size: 0.7rem;
 padding: 0.4rem;
 border-radius: 3px;
 margin-bottom: 0.25rem;
 display: flex;
 flex-direction: column;
}

.schedule-name {
 font-weight: 600;
 margin-bottom: 0.1rem;
}

.schedule-time {
 font-size: 0.65rem;
 opacity: 0.9;
}

.schedule.주간 {
 background-color: #e3f2fd;
 color: #1976d2;
}

.schedule.야간 {
 background-color: #f3e5f5;
 color: #7b1fa2;
}

.schedule-details {
 margin-top: 1.5rem;
 padding: 1.5rem;
 background: #f8f9fa;
 border-radius: 5px;
}

.schedule-item {
 display: flex;
 align-items: center;
 gap: 1rem;
 margin-bottom: 0.8rem;
 padding: 0.5rem;
 background: white;
 border-radius: 4px;
}

.schedule-type {
 padding: 0.25rem 0.5rem;
 border-radius: 3px;
 font-size: 0.8rem;
}

.schedule-type.주간 {
 background-color: #e3f2fd;
 color: #1976d2;
}

.schedule-type.야간 {
 background-color: #f3e5f5;
 color: #7b1fa2;
}

.schedule-item .schedule-name {
 font-size: 0.9rem;
 font-weight: 600;
 color: #333;
}

.schedule-item .schedule-time {
 color: #666;
 font-size: 0.85rem;
}

@media (max-width: 768px) {
 .calendar-day {
   min-height: 100px;
 }

 .schedule {
   font-size: 0.65rem;
   padding: 0.3rem;
 }

 .schedule-time {
   font-size: 0.6rem;
 }
}
</style>
