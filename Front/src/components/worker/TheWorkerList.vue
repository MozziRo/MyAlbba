<template>
  <div class="worker-list">
    <h2>알바생 목록</h2>
    <ul>
      <li v-for="worker in workers" :key="worker.id" class="worker-item">
        <input type="checkbox" :value="worker.id" v-model="selectedWorkers" />
        <span class="worker-name">{{ worker.name }}</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { defineEmits, ref, watch } from 'vue';

// 부모 컴포넌트에서 props로 받아올 데이터의 타입 정의
const props = defineProps({
  workers: {
    type: Array,
    required: true,
  },
});

// emit 정의
const emit = defineEmits(['update:selectedWorkers'])


const selectedWorkers = ref([]); // 선택된 알바생 ID 저장

// 선택된 알바생이 변경될 때마다 부모에게 전달
watch(selectedWorkers, (newSelected) => {
  emit('update:selectedWorkers', newSelected); // 부모에게 선택된 알바생 ID 전달
});
</script>

<style scoped>
.worker-list {
  font-family: Arial, sans-serif;
  padding: 20px;
  background-color: #F5F5F5;  /* 밝은 중립 색상 */
}

.worker-item {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #E0E0E0;  /* 중립 색상 */
  border-radius: 5px;
  background-color: white;
  display: flex;
  align-items: center;
}

.worker-item input[type="checkbox"] {
  margin-right: 10px;
  accent-color: #2D3250;  /* 진한 네이비 블루 */
}

.worker-id {
  font-weight: bold;
  color: #424769;  /* 중간 톤 네이비 블루 */
}

.worker-name {
  margin-left: 10px;
  color: #2D3250;  /* 진한 네이비 블루 */
  font-weight: 500;
}
</style>
