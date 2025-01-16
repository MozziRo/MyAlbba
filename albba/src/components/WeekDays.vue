<template>
    <div class="weekdays">
      <div
        v-for="(day, index) in days"
        :key="index"
        class="day"
      >
        <div>{{ day.date }}</div>
        <div>{{ day.name }}</div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref } from 'vue';
  
  
  const days = ref([
    { date: '12', name: 'SUN' },
    { date: '13', name: 'MON' },
    { date: '14', name: 'TUE' },
    { date: '15', name: 'WED' },
    { date: '16', name: 'THU' },
    { date: '17', name: 'FRI' },
    { date: '18', name: 'SAT' },
  ]);
  </script>
  
  <style scoped>
  .weekdays {
    display: flex;
    justify-content: space-between;
    background-color: #fff;
    padding: 10px 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .day {
    text-align: center;
    font-size: 14px;
    font-weight: bold;
    color: #333;
  }
  </style>
  