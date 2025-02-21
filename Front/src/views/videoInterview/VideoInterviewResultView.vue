<template>
  <div class="result-container">
    <!-- 우측 상단 프린트 버튼 -->
    <button class="print-button" @click="printPage">
      <i class="fas fa-print"></i>
    </button>

    <h1>면접 결과 분석</h1>
    <div v-if="isLoading" class="loading">
      결과를 분석중입니다...
    </div>
    <div v-else class="result-content">
      <!-- 지원자 이름 -->
      <div class="applicant-name">
        <h2 style="font-size: 0.8em;">지원자: </h2>
        <span style="color: var(--primary); font-size: 1.2em;">{{ interviewStore.currentMemo.applicantName }}</span>
      </div>

      <!-- 사장님이 작성한 메모모 -->
      <MemoSection
        :memo-text="interviewStore.currentMemo.memoText"
        :print-options="printOptions"
      />

      <!-- 분석 결과 -->
      <!-- 삼각형 -->
      <div class="analysis-section" :class="{ 'hidden-on-print': !printOptions.includeAnalysis }" v-show="printOptions.includeAnalysis">
        <SentimentTriangle :sentiment-scores="analysisResult.sentiment_scores" />

        <div class="result-text-container">
          <div class="sentiment-indicator">
            <i :class="sentimentIcon"></i>
          </div>
          <p class="result-text">
            {{ formatText(analysisResult.text) }}
          </p>
        </div>
        <!-- 점수 -->
        <SentimentScores :scores="analysisResult.sentiment_scores" />
      </div>

      <!-- 프린트 옵션 -->
      <PrintOptions v-model="printOptions" />
    </div>

    <!-- 성공 실패 모달 -->
    <SuccessFailureModal
      v-if="showModal"
      :isVisible="showModal"
      :isSuccess="modalSuccess"
      :message="modalMessage"
      @close="closeModal" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useInterviewStore } from '@/store/video/interviewStore'
import { analyzeSentiment, extractKeywords } from '@/common/apis/api/interview'
import SuccessFailureModal from '@/components/common/SuccessFailureModal.vue'
import MemoSection from '@/components/videoInterview/result/MemoSection.vue'
import SentimentTriangle from '@/components/videoInterview/result/SentimentTriangle.vue'
import SentimentScores from '@/components/videoInterview/result/SentimentScores.vue'
import PrintOptions from '@/components/videoInterview/result/PrintOptions.vue'

const route = useRoute()
const interviewStore = useInterviewStore()
const isLoading = ref(true)
const printOptions = ref({
  includeMemo: false,
  includeAnalysis: true
})
const STORAGE_KEY_CURRENT = 'current_memo'

// 초기화
const analysisResult = ref({
  text: "",
  sentiment_scores: {
    pos: 0,
    neg: 0,
    neu: 0,
  }
})

// 더미 메모 데이터
const dummyMemo = {
  applicantName: "",
  memoText: "",
  timestamp: new Date().toLocaleString()
}

// 텍스트 포맷팅 함수 추가
const formatText = (text) => {
  // 40자마다 줄바꿈 추가
  return text.replace(/(.{40})/g, '$1\n');
}

// 모달 관련 변수
const showModal = ref(false)
const modalSuccess = ref(true)
const modalMessage = ref('')

const sentimentClass = computed(() => {
  const pos = analysisResult.value.sentiment_scores.pos
  const neg = analysisResult.value.sentiment_scores.neg
  const neu = analysisResult.value.sentiment_scores.neu

  if (pos > neg && pos > neu) return 'positive'
  if (neg > pos && neg > neu) return 'negative'
  return 'neutral'
})

const sentimentIcon = computed(() => {
  const pos = analysisResult.value.sentiment_scores.pos
  const neg = analysisResult.value.sentiment_scores.neg

  if (pos > neg) return 'fas fa-smile'
  if (neg > pos) return 'fas fa-frown'
  return 'fas fa-meh'
})

const analyzeMemo = async () => {
  isLoading.value = true
  try {
    // 감성 분석 API 요청
    const sentimentResult = await analyzeSentiment({text: interviewStore.currentMemo.memoText});
    // 키워드 분석 API 요청
    const keywordsResult = await extractKeywords({text: interviewStore.currentMemo.memoText});

    // 할당
    analysisResult.value.text = keywordsResult.text
    analysisResult.value.sentiment_scores = sentimentResult.sentiment_scores

  } catch (error) {
    console.error('API 요청 중 에러 발생:', error)
    modalSuccess.value = false
    modalMessage.value = "분석 중 오류가 발생했습니다."
    showModal.value = true
  } finally {
    isLoading.value = false
  }
}

const closeModal = () => {
  showModal.value = false
}

const printPage = () => {
  window.print()
}

onMounted(() => {
  // 스토어에 데이터가 없는 경우 로컬스토리지에서 불러오기
  if (!interviewStore.currentMemo.memoText) {
    const storedMemo = localStorage.getItem(STORAGE_KEY_CURRENT)
    if (storedMemo) {
      const memoData = JSON.parse(storedMemo)
      interviewStore.setMemo(memoData)
    }
  }
  // 더미
  // interviewStore.setMemo(dummyMemo)
  analyzeMemo()
})
</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css');

.result-container {
  position: relative;
  padding: 2rem;
  text-align: center;
  background: linear-gradient(135deg, #f5f7fa, #c3cfe2);
  min-height: 100vh;
  font-family: 'Pretendard', sans-serif;
}

.print-button {
  position: absolute;
  top: 20px;
  right: 20px;
  background: var(--primary);
  color: white;
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  font-size: 1.5rem;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease;
}

.print-button:hover {
  transform: scale(1.1);
}

h1 {
  margin-bottom: 1rem;
  font-size: 2.5rem;
  color: #333;
}

.loading {
  font-size: 1.5rem;
  margin-top: 2rem;
  color: #666;
}

.result-content {
  background: white;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 800px;
  width: 100%;
  box-sizing: border-box;
  margin: 0 auto;
  overflow: hidden;
}

.analysis-section {
  margin: 2rem 0;
  padding: 2rem;
  background: #f6fbf9;
  border-radius: 12px;
}

.result-text-container {
  text-align: center;
  margin: 2rem 0;
  padding: 2rem;
  background:#fae7d9;
  border-radius: 12px;
}

.sentiment-indicator {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.result-text {
  font-size: 1.2rem;
  line-height: 1.8;
  color: #333;
  margin-top: 1rem;
  white-space: pre-wrap;
  word-break: keep-all;
}

.hidden-on-print {
  display: none !important; /* 프린트 시 완전히 숨기기 */
}

@media print {
  .print-button,
  .print-options {
    display: none;
  }
  .hidden-on-print {
    display: none !important;
  }
  .result-container {
    padding: 0;
    background: none;
    min-height: auto;
  }

  .result-content {
    width: 100%;
    max-width: none;
    margin: 0;
    padding: 0;
    box-shadow: none;
    background: none;
  }

  .analysis-section {
    page-break-inside: avoid;
    margin: 1rem 0;
    padding: 1rem;
  }

  h1 {
    margin-top: 0;
  }
}
</style>
