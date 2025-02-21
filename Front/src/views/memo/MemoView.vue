<template>
  <div class="memo-page">
    <!-- 메인 화면 레이아웃 -->
    <div class="main-wrapper">
      <!-- 왼쪽 사이드바 -->
      <div v-if="!isOwner" class="sidebar">
        <h1 class="text-lg text-white mb-4">메모장</h1>
        
        <div class="memo-folders mb-6">
          <div
            :class="['folder-item', { active: activeTab === 'received' }]"
            @click="switchTab('received')"
          >
            <v-icon>mdi-inbox</v-icon>
            <span>수신함</span>
            <span class="folder-count">{{ receivedCount }}</span>
          </div>
          <div
            :class="['folder-item', { active: activeTab === 'sent' }]"
            @click="switchTab('sent')"
          >
            <v-icon>mdi-send</v-icon>
            <span>발신함</span>
            <span class="folder-count">{{ sentCount }}</span>
          </div>
        </div>

        <!-- 상태 섹션 추가 -->
        <div class="status-section">
          <h2 class="text-xs text-neutral-300 uppercase mb-2 px-3">상태</h2>
          <div class="space-y-1">
            <div 
              :class="['folder-item', { active: statusFilter === 'ALL' }]"
              @click="handleStatusFilterChange('ALL')"
            >
              <v-icon>mdi-pound</v-icon>
              <span>전체</span>
              <span class="folder-count">{{ totalCount }}</span>
            </div>
            <div 
              :class="['folder-item', { active: statusFilter === 'NOT_DONE' }]"
              @click="handleStatusFilterChange('NOT_DONE')"
            >
              <v-icon>mdi-pound</v-icon>
              <span>진행중</span>
              <span class="folder-count">{{ inProgressCount }}</span>
            </div>
            <div 
              :class="['folder-item', { active: statusFilter === 'DONE' }]"
              @click="handleStatusFilterChange('DONE')"
            >
              <v-icon>mdi-pound</v-icon>
              <span>완료</span>
              <span class="folder-count">{{ completedCount }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 오른쪽 메인 컨텐츠 영역 -->
      <div class="main-content">
        <!-- 검색 및 정렬 헤더 -->
        <div class="content-header">
          <div class="search-container">
            <div class="search-input">
              <v-icon>mdi-magnify</v-icon>
              <input
                v-model="searchQuery"
                placeholder="메모 검색..."
                type="text"
              />
            </div>
            <div class="sort-select-wrapper">
              <v-icon class="sort-icon">mdi-sort-variant</v-icon>
              <select
                v-model="sortOrder"
                class="sort-select"
              >
                <option value="newest">최신순</option>
                <option value="oldest">오래된순</option>
              </select>
            </div>
          </div>
        </div>

        <!-- 그룹화된 메모 목록 -->
        <div class="memo-groups">
          <template v-for="(memos, group) in groupedMemos" :key="group">
            <div v-if="memos.length > 0" class="memo-group">
              <div class="group-header">
                <h3>
                  {{ getGroupTitle(group) }}
                  <span class="group-count">({{ memos.length }})</span>
                </h3>
              </div>

              <div class="memo-list">
                <MemoCard
                  v-for="memo in memos"
                  :key="memo.id"
                  :memo="memo"
                  @update-status="handleUpdateMemoStatus"
                />
              </div>
            </div>
          </template>

          <!-- 결과가 없을 때 표시 -->
          <div v-if="!hasAnyMemos" class="no-results">
            <v-icon size="48" color="#9CA3AF">mdi-note-text-outline</v-icon>
            <p>등록된 알림이 없습니다</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 메모 입력 영역 -->
    <MemoInput
      v-if="isOpen"
      :workers="workerStore.workers"
      @close="isOpen = false"
      @submit="handleMemoSubmit"
    />

    <!-- 새 메모 작성 버튼 -->
    <button
      v-if="!isOwner && !isOpen"
      class="new-memo-btn"
      @click="isOpen = true"
    >
      <v-icon>mdi-pencil</v-icon>
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import _ from 'lodash'
import { useMemberStore } from '@/store/member/memberStore'
import { useMemoStore } from '@/store/memo/memoStore'
import { useWorkerStore } from '@/store/worker/workerStore'
import MemoCard from '@/components/memo/MemoCard.vue'
import MemoInput from '@/components/memo/MemoInput.vue'

// Store 초기화
const memoStore = useMemoStore()
const workerStore = useWorkerStore()
const memberStore = useMemberStore()
const { isOwner } = storeToRefs(memberStore)

// 로컬 상태 관리
const isOpen = ref(false)
const activeTab = ref('received')
const searchQuery = ref('')
const statusFilter = ref('ALL')
const sortOrder = ref('newest')

// 계산된 속성들
const baseFilteredMemos = computed(() => {
  let memos = activeTab.value === 'received'
    ? memoStore.receivedMemos
    : memoStore.sentMemos

  if (statusFilter.value === 'ALL') {
    return memos
  }
  
  return memos.filter(memo => memo.status === statusFilter.value)
})

const searchedMemos = computed(() => {
  if (!searchQuery.value) return baseFilteredMemos.value

  const query = searchQuery.value.toLowerCase()
  return baseFilteredMemos.value.filter(memo =>
    memo.content.toLowerCase().includes(query) ||
    memo.creator.toLowerCase().includes(query)
  )
})

const sortedMemos = computed(() => {
  return [...searchedMemos.value].sort((a, b) => {
    if (sortOrder.value === 'newest') {
      return new Date(b.createdAt) - new Date(a.createdAt)
    }
    return new Date(a.createdAt) - new Date(b.createdAt)
  })
})

const groupedMemos = computed(() => {
  return _.groupBy(sortedMemos.value, memo => {
    const date = new Date(memo.createdAt)
    const today = new Date()
    const yesterday = new Date(today)
    yesterday.setDate(yesterday.getDate() - 1)

    if (date.toDateString() === today.toDateString()) return 'today'
    if (date.toDateString() === yesterday.toDateString()) return 'yesterday'
    return 'older'
  })
})

const hasAnyMemos = computed(() => {
  return Object.values(groupedMemos.value).some(memos => memos.length > 0)
})

const receivedCount = computed(() => {
  return memoStore.receivedMemos.length
})

const sentCount = computed(() => {
  return memoStore.sentMemos.length
})

const totalCount = computed(() => {
  return activeTab.value === 'received' 
    ? memoStore.receivedMemos.length 
    : memoStore.sentMemos.length
})

const inProgressCount = computed(() => {
  const memos = activeTab.value === 'received' 
    ? memoStore.receivedMemos 
    : memoStore.sentMemos
  return memos.filter(memo => memo.status === 'NOT_DONE').length
})

const completedCount = computed(() => {
  const memos = activeTab.value === 'received' 
    ? memoStore.receivedMemos 
    : memoStore.sentMemos
  return memos.filter(memo => memo.status === 'DONE').length
})

// 메서드들
const getGroupTitle = (group) => {
  switch(group) {
    case 'today': return '오늘'
    case 'yesterday': return '어제'
    default: return '이전'
  }
}

const handleStatusFilterChange = (status) => {
  statusFilter.value = status
}

const switchTab = (tab) => {
  activeTab.value = tab
  searchQuery.value = ''
  sortOrder.value = 'newest'
  statusFilter.value = 'ALL'
}

const handleMemoSubmit = async (memoData) => {
  try {
    const response = await memoStore.addMemoAction(memoData)
    if (response) {
      isOpen.value = false
      await refreshMemos()
    }
  } catch (error) {
    console.error('Error submitting memo:', error)
  }
}

const handleUpdateMemoStatus = async (memo) => {
  try {
    const success = await memoStore.updateMemoStatusAction(memo)
    if (success) {
      await refreshMemos()
    }
  } catch (error) {
    console.error('Error updating memo status:', error)
  }
}

const refreshMemos = async () => {
  try {
    if (!isOwner.value) {
      await memoStore.fetchMemos()
    } else {
      await memoStore.fetchAllMemos()
    }
  } catch (error) {
    console.error('Failed to refresh memos:', error)
  }
}

// 라이프사이클 훅
onMounted(async () => {
  try {
    await workerStore.fetchAllWorkers()
    await refreshMemos()
  } catch (error) {
    console.error('Failed to load initial data:', error)
  }
})
</script>

<style scoped>
.memo-page {
  min-height: calc(100vh - 120px);
  background: linear-gradient(135deg, #ffffff, #f0f2f5);
  position: relative;
}

.main-wrapper {
  display: flex;
  min-height: calc(100vh - 180px);
  max-width: 1400px;
  margin: 0 auto;
  padding: 2rem;
}

.memo-folders {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

/* 상태 섹션 타이틀 */
.status-section h2 {
  font-size: 0.688rem; /* 11px */
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: 0.75rem;
  padding-left: 0.75rem;
  letter-spacing: 0.5px;
}

/* 섹션 구분선 */
.status-section {
  margin-top: 1.25rem;
  padding-top: 1.25rem;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
}

/* 상태 폴더 아이템 간격 조정 */
.status-section .folder-item {
  margin-bottom: 0.25rem;
}


.sort-select-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.sort-icon {
  position: absolute;
  left: 0.75rem;
  color: var(--neutral-dark);
}

.sort-select {
  padding: 0.75rem 2.5rem 0.75rem 2.5rem;
  border: 1px solid var(--neutral);
  border-radius: 8px;
  background: white;
  color: var(--primary);
  min-width: 120px;
  cursor: pointer;
  font-size: 0.875rem;
  appearance: none;
}

.sidebar {
  width: 240px; /* 너비 약간 줄임 */
  background-color: var(--primary);
  height: fit-content;
  position: sticky;
  top: 120px;
  padding: 1.25rem; /* 패딩 살짝 줄임 */
  border-radius: 16px;
  margin-right: 2rem;
  color: white;
}

/* 메시지함 타이틀 */
.sidebar h1 {
  font-size: 0.875rem; /* 14px */
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 1.25rem;
  padding-left: 0.5rem;
}

/* 폴더 아이템 스타일 개선 */
.folder-item {
  display: flex;
  align-items: center;
  gap: 0.75rem; /* 간격 살짝 줄임 */
  padding: 0.625rem 0.75rem; /* 패딩 조정 */
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.813rem; /* 13px */
}

/* 아이콘 색상 통일 */
.folder-item .v-icon {
  color: rgba(255, 255, 255, 0.9);
}

.folder-item:hover {
  background-color: rgba(255, 255, 255, 0.08);
}

.folder-item.active {
  background-color: rgba(255, 255, 255, 0.12);
}

/* 폴더 카운트 배지 개선 */
.folder-count {
  margin-left: auto;
  background-color: rgba(249, 177, 122, 0.9); /* var(--accent) with opacity */
  color: var(--primary-dark);
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-size: 0.75rem; /* 12px */
  font-weight: 500;
  min-width: 1.5rem;
  text-align: center;
}

.folder-count.selected {
  background-color: var(--primary);
  color: var(--neutral-light);
}

.main-content {
  flex: 1;
  max-width: calc(100% - 280px);
  margin: 0 auto;
}

.sidebar:not(:has(*)) + .main-content,
.main-wrapper:not(:has(.sidebar)) .main-content {
  max-width: 800px;
  margin: 0 auto;
}

.status-tabs {
  margin-bottom: 1rem;
  background: white;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.tab-container {
  display: flex;
  gap: 0.5rem;
}

.tab-item {
  padding: 0.625rem 1.25rem;
  border-radius: 8px;
  font-size: 0.875rem;
  color: #6b7280;
  cursor: pointer;
  border: none;
  background: none;
  transition: all 0.2s ease;
}

.tab-item:hover {
  background-color: #f3f4f6;
}

.tab-item.active {
  background-color: var(--primary);
  color: white;
  font-weight: 500;
}

.content-header {
  margin: 1rem 0 2rem;
  background: white;
  padding: 1rem;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.search-container {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.search-input {
  background-color: var(--neutral-light);
  border: 1px solid var(--neutral);
  border-radius: 8px;
  padding: 0.75rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.search-input input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 0.875rem;
}

.filter-select {
  padding: 0.5rem 1rem;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background: white;
  font-size: 0.875rem;
  color: #374151;
  cursor: pointer;
  min-width: 120px;
}

.memo-groups {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.memo-group {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.group-header {
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #f3f4f6;
}

.group-header h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #374151;
}

.group-count {
  color: #6b7280;
  font-weight: normal;
  margin-left: 0.5rem;
}

.memo-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
}

.no-results {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
  color: #9ca3af;
  text-align: center;
}

.no-results p {
  margin-top: 1rem;
  font-size: 0.875rem;
}

.new-memo-btn {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: var(--primary);
  color: white;
  border: none;
  cursor: pointer;
  box-shadow: 0 4px 12px var(--primary-light);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  z-index: 1000;
}

.new-memo-btn:hover {
  background-color: var(--primary-dark);
  transform: scale(1.05);
  box-shadow: 0 6px 16px var(--primary-light);
}

@media (max-width: 1024px) {
  .sidebar {
    width: 240px;
  }

  .main-content {
    max-width: calc(100% - 240px);
  }

  .memo-list {
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .main-wrapper {
    padding: 1rem;
  }

  .sidebar {
    display: none;
  }

  .main-content {
    max-width: 800px;
    margin: 0 auto;
  }

  .tab-container {
    overflow-x: auto;
    padding-bottom: 0.5rem;
  }

  .tab-item {
    white-space: nowrap;
  }

  .search-container {
    flex-direction: column;
  }

  .filter-select {
    width: 100%;
  }

  .memo-list {
    grid-template-columns: 1fr;
  }

  .new-memo-btn {
    right: 1rem;
    bottom: 1rem;
  }
}

@media (max-width: 480px) {
  .main-wrapper {
    padding: 0.5rem;
  }

  .content-header,
  .memo-group {
    padding: 1rem;
  }

  .status-tabs {
    padding: 0.75rem;
  }

  .tab-item {
    padding: 0.5rem 1rem;
  }
}
</style>