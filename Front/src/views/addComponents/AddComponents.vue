<template>
  <div class="container">
    <h1 class="title">
      <span class="mdi mdi-plus-box"></span>
      추가할 위젯을 선택하세요</h1>
    <div class="widget-layout">
        <div v-for="widget in availableWidgets" :key="widget.name" class="widget-frame">
            <div class="widget-name">{{ widget.name }}</div>
            <component :is="resolveComponent(widget.component)" class="widget-component"/>
            <div class="btn-layout">
              <button class="add-btn" @click="addNewWidget(widget)">
                + 위젯 추가하기</button>
            </div>
        </div>
    </div>
    <button class="back-btn" @click="goBack">돌아가기</button>

    <reviews-loading-modal
      v-if="isModalOpen"
      @close="closeModal"
    />

  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useWidgetStore } from '@/store/widget/widgetStore';
import { ref, defineAsyncComponent } from 'vue';
import ReviewsLoadingModal from '@/components/review/ReviewsLoadingModal.vue';

const router = useRouter();
const widgetStore = useWidgetStore();
const isModalOpen = ref(false);

const availableWidgets = ref([
  { name: '고객 리뷰', component: 'CustomerReview' },
  { name: '재고 현황', component: 'StockStatus' },
  { name: '근무 기록 일지', component: 'WorkLog' }
]);

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

const addNewWidget = async (widget) => {
  isModalOpen.value = true; // 리뷰 로딩 모달창 열기

  await widgetStore.addWidget(widget);

  isModalOpen.value = false; // 리뷰 로딩 모달창 닫기
  router.push('/main'); // 추가 후 메인 페이지로 이동
};

const closeModal = () => {
  isModalOpen.value = close;
  router.push('/main');
}

const goBack = () => {
  router.push('/main');
};
</script>

<style scoped>
.container {
  margin: 0 auto;
  padding: 3rem 10rem;
}

.title {
  color: #2D3250;
}

.grid {
  display: grid;
  gap: 10px;
}

.add-btn {
  margin-top: 10px;
  width: 200px;
  background: rgba(66, 71, 105, 0.1);
  font-size: small;
  font-weight: bold;
  color: #424769;
  padding: 10px;
  border-radius: 10px;
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
}

.add-btn:hover {
  background: rgba(66, 71, 105, 0.2);
}

.btn-layout {
  display: flex;
  justify-content: center;
  padding: 1rem;
}

.back-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: #333;
  border-radius: 10px;
  border: 1px solid #ddd;
  cursor: pointer;
  transition: all 0.2s ease;
}

.back-btn:hover {
  background-color: rgba(66, 71, 105, 0.1);
  color: #333;
}

.widget-layout {
  display: flex;
  justify-content: space-around;
  padding: 1rem 0rem;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}

.widget-name {
  padding: 1rem 0rem;
  font-weight: bold;
  color: #333;
}

.widget-frame {
  flex-direction: column;
  padding: 1rem;
  border-radius: 12px;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;
}

.widget-frame:hover {
  transform: translateY(-4px);
}

.widget-component {
  height: 300px;
  width: 300px;
  background: white;
  padding: 1.5rem;
  border-radius: 30px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
  border: 1px solid #e3e3e3;
  box-shadow: 0 5px 10px rgba(66, 71, 105, 0.2);
}
</style>

