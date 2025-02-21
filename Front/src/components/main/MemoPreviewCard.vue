<template>
  <div class="card memo-card">
    <div class="card-header">
      <div class="card-icon">
        <div class="avatar">
          <i class="mdi mdi-message-text icon"></i>
        </div>
      </div>
      <div class="card-title-section">
        <h3 class="card-title">최근 알림장</h3>
        <span class="card-date">{{ latestMemoDate }}</span>
      </div>
    </div>
    <div class="card-body">
      <div v-if="memos.length > 0" class="memo-preview">
        {{ memos[0].content }}
      </div>
      <div v-else class="no-memo">
        새로운 알림장이 없습니다
      </div>
    </div>
    <div class="card-footer">
      <span class="chip">{{ unreadCount }}개의 새 알림</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia';
import { getReceivedMemos, getAllMemos } from '@/common/apis/api/memo'
import { useMemberStore } from '@/store/member/memberStore';

const memberStore = useMemberStore();
const { isOwner } = storeToRefs(memberStore);

const memos = ref([])

onMounted(async () => {
  try {
    if(!isOwner) {
      const response = await getReceivedMemos()
      memos.value = response
    } else {
      const response = await getAllMemos()
      memos.value = response
    }
  } catch (error) {
    console.error('메모 로딩 실패:', error)
  }
})

const latestMemoDate = computed(() => {
  if (!memos.value.length) return ''
  return new Date(memos.value[0].createdAt).toLocaleString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
})

const unreadCount = computed(() => {
  return memos.value.filter(memo => memo.status === 'NOT_DONE').length
})
</script>

<style scoped>
.card {
  background-color: #fff;
  border-radius: 16px;
  padding: 24px;
  font-family: 'Pretendard', sans-serif;
  transition: transform 0.2s ease;
  height: 320px;
  display: flex;
  flex-direction: column;
}

.card:hover {
  transform: translateY(-4px);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.card-icon {
  margin-right: 16px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar .icon {
  font-size: 28px;
  color: #F9B17A;
}

.card-title-section {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 1.25rem;
  margin: 0;
  font-weight: 600;
}

.card-date {
  font-size: 0.9rem;
  color: #999;
}

.card-body {
  flex: 1;
  padding: 20px 0;
  font-size: 1rem;
  color: #333;
  display: flex;
  align-items: flex-start;
  min-height: 160px;
}

.memo-preview {
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.6;
}

.no-memo {
  font-size: 1rem;
  color: #666;
  font-style: italic;
  width: 100%;
  text-align: center;
  padding: 20px 0;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.chip {
  background-color: rgba(66, 71, 105, 0.1);
  color: #424769;
  padding: 6px 14px;
  border-radius: 12px;
  font-size: 0.9rem;
  font-weight: 500;
}
</style>
