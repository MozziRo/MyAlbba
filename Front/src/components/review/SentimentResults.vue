<template>
  <div class="text-center my-6">
    <p>최근 20개의 리뷰 분석 결과</p>
    <p class="text-bold">{{ storeData.name }}은/는 <br/>
      고객들에게 {{ resultText }}를 받고 있네요!</p>
    <div class="sentiment-result">
      <div class="text-center">
        <span class="text-5xl emoji">😊</span>
        <p class="font-semibold">만족스러워요 <br/> {{ formatScore(sentiment.sentiment_scores.pos) }}</p>
      </div>
      <div class="text-center">
        <span class="text-5xl emoji">😐</span>
        <p class="font-semibold">무난해요 <br/> {{ formatScore(sentiment.sentiment_scores.neu) }}</p>
      </div>
      <div class="text-center">
        <span class="text-5xl emoji">🤔</span>
        <p class="font-semibold">아쉬워요 <br/> {{ formatScore(sentiment.sentiment_scores.neg) }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, watch } from 'vue';
import { getStore } from '@/common/apis/api/store';

const props = defineProps({
  sentiment: Object,
});

const storeData = ref({});
const resultText = ref('');

const formatScore = (score) => (score * 100).toFixed(1) + '%';

onMounted(async () => {
  storeData.value = await getStore();
});

watch(() => props.sentiment, (newSentiment) => {
  if (newSentiment && newSentiment.sentiment_scores) {
    const { pos, neu, neg } = newSentiment.sentiment_scores;

    if (pos >= neu && pos >= neg) {
      resultText.value = '좋은 평가';
    } else if (neu >= pos && neu >= neg) {
      resultText.value = '무난한 평가';
    } else {
      resultText.value = '아쉬운 평가';
    }
  }
}, { immediate: true });

</script>

<style scoped>
.text-bold {
  font-size: larger;
  font-weight: bold;
}

.sentiment-result {
  display: flex;
  justify-content: center;
  padding: 1rem;
  gap: 2rem;
}

.emoji {
  font-size: 5rem;
}

.font-semibold {
  color: #424769;
}
</style>
