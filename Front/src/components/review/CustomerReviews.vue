<template>
  <div class="review-container">
    <div class="review-layout" ref="reviewLayout">
      <div
        v-for="(review, index) in visibleReviews"
        :key="index"
        class="review-card"
        @mouseenter="pauseAutoSlide"
        @mouseleave="resumeAutoSlide"
      >
        <div class="flex items-center mb-2">
        </div>
        <p class="review-text">{{ review.review_text }}</p>
      </div>
    </div>

    <!-- 페이지네이션 표시 -->
    <div class="pagination">
      <span
        v-for="(dot, index) in totalPages"
        :key="index"
        class="dot"
        :class="{ active: index === currentPage }"
        @click="goToPage(index)"
      ></span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, defineProps } from 'vue';

const props = defineProps({
  reviews: Array,
});

const currentPage = ref(0);
const reviewsPerPage = 5;
let interval = null;
const isHovered = ref(false); // ✅ Hover 상태 감지

// 현재 페이지에 해당하는 리뷰 5개씩 가져오기
const visibleReviews = computed(() => {
  const start = currentPage.value * reviewsPerPage;
  return props.reviews.slice(start, start + reviewsPerPage);
});

// 전체 페이지 수 계산
const totalPages = computed(() => {
  return Math.ceil(props.reviews.length / reviewsPerPage);
});

// 다음 페이지로 이동
const nextPage = () => {
  if (!isHovered.value) { // ✅ Hover 중이 아닐 때만 자동 변경
    currentPage.value = (currentPage.value + 1) % totalPages.value;
  }
};

// 특정 페이지로 이동
const goToPage = (index) => {
  currentPage.value = index;
  resetAutoSlide();
};

// 자동 슬라이드 시작
const startAutoSlide = () => {
  interval = setInterval(nextPage, 5000); // 5초마다 변경
};

// 자동 슬라이드 정지
const pauseAutoSlide = () => {
  isHovered.value = true; // ✅ Hover 상태 업데이트
  clearInterval(interval);
};

// 자동 슬라이드 재시작
const resumeAutoSlide = () => {
  isHovered.value = false; // ✅ Hover 상태 해제
  startAutoSlide();
};

// 초기 자동 슬라이드 시작
onMounted(startAutoSlide);
onUnmounted(() => clearInterval(interval));
</script>

<style scoped>
.review-container {
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.review-layout {
  overflow: hidden;
  white-space: nowrap;
  width: 100%;
  justify-content: center;
}

.review-card {
  flex: 0 0 240px;
  margin: 1rem;
  padding: 1rem;
  border: 1px solid #e3e3e3;
  background: white;
  box-shadow: 0 2px 5px rgba(66, 71, 105, 0.1);
  border-radius: 10px;
  text-align: left;
  overflow: hidden;
  max-height: 150px;
  transition: max-height 0.3s ease-in-out;
}

.review-card:hover {
  max-height: 500px;
  overflow: visible;
}

.review-text {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  transition: all 0.3s ease-in-out;
}

.review-card:hover .review-text {
  -webkit-line-clamp: unset;
  white-space: normal;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

.dot {
  width: 10px;
  height: 10px;
  margin: 0 5px;
  background-color: #d4d4d4;
  border-radius: 50%;
  display: inline-block;
  cursor: pointer;
  transition: background 0.3s;
}

.dot.active {
  background-color: #424769;
}
</style>
