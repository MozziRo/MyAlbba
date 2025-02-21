<template>
  <div class="progress-container">
    <div
      v-for="(step, index) in steps"
      :key="index"
      class="progress-wrapper"
    >
      <div class="progress-item" :class="{ active: currentStep >= index + 1 }">
        <div class="progress-circle">{{ index + 1 }}</div>
        <span class="progress-text">{{ step }}</span>
      </div>
      <div
        v-if="index < steps.length - 1"
        class="progress-line"
        :class="{ active: currentStep >= index + 2 }"
      ></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  steps: {
    type: Array,
    required: true
  },
  currentStep: {
    type: Number,
    required: true
  }
})
</script>

<style scoped>
.progress-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 60px;
  font-family: 'Pretendard';
  color: #333;
}

.progress-wrapper {
  display: flex;
  align-items: center;
}

.progress-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.progress-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 8px;
  border: 2px solid #ddd;
}

.progress-text {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
}

.progress-line {
  width: 100px;
  height: 2px;
  background-color: #ddd;
  margin: 0 15px;
  margin-bottom: 30px;
}

.progress-item.active .progress-circle {
  background-color: #424769;
  color: white;
  border-color: #424769;
}

.progress-item.active .progress-text {
  color: #424769;
}

.progress-line.active {
  background-color: #424769;
}

@media (max-width: 768px) {
  .progress-line {
    width: 50px;
  }
}
</style>
