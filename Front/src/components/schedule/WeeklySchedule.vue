<script setup>
import { ref, onMounted } from 'vue';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { useMemberStore } from '@/store/member/memberStore';
import { storeToRefs } from 'pinia';
import { formatDate } from '@/common/apis/utils/date';

const memberStore = useMemberStore();
const { isOwner } = storeToRefs(memberStore);

const scheduleStore = useScheduleStore();
const { schedules } = storeToRefs(scheduleStore);
const { mySchedules } = storeToRefs(scheduleStore);

// 현재 주의 날짜 계산
const getCurrentWeekDates = () => {
  const now = new Date();  // 실제 현재 날짜 사용
  const currentDay = now.getDay();
  const diff = now.getDate() - currentDay;
  
  const weekStart = new Date(now.setDate(diff));
  const weekDates = [];
  
  for (let i = 0; i < 7; i++) {
    const day = new Date(weekStart);
    day.setDate(weekStart.getDate() + i);
    weekDates.push(day);
  }
  
  return weekDates;
};

const weekDates = ref(getCurrentWeekDates());

const getSchedulesForDate = (date) => {
  const formattedDate = formatDate(date);
  if(isOwner.value) {
      return schedules.value.filter(schedule => schedule.date === formattedDate);
  } 
  
  return mySchedules.value.filter(schedule => schedule.date === formattedDate);
};

onMounted(async () => {
  await scheduleStore.fetchAllSchedules();
  if(!isOwner.value) { 
    await scheduleStore.fetchMySchedules(); 
  }
});

// Date 형식 변환
const formatTime = function(time) {
  if (!time) return '';
  return time.slice(0, 5);
}

// 현재 근무하고 있는 알바생 확인
const isCurrentShift = (schedule, date) => {
  if (!schedule || !schedule.startTime || !schedule.endTime) return false;

  const now = new Date();
  const scheduleDate = new Date(date);
  
  // startTime과 endTime을 해당 날짜의 시간으로 설정
  const [startHour, startMinute] = schedule.startTime.split(':').map(Number);
  const [endHour, endMinute] = schedule.endTime.split(':').map(Number);

  const startDateTime = new Date(scheduleDate.setHours(startHour, startMinute, 0));
  const endDateTime = new Date(scheduleDate.setHours(endHour, endMinute, 0));

  return now >= startDateTime && now <= endDateTime;
};

// 현재 날짜 확인
const isToday = (date) => {
  const today = new Date();
  return (
    date.getDate() === today.getDate() &&
    date.getMonth() === today.getMonth() &&
    date.getFullYear() === today.getFullYear()
  );
};

</script>

<template>
  <div class="weeklySchedule">
    <h3 class="title">
      <v-icon
          color="#666"
          size="24"
        >
        mdi-calendar-today
        </v-icon>
      이번주 스케줄</h3>
    <div class="schedule-container">
      <table class="schedule-table">
        <thead>
          <tr>
            <th v-for="(date, index) in weekDates" 
                :key="index" 
                :class="{ 
                  'today-header': isToday(date), 
                  'date-header' : !isToday(date)
                  }">
              <div class="day">
                {{ ['일', '월', '화', '수', '목', '금', '토'][date.getDay()] }}
              </div>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td v-for="(date, dateIndex) in weekDates" 
                :key="dateIndex" 
                class="schedule-cell">
              <div v-for="schedule in getSchedulesForDate(date)"
                  :key="schedule.id"
                  :class="{
                    'current-shift': isCurrentShift(schedule, date),
                    'not-current-shift': !isCurrentShift(schedule, date)
                  }">
                <template v-if="isOwner">
                  <div class="employee-name"> 
                    <v-icon
                      color="#2D3250"
                      size="12"
                    >
                    mdi-check-circle
                    </v-icon>
                  {{ schedule.workerName }}</div>
                </template>
                <template v-else>
                  <div class="schedule-time">
                    <v-icon
                      color="#424769"
                      size="12"
                    >
                    mdi-check-circle
                    </v-icon>
                    {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}</div>
                </template>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style scoped>
.weeklySchedule {
  padding: 0 1.5rem;
  margin-top: 1rem;
}

.schedule-container {
 width: 100%;
 min-width: 320px;
 background: white;
 border-radius: 30px; /* 맞춤 */
 padding: 1px;
 overflow: hidden;
 border: 1px solid #E0E0E0;
 box-shadow: 0 5px 10px rgba(45, 50, 80, 0.2);
}

.title {
 color: #2D3250;
 padding: 1rem;
}

.schedule-table {
 width: 100%;
 table-layout: fixed;
 border-collapse: separate;
 border-spacing: 0;
 border-radius: 12px;
}

.date-header {
 padding: 16px 8px;
 text-align: center;
 position: relative;
 width: 14.28%;
}

.date-header .day {
 display: inline-block;
 width: 40px;
 height: 40px;
 line-height: 40px;
 border-radius: 50%;
 color: #424769;
}

.today-header .day {
 display: inline-block;
 background-color: #F9B17A;
 width: 40px;
 height: 40px;
 line-height: 40px;
 border-radius: 50%;
 color: #2D3250;
}

.date {
 font-weight: medium;
 margin-bottom: 6px;
 color: white;
 font-size: clamp(0.875rem, 2vw, 1rem);
}

.day {
 font-size: clamp(0.75rem, 1.5vw, 0.875rem);
 color: #424769;
 font-weight: 500;
}

.schedule-cell {
 padding: 8px;
 padding-bottom: 40px;
 vertical-align: top;
}

.employee-name {
 padding-left: 0.5rem;
 font-weight: 600;
 color: #424769;
 white-space: nowrap;
 overflow: hidden;
 text-overflow: ellipsis;
}

.schedule-time {
 padding-left: 0.5rem;
 font-weight: 600;
 color: #424769;
 white-space: nowrap;
 overflow: hidden;
 text-overflow: ellipsis;
}

.shift-type {
 margin-bottom: 2px;
 font-weight: 500;
}

.current-shift {
 font-size: clamp(0.65rem, 1.2vw, 0.75rem);
 border: 3px dashed #F9B17A;
 font-weight: bold;
 border-radius: 10px;
 padding: 5px;
 padding: 6px;
 margin-bottom: 4px;
}

.not-current-shift {
 font-size: clamp(0.65rem, 1.2vw, 0.75rem);
 border: 3px dashed #E0E0E0;
 border-radius: 10px;
 padding: 5px;
 padding: 6px;
 margin-bottom: 4px;
}

@media (max-width: 640px) {
 .schedule-container {
   border-radius: 8px;
   font-size: 14px;
 }
 
 .date-header {
   padding: 8px 4px;
 }
 
 .schedule-cell {
   padding: 4px;
 }
 
 .schedule-item {
   padding: 6px;
   margin-bottom: 4px;
 }
 
 .employee-name, .shift-type {
   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
 }
 
 .schedule-time {
   white-space: nowrap;
   overflow: hidden;
   text-overflow: ellipsis;
 }
}

@media (max-width: 480px) {
 .schedule-table {
   font-size: 12px;
 }
 
 .date-header {
   padding: 4px 2px;
 }
 
 .schedule-cell {
   height: 120px;
 }
 
 .schedule-item {
   padding: 4px;
 }
}
</style>