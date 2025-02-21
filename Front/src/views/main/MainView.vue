<template>
  <div class="main-layout">
    <div class="main-content">
      <NoticeBar />
      <div class="main-component">
        <WeeklySchedule />
        <FeatureButtons />
        <!-- 추가된 위젯을 동적으로 렌더링 -->
        <div class="dynamic-widgets">
          <div v-for="widget in widgetStore.widgets" :key="widget.name" class="widget" @click="navigateTo('review')">
            <h3 class="title">
              <i :class="widget.name === '고객 리뷰' ? 'mdi mdi-account-multiple' : 'mdi mdi-view-grid'" color="#666" size="24"></i>
              {{ widget.name }}
            </h3>
            <div class="feature-card">
              <component :is="resolveComponent(widget.component)" />
            </div>
          </div>
        </div>
        <button class="add-widget-btn" @click="goToAddComponents">+ 위젯 추가하기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useWidgetStore } from '@/store/widget/widgetStore';
import { defineAsyncComponent, onMounted } from 'vue';
import NoticeBar from '@/components/notice/NoticeBar.vue';
import WeeklySchedule from '@/components/schedule/WeeklySchedule.vue';
import FeatureButtons from '@/components/main/FeatureButtons.vue';

const router = useRouter();
const widgetStore = useWidgetStore();

onMounted(() => {
  console.log("ReviewPage.vue : onMounted")
  if (widgetStore.reviews.length === 0) {
    widgetStore.addReviewsToStore();
  }
});

const goToAddComponents = () => {
  router.push('/add-components');
};

const navigateTo = (route) => {
  if (route) {
    router.push(`/${route}`);
  }
};

const resolveComponent = (name) => {
  const components = {
    ScheduleCard: defineAsyncComponent(() => import('@/views/Schedule/ScheduleCard.vue')),
    MemoPreviewCard: defineAsyncComponent(() => import('@/components/main/MemoPreviewCard.vue')),
    SalaryPreviewCard: defineAsyncComponent(() => import('@/components/main/SalaryPreviewCard.vue')),
    CustomerReview: defineAsyncComponent(() => import('@/components/main/ReviewPreviewCard.vue')),
    StockStatus: defineAsyncComponent(() => import('@/components/main/StockStatus.vue')),
    WorkLog: defineAsyncComponent(() => import('@/components/main/WorkLog.vue'))
  };
  return components[name] || null;
};
</script>

<style scoped>
.nav-fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 1000;
}

.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.dropdown-menu {
  position: absolute;
  top: 60px; /* 네비게이션 바 높이에 맞춰 조정 */
  left: 0;
  width: 100%;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transform: translateY(-100%);
  transition: transform 0.3s ease-in-out;
  z-index: 100;
}

.dropdown-menu.show {
  transform: translateY(0);
}

.dropdown-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dropdown-menu li {
  padding: 1rem 2rem;
  cursor: pointer;
  transition: background-color 0.3s;
}

.dropdown-menu li:hover {
  background-color: #f5f5f5;
}

.main-content {
  flex: 1;
  margin-bottom: 60px; /* Footer 높이 */
  min-height: calc(100vh - 120px);
}

.main-component {
  flex: 1;
  max-width: 1200px;
  margin: 2rem auto;
  padding: 0 1rem;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.dynamic-widgets {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin-top: 1rem;
  padding: 0 1.5rem;
}

.title {
  color:#666;
  padding: 1rem;
}

.widget {
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}

.widget:hover {
  transform: translateY(-4px);
}

.feature-card {
  height: 375px;
  background: white;
  padding: 1.5rem;
  border-radius: 30px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
  border: 1px solid #e3e3e3; /*테두리 색상과 두께 */
  box-shadow: 0 5px 10px rgba(0,0,0,0.2);
}

.add-widget-btn {
  grid-column: span 3;
  background: #424769;
  padding: 15px;
  text-align: center;
  cursor: pointer;
  border-radius: 8px;
  color: #ffffff;
  font-size: small;
  font-weight: bold;
}
</style>
