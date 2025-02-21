<template>
  <div class="memo-input-overlay">
    <div class="memo-input-container">
      <!-- 축소된 입력 필드 -->
      <div v-if="!isExpanded" class="input-box">
        <input
          v-model="content"
          placeholder="메모 내용을 입력해주세요..."
          class="memo-input"
          @focus="isExpanded = true"
        />
        <button @click="$emit('close')" class="close-btn">
          <v-icon>mdi-close</v-icon>
        </button>
      </div>

      <!-- 확장된 입력 영역 -->
      <div v-else class="expanded-area">
        <!-- 작성자 선택 영역 -->
        <div class="worker-list-container">
          <div class="worker-list">
            <!-- 전체 선택 버튼 -->
            <button 
              class="worker-item"
              :class="{ 'is-selected': isAllSelected }"
              @click="selectAll"
            >
              <div class="worker-avatar">
                <span>All</span>
              </div>
              <span class="worker-name">전체</span>
            </button>

            <!-- 개별 작성자 선택 -->
            <button
              v-for="worker in workers"
              :key="worker.workerId"
              class="worker-item"
              :class="{ 'is-selected': selectedWorkers.includes(worker.workerId) }"
              @click="toggleWorker(worker)"
            >
              <div class="worker-avatar">
                <span>{{ worker.name[0] }}</span>
              </div>
              <span class="worker-name">{{ worker.name }}</span>
            </button>
          </div>
        </div>

        <!-- 메모 입력 영역 -->
        <textarea
          v-model="content"
          placeholder="메모 내용을 입력해주세요..."
          class="memo-textarea"
          rows="4"
        ></textarea>

        <!-- 작성 버튼 영역 -->
        <div class="button-group">
          <button 
            class="cancel-btn" 
            @click="$emit('close')"
          >
            취소
          </button>
          <button
            class="submit-btn"
            :disabled="!isValid"
            @click="handleSubmit"
          >
            작성하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  workers: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['close', 'submit'])

// 상태 관리
const isExpanded = ref(false)
const content = ref('')
const selectedWorkers = ref([])

// 계산된 속성
const isAllSelected = computed(() => {
  const allWorkerIds = props.workers.map(w => w.workerId)
  return allWorkerIds.every(id => selectedWorkers.value.includes(id))
})

const isValid = computed(() => {
  return content.value.trim().length > 0 && selectedWorkers.value.length > 0
})

// 메서드
const selectAll = () => {
  if (isAllSelected.value) {
    selectedWorkers.value = []
  } else {
    selectedWorkers.value = props.workers.map(w => w.workerId)
  }
}

const toggleWorker = (worker) => {
  const index = selectedWorkers.value.indexOf(worker.workerId)
  if (index === -1) {
    selectedWorkers.value.push(worker.workerId)
  } else {
    selectedWorkers.value.splice(index, 1)
  }
}

const handleSubmit = () => {
  if (!isValid.value) return

  emit('submit', {
    content: content.value,
    workers: selectedWorkers.value
  })

  // 초기화
  content.value = ''
  selectedWorkers.value = []
  isExpanded.value = false
}
</script>

<style scoped>
.memo-input-overlay {
  position: fixed;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  max-width: 600px;
  z-index: 1000;
}

.memo-input-container {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(66, 71, 105, 0.15);
}

.input-box {
  display: flex;
  align-items: center;
  padding: 1rem;
  gap: 0.5rem;
}

.memo-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 0.875rem;
  padding: 0.5rem;
}

.expanded-area {
  padding: 1.5rem;
}

.worker-list-container {
  margin-bottom: 1rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #e5e7eb;
}

.worker-list {
  display: flex;
  gap: 1rem;
  overflow-x: auto;
  padding: 0.5rem;
}

.worker-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  border: none;
  background: none;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.worker-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
  color: #4b5563;
  transition: all 0.2s ease;
}

.worker-name {
  font-size: 0.75rem;
  color: #4b5563;
}

.worker-item.is-selected {
  background-color: rgba(66, 71, 105, 0.1);
}

.worker-item.is-selected .worker-avatar {
  background-color: #424769;
  color: white;
}

.memo-textarea {
  width: 100%;
  resize: none;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 0.75rem;
  font-size: 0.875rem;
  line-height: 1.5;
  margin-bottom: 1rem;
}

.memo-textarea:focus {
  outline: none;
  border-color: #424769;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.2);
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 0.75rem;
}

.cancel-btn, .submit-btn {
  padding: 0.5rem 1rem;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.cancel-btn {
  background: none;
  border: 1px solid #e5e7eb;
  color: #4b5563;
}

.submit-btn {
  background-color: #424769;
  border: none;
  color: white;
}

.submit-btn:disabled {
  background-color: #9E9E9E;
  cursor: not-allowed;
}

.close-btn {
  padding: 0.5rem;
  border-radius: 50%;
  border: none;
  background: none;
  color: #6b7280;
  cursor: pointer;
}

.close-btn:hover {
  background-color: #f3f4f6;
}

@media (max-width: 640px) {
  .memo-input-overlay {
    bottom: 0;
    width: 100%;
    max-width: none;
  }

  .memo-input-container {
    border-radius: 16px 16px 0 0;
  }

  .button-group {
    position: sticky;
    bottom: 0;
    background: white;
    padding-top: 0.75rem;
  }
}
</style>