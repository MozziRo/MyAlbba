<template>
  <div class="triangle-container">
    <svg viewBox="0 0 100 100" class="analysis-triangle">
      <!-- 동적 삼각형 -->
      <path :d="trianglePath" :class="sentimentClass" />
      <!-- 기준 삼각형 (점선) -->
      <path d="M50 10 L90 80 L10 80 Z" class="base-triangle" />

      <!-- 점수 표시 -->
      <text x="50" y="5" class="score-label positive">긍정</text>
      <text x="95" y="85" class="score-label negative">부정</text>
      <text x="5" y="85" class="score-label neutral">중립</text>

      <!-- 현재 점수 -->
      <text x="50" y="50" class="current-score">{{ overallSentiment }}</text>
    </svg>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  sentimentScores: {
    type: Object,
    required: true
  }
})

const trianglePath = computed(() => {
  const scores = props.sentimentScores

  const centerX = 50
  const centerY = 45

  const posDistance = 10 + scores.pos * 60
  const negDistance = 10 + scores.neg * 60
  const neuDistance = 10 + scores.neu * 60

  const topPoint = `${centerX} ${centerY - posDistance}`
  const rightPoint = `${centerX + negDistance} ${centerY + (negDistance/2)}`
  const leftPoint = `${centerX - neuDistance} ${centerY + (neuDistance/2)}`

  return `M ${topPoint} L ${rightPoint} L ${leftPoint} Z`
})


const sentimentClass = computed(() => {
  const pos = props.sentimentScores.pos;  // 긍정 점수
  const neg = props.sentimentScores.neg;  // 부정 점수
  const neu = props.sentimentScores.neu;  // 중립 점수

  if (pos > neg && pos > neu) return 'positive';        // 긍정
  if (neg > pos && neg > neu) return 'negative';        // 부정
  if (neu > pos && neu > neg) return 'neutral';         // 중립
  return 'neutral';                                      // 기본값
})

const overallSentiment = computed(() => {
  const pos = props.sentimentScores.pos;  // 긍정 점수
  const neg = props.sentimentScores.neg; // 부정 점수
  const neu = props.sentimentScores.neu;  // 중립 점수

  if (pos > neg && pos > neu) return '긍정적';          // 긍정적
  if (neg > pos && neg > neu) return '부정적';          // 부정적
  if (neu > pos && neu > neg) return '중립적';          // 중립적
  return '중립적'
})

</script>

<style scoped>
.triangle-container {
  width: 100%;
  max-width: 500px;
  margin: 0 auto;
  padding: 2rem;
}

.analysis-triangle {
  width: 100%;
  height: auto;
}

.base-triangle {
  fill: none;
  stroke: #E0E0E0;  /* 중립 색상 */
  stroke-dasharray: 4;
  stroke-width: 0.5;
}

/* 색상 수정 */
path.positive {
  fill: rgba(45, 50, 80, 0.2);  /* 진한 네이비 블루 + 투명도 */
}

path.slightly-positive {
  fill: rgba(66, 71, 105, 0.2);  /* 중간 톤 네이비 블루 + 투명도 */
}

path.negative {
  fill: rgba(255, 68, 68, 0.2);  /* 빨간색 + 투명도 */
}

path.very-negative {
  fill: rgba(204, 0, 0, 0.2);  /* 어두운 빨간색 + 투명도 */
}

path.neutral {
  fill: rgba(224, 224, 224, 0.2);  /* 중립 색상 + 투명도 */
}

.score-label {
  font-size: 4px;
  fill: #424769;  /* 중간 톤 네이비 블루 */
  text-anchor: middle;
  font-family: 'Pretendard', sans-serif;
}

.current-score {
  font-size: 6px;
  fill: #2D3250;  /* 진한 네이비 블루 */
  text-anchor: middle;
  font-weight: 600;
  font-family: 'Pretendard', sans-serif;
}
</style>
