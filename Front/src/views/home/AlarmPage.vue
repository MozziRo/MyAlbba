<template>
  <div class="alarm-container">
    <h3 class="alarm-title">오늘의 인수인계</h3>
    <div class="alarm-list">
      <div v-for="(alarm, index) in alarms" :key="index" class="alarm-card">
        <div class="alarm-header">
          <span class="alarm-user">{{ alarm.user }}</span>
          <span class="alarm-time">{{ alarm.time }}</span>
        </div>
        <p class="alarm-message">{{ alarm.message }}</p>
        <span class="alarm-status">{{ alarm.status }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

// 샘플 데이터
const alarms = ref([
  { user: "김주원", time: "2월 18일 오전 10:19", message: "커피 원두가 떨어졌어요, 주문 넣어야 됩니다.", status: "진행중" },
  { user: "권신범", time: "2월 20일 오전 10:18", message: "컵 닦을때 조심히 닦아주세요 쉽게 깨져요.", status: "진행중" },
]);
</script>

<style scoped>
.alarm-container {
  width: 90%;
  max-width: 450px;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.alarm-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #333;
}

.alarm-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.alarm-card {
  background: #f9f9f9;
  padding: 12px;
  border-radius: 8px;
  text-align: left;
}

.alarm-header {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 4px;
}

.alarm-user {
  font-weight: bold;
  color: #333;
}

.alarm-message {
  font-size: 1rem;
  color: #444;
  margin-bottom: 6px;
}

.alarm-status {
  font-size: 0.85rem;
  color: #00b386;
  font-weight: bold;
}
</style>
