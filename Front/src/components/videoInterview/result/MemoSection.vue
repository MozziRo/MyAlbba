<template>
  <div class="summary-text" :class="{ 'hidden-on-print': !printOptions.includeMemo }" v-show="printOptions.includeMemo">
    <h3>작성 내용</h3>
    <div class="memo-content">
      {{ memoText }}
    </div>
  </div>
</template>

<script setup>
defineProps({
  memoText: {
    type: String,
    required: true
  },
  printOptions: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.summary-text {
  margin: 1rem 0;
  padding: 1rem;
  background: #F5F5F5;  /* 중립 색상 사용 */
  border-radius: 8px;
  font-size: 1.2rem;
  color: #2D3250;  /* 진한 네이비 블루로 변경 */
  white-space: pre-wrap;
}

.memo-content {
  white-space: pre-wrap;
  line-height: 1.6;
  color: #424769;  /* 중간 톤 네이비 블루로 변경 */
  font-size: 1.1rem;
}
</style>
