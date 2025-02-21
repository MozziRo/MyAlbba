<template>
  <div>
    <div class="container" v-if="widgetStore.sentiment && widgetStore.sentiment.sentiment_scores">
      <div class="sentiment-result">
        <h2 class="title">종합 의견</h2>
        <SentimentResults v-if="widgetStore.sentiment" :sentiment="widgetStore.sentiment"/>
      </div>
      <div class="customer-review">
        <h2 class="title">최근 리뷰</h2>
        <CustomerReview  v-if="widgetStore.reviews" :reviews="widgetStore.reviews"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import SentimentResults from '@/components/review/SentimentResults.vue';
import CustomerReview from '@/components/review/CustomerReviews.vue';
import { useWidgetStore } from '@/store/widget/widgetStore.js';
import { ref, onMounted } from 'vue';

const widgetStore = useWidgetStore();

onMounted(() => {
  console.log("ReviewPage.vue : onMounted")
  widgetStore.addReviewsToStore();
});

</script>

<style scoped>
.container {
  margin: 0 auto;
  padding: 3rem 10rem;
  display: flex;
}

.sentiment-result,
.customer-review {
  flex: 1;
  max-width: 50%;
}

.title {
  color: #333D4B;
}

.sentiment-result .title {
  margin-bottom: 100px;
}
</style>
