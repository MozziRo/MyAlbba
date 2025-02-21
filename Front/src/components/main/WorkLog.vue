<template>
  <div class="worklog-widget">
    <!-- <h3 class="widget-title">근무 기록 일지</h3> -->
    <ul class="worklog-list">
      <li v-for="log in workLogs" :key="log.id">
        <span class="date">{{ log.date }}</span>
        <span class="shift">{{ log.shift }}</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const workLogs = ref([
  { id: 1, date: '2024-02-01', shift: '오전 9시 - 오후 6시' },
  { id: 2, date: '2024-02-02', shift: '오후 2시 - 오후 10시' },
  { id: 3, date: '2024-02-03', shift: '오전 9시 - 오후 6시' }
]);
</script>

<style scoped>
.widget-title {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.worklog-list {
  list-style: none;
  padding: 0;
}

.worklog-list li {
  padding: 8px;
  border-bottom: 1px solid #ddd;
}

.date {
  font-weight: bold;
  margin-right: 10px;
  color: #424769;  /* Primary color */
}

.shift {
  color: #676F9D;  /* Primary light */
}
</style>
