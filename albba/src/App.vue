<template>
  <div id="app">
    <Header />
    <div class="main-container">
      <WeekDays />
      <div class="content">
        <Calendar />
        <Notification />
        <SalaryCalculator />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import Header from './components/Header.vue';
import WeekDays from './components/WeekDays.vue';
import Calendar from './components/Calendar.vue';
import Notification from './components/Notification.vue';
import SalaryCalculator from './components/SalaryCalculator.vue';
</script>

<style scoped>
#app {
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
}

.main-container {
  padding: 20px;
}

.content {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 20px;
}
</style>
