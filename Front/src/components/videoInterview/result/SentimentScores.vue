<template>
  <div class="sentiment-scores">
    <div class="score-item" :class="{ 'highlight': isPositive }">
      <span class="label">우수</span>

      <span class="value">{{ formatScore(scores.pos) }}</span>
    </div>
    <div class="score-item" :class="{ 'highlight': isNeutral }">
      <span class="label">보통</span>
      <span class="value">{{ formatScore(scores.neu) }}</span>
    </div>
    <div class="score-item" :class="{ 'highlight': isNegative }">
      <span class="label">추가 검토 필요</span>
      <span class="value">{{ formatScore(scores.neg) }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  scores: {
    type: Object,
    required: true
  }
})

const isPositive = computed(() => props.scores.pos > 0.5)
const isNeutral = computed(() => props.scores.neu > 0.5)
const isNegative = computed(() => props.scores.neg > 0.5)

const formatScore = (score) => (score * 100).toFixed(1) + '%'
</script>

<style scoped>
.sentiment-scores {
  display: flex;
  justify-content: center;
  gap: 3rem;
  margin: 2rem 0;
}

.score-item {
  text-align: center;
  padding: 1rem;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.score-item.highlight {
  background: #f6fbf9;  /* 밝은 배경색 */
  transform: scale(1.05);
}

.score-item .label {
  display: block;
  font-size: 1.1rem;
  color: #424769;  /* 중간 톤 네이비 블루 */
  margin-bottom: 0.5rem;
}

.score-item .value {
  font-size: 1.4rem;
  font-weight: 600;
  color: #2D3250;  /* 진한 네이비 블루 */
}
</style>
