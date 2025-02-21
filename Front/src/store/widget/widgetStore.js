import { defineStore } from 'pinia';
import { ref, watch } from 'vue';
import { fetchAndSaveReviews, getReviews } from '@/common/apis/api/widget';

export const useWidgetStore = defineStore('widgetStore', () => {
  const widgets = ref([]);
  const reviews = ref([]);
  const sentiment = ref({});

  // localStorage에서 위젯 데이터를 가져와 저장
  const loadWidgets = () => {
    const storedWidgets = localStorage.getItem('widgets');
    if (storedWidgets) {
      widgets.value = JSON.parse(storedWidgets);
    }
  };

  // 위젯 추가 및 크롤링 요청
  const addWidget = async (widget) => {
    try {
      const response = await fetchAndSaveReviews();
      widgets.value.push(widget);
    } catch (error) {
      console.log("위젯 추가를 실패하였습니다.");
    }
  };

  // 리뷰 가져오기
  const addReviewsToStore = async () => {
    try {
      const response = await getReviews();
      reviews.value = response.data.reviews;
      sentiment.value = response.data.sentiment;
    } catch (error) {
      console.log("리뷰 조회를 실패하였습니다.");
    }
  }

  // localStorage에 위젯 데이터 저장
  watch(
    widgets,
    (newWidgets) => {
      localStorage.setItem('widgets', JSON.stringify(newWidgets));
    },
    { deep: true }
  );

  loadWidgets();

  return { 
    widgets, 
    reviews,
    sentiment,
    
    addWidget,
    addReviewsToStore 
  };
});
