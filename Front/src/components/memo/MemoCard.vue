<template>
  <div class="memo-card" :class="{ 'completed': isCompleted }">
    <div class="memo-header">
      <div class="user-info">
        <div class="checkbox-wrapper">
          <input 
            type="checkbox"
            :checked="isCompleted"
            @click.stop="openStatusModal"
            :disabled="isCompleted"
            class="status-checkbox"
          />
          <span class="creator-name">{{ memo.creator }}</span>
        </div>
      </div>
      <span class="timestamp">{{ formattedDate }}</span>
    </div>

    <div class="memo-content" :class="{ 'completed': isCompleted }">
      {{ memo.content }}
    </div>

    <div class="memo-footer">
      <div class="status-badge" :class="memo.status.toLowerCase()">
        {{ isCompleted ? '완료' : '진행중' }}
      </div>
    </div>

    <MemoStatusModal
      v-if="isModalOpen"
      :memo="memo"
      @close="closeStatusModal"
      @update-status="updateStatus"
    />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import MemoStatusModal from './MemoStatusModal.vue'

const props = defineProps({
  memo: {
    type: Object,
    required: true,
    validator: (memo) => {
      return ['id', 'content', 'creator', 'status', 'createdAt'].every(key => key in memo)
    }
  }
})

const emit = defineEmits(['update-status'])

// 모달 상태 관리
const isModalOpen = ref(false)

const isCompleted = computed(() => props.memo.status === 'DONE')

const formattedDate = computed(() => {
  const date = new Date(props.memo.createdAt)
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
    hour12: true
  }).format(date)
})

// 모달 관련 메서드
const openStatusModal = () => {
  if (!isCompleted.value) {
    isModalOpen.value = true
  }
}

const closeStatusModal = () => {
  isModalOpen.value = false
}

const updateStatus = async (memo) => {
  emit('update-status', memo)
  closeStatusModal()
}
</script>

<style scoped>
.memo-card {
  background: white;
  border-radius: 12px;
  padding: 1.25rem;
  box-shadow: 0 1px 3px rgba(66, 71, 105, 0.1);
  transition: all 0.2s ease;
}

.memo-card.completed {
  opacity: 0.8;
}

.memo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.checkbox-wrapper {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.status-checkbox {
  width: 18px;
  height: 18px;
  border-radius: 4px;
  cursor: pointer;
  accent-color: #424769;
}

.status-checkbox:disabled {
  cursor: default;
}

.creator-name {
  font-size: 0.875rem;
  font-weight: 500;
  color: #374151;
}

.timestamp {
  font-size: 0.75rem;
  color: #6b7280;
}

.memo-content {
  color: #374151;
  font-size: 0.875rem;
  line-height: 1.5;
  margin-bottom: 1rem;
  word-break: break-word;
  white-space: pre-line;
  margin-left: 2.5rem;
}

.memo-content.completed {
  color: #6B7280;
  text-decoration: line-through;
}

.memo-footer {
  display: flex;
  justify-content: flex-start;
  border-top: 1px solid #f3f4f6;
  padding-top: 1rem;
  margin-left: 2.5rem;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-badge.done {
  background-color: rgba(66, 71, 105, 0.1);
  color: #424769;
}

.status-badge.not_done {
  background-color: #f3f4f6;
  color: #374151;
}

@media (max-width: 640px) {
  .memo-card {
    padding: 1rem;
  }

  .memo-content {
    font-size: 0.813rem;
  }

  .timestamp {
    font-size: 0.688rem;
  }
}
</style>