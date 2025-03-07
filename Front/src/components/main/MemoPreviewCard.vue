<template>
  <div class="memo-container">
    <div class="memo-header">
      <div class="left-area">
        <div class="icon-container">
          <i class="mdi mdi-message-text"></i>
        </div>
      </div>
      <div class="right-area">
        <div class="top-row">
          <span class="memo-title">최근 알림장</span>
          <span class="count-label">{{ memos.length }}개의 알림</span>
        </div>
        <div class="date-row"></div>
      </div>
    </div>
    
    <div class="memo-content">
      <div class="memo-list">
        <div v-for="(memo, index) in displayedMemos" :key="index" class="memo-item">
          <div class="item-content">
            <div class="bubble-icon">
              <svg viewBox="0 0 24 24" class="chat-bubble">
                <path d="M12 21a9 9 0 0 1-9-9 9 9 0 0 1 9-9 9 9 0 0 1 9 9c0 1.53-.384 2.97-1.058 4.23l1.058 3.77-3.77-1.058A8.96 8.96 0 0 1 12 21z"/>
              </svg>
            </div>
            <div class="memo-tag">{{ memo?.creator || '작성자' }}</div>
            <div class="memo-text">{{ memo?.content || '내용 없음' }}</div>
          </div>
        </div>
      </div>
      
      <button v-if="hasMoreMemos" @click="showMore" class="show-more-btn">
        더보기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { getReceivedMemos } from '@/common/apis/api/memo'

// 컴포넌트 마운트 상태를 추적하는 변수
const isComponentMounted = ref(true)
const memos = ref([])
const displayLimit = ref(3)

const displayedMemos = computed(() => {
  return memos.value.slice(0, displayLimit.value)
})

const hasMoreMemos = computed(() => {
  return memos.value.length > displayLimit.value
})

const showMore = () => {
  displayLimit.value += 3
}

// API 요청을 저장할 변수
let apiRequest = null

onMounted(async () => {
  try {
    if (isComponentMounted.value) {
      const response = await getReceivedMemos()
      
      if (isComponentMounted.value) {
        memos.value = Array.isArray(response) ? response : []
      }
    }
  } catch (error) {
    if (isComponentMounted.value) {
      console.error('메모 로딩 실패:', error)
    }
  }
})

onBeforeUnmount(() => {
  isComponentMounted.value = false
  
  if (apiRequest && typeof apiRequest.cancel === 'function') {
    apiRequest.cancel('컴포넌트 언마운트로 인한 요청 취소')
  }
})
</script>

<style scoped>
.memo-container {
  height: 100%;
  width: 100%; /* 부모 컨테이너에 꽉 차도록 설정 */
  padding: 1.25rem 1rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.02);
  box-sizing: border-box;
}

.memo-header {
  display: flex;
  align-items: flex-start;
  padding-bottom: 0.75rem;
  gap: 5px;
  width: 100%;
}

.left-area {
  display: flex;
  align-items: flex-start;
}

.right-area {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.top-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.date-row {
  display: flex;
  align-items: center;
  margin-top: 4px;
}

.icon-container {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #f3f3f3;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-right: 5px;
}

.icon-container i {
  font-size: 20px;
  color: #F9B17A;
}

.memo-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #424769;
}

.date-label {
  font-size: 0.75rem;
  color: white;
  background-color: #424769;
  padding: 2px 10px;
  border-radius: 15px;
}

.count-label {
  font-size: 0.75rem;
  color: #424769;
  background-color: rgba(66, 71, 105, 0.1);
  padding: 2px 10px;
  border-radius: 15px;
}

.memo-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-left: 10px;
  padding-right: 10px;
  width: 100%;
}

.memo-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  width: 100%;
  margin: 0 auto;
}

.memo-item {
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.7);
  overflow: hidden;
  margin-bottom: 2px;
  border: 1px solid rgba(220, 220, 220, 0.5);
  width: 100%;
}

.item-content {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  gap: 10px;
  width: 100%;
}

.bubble-icon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
}

.chat-bubble {
  width: 100%;
  height: 100%;
  fill: none;
  stroke: #424769;
  stroke-width: 1.5;
}

.memo-tag {
  font-size: 0.8rem;
  background: rgba(66, 71, 105, 0.1);
  color: #424769;
  padding: 2px 10px;
  border-radius: 12px;
  white-space: nowrap;
  font-weight: 500;
  flex-shrink: 0;
  min-width: 60px;
  padding: 4px 8px;
  border-radius: 6px;
}

.memo-text {
  font-size: 0.85rem;
  color: #424769;
  margin-left: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex: 1;
}

.show-more-btn {
  align-self: center;
  background-color: transparent;
  border: 1px solid #F9B17A;
  color: #F9B17A;
  font-size: 0.85rem;
  font-weight: 500;
  padding: 6px 16px;
  cursor: pointer;
  border-radius: 15px;
  margin-top: 4px;
}

.show-more-btn:hover {
  background-color: rgba(66, 71, 105, 0.05);
}
</style>
