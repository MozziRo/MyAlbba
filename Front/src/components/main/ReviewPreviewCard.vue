<template>
  <div class="review-layout">
    <h4 class="title">{{ storeData.name }}에 대한 리뷰</h4>
    
    <div class="review-card">
      <div class="icon-layout">
        <p class="review-user-icon">🗣️</p>
    </div>
      
      <p class="review-text">
        {{ widgetStore.reviews[currentReviewIndex]?.review_text || "등록된 리뷰가 없습니다." }}
      </p>
    </div>

    <!-- 이전 / 다음 버튼 추가 -->
    <div class="review-nav">
      <button @click.stop="prevReview" :disabled="currentReviewIndex === 0">이전</button>
      <button @click.stop="nextReview" :disabled="currentReviewIndex === widgetStore.reviews.length - 1">다음</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useWidgetStore } from '@/store/widget/widgetStore';
import { getStore } from '@/common/apis/api/store';

const widgetStore = useWidgetStore();
const storeData = ref({});
const currentReviewIndex = ref(0); // 현재 리뷰 인덱스

onMounted(async () => {
  storeData.value = await getStore();
  if (!widgetStore.reviews || widgetStore.reviews.length === 0) {
    widgetStore.addReviewsToStore();
  }
});

// 이전 리뷰 보기
const prevReview = () => {
  if (currentReviewIndex.value > 0) {
    currentReviewIndex.value--;
  }
};

// 다음 리뷰 보기
const nextReview = () => {
  if (currentReviewIndex.value < widgetStore.reviews.length - 1) {
    currentReviewIndex.value++;
  }
};
</script>

<style scoped>
.review-layout {
  max-width: 300px;
  text-align: center;
}

.title {
  color: #333;
}

.review-card {
  margin: 1rem;
  padding: 1rem;
  border: 1px solid #e3e3e3;
  border-radius: 10px;
  min-width: 100px;
  overflow: hidden;
  height: 150px;
}

.icon-layout {
  display: flex;
}

.review-user-icon {
  font-size: 32px;
  margin-bottom: 8px;
  color: red;
}

.review-text {
  white-space: normal;
  max-height: 300px;
  max-width: 100%;
  font-size: small;
}

.review-nav {
  display: flex;
  justify-content: space-around;
  margin-top: 10px;
}

button {
  border: 1px solid  #424769;
  color: #333;
  padding: 5px 10px;
  border-radius: 10px;
  cursor: pointer;
}

button:hover {
  background-color: #e0f7fa;
  border-color:  #2D3250;
  color:  #424769;;
}

button:disabled {
  background-color: #f9f9f9;
  color: #ccc;
  border-color: #ddd;
  cursor: not-allowed;
}

.review-card {
  margin: 1rem;
  padding: 1rem;
  border: 1px solid #e3e3e3;
  border-radius: 10px;
  min-width: 100px;
  white-space: nowrap;
  background: white;
  box-shadow: 0 2px 5px rgba(66, 71, 105, 0.1);
  transition: transform 0.2s ease;
}

.review-card:hover {
  transform: translateY(-4px);
}

.review-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
  max-width: 100%;
  color: #424769;
}
</style>
