<template>
  <div class="worker-selection-panel" :class="{ 'is-open': isOpen }">
    <div class="panel-header">
      <div class="selection-actions">
        <button class="action-btn" @click="selectNextWorkers">
          <Calendar class="icon" :size="18" />
          <span>다음 근무자</span>
        </button>
        <button class="action-btn" @click="selectAllWorkers">
          <Users class="icon" :size="18" />
          <span>전체 근무자</span>
        </button>
        <button class="action-btn" @click="toggleDropdown">
          <UserPlus class="icon" :size="18" />
          <span>근무자 선택</span>
        </button>
      </div>
    </div>

    <div>
      <!-- 드롭다운 오버레이 -->
      <div v-if="dropdownOpen" class="dropdown-overlay" @click.self="closeDropdown">
        <div class="dropdown">
          <div class="dropdown-header">
            <div class="header-title">
              <UserPlus class="header-icon" :size="20" />
              <h4>근무자 선택</h4>
            </div>
            <button class="close-icon-btn" @click="closeDropdown">
              <X :size="20" />
            </button>
          </div>
          <div class="dropdown-content">
            <div class="search-box">
              <div class="search-input-wrapper">
                <Search class="search-icon" :size="18" />
                <input
                  type="text"
                  v-model="searchQuery"
                  placeholder="이름 검색"
                  class="dropdown-search"
                />
              </div>
            </div>
            <div class="dropdown-options">
              <div
                v-for="worker in filteredWorkers"
                :key="worker.workerId"
                class="dropdown-option"
                :class="{ 'selected': selectedWorkerIds.includes(worker.workerId) }"
                @click="toggleWorkerSelection(worker)"
              >
                {{ worker.name }}
              </div>
            </div>
            <button class="close-btn" @click="closeDropdown">완료</button>
          </div>
        </div>
      </div>
    </div>

    <div class="selected-summary" v-if="selectedWorkerIds.length > 0">
      <div class="selected-count">
        <CheckCircle2 class="count-icon" :size="16" />
        <span>{{ selectedWorkerIds.length }}명 선택됨</span>
      </div>
      <button class="clear-btn" @click="clearSelection">
        <RefreshCw :size="16" />
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useWorkerStore } from '@/store/worker/workerStore';
import { Search, Calendar, Users, UserPlus, X, RefreshCw, CheckCircle2 } from 'lucide-vue-next';

const props = defineProps({
  isOpen: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:selectedWorkers']);

const workerStore = useWorkerStore();
const dropdownOpen = ref(false);
const searchQuery = ref('');
const selectedWorkerIds = ref([]);

// 워커스토어에서 workers 가져오기
const workers = computed(() => workerStore.workers || []);

// 필터링된 워커 목록 (검색 적용)
const filteredWorkers = computed(() => {
  return workers.value.filter(worker =>
    worker.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 드롭다운 열기/닫기
const toggleDropdown = () => {
  dropdownOpen.value = !dropdownOpen.value;
};

// 드롭다운 닫기
const closeDropdown = () => {
  dropdownOpen.value = false;
};

// 워커 선택 토글
const toggleWorkerSelection = (worker) => {
  console.log('Toggling worker:', worker);
  const index = selectedWorkerIds.value.indexOf(worker.workerId);
  if (index === -1) {
    selectedWorkerIds.value.push(worker.workerId);
  } else {
    selectedWorkerIds.value.splice(index, 1);
  }
  console.log('Selected workers:', selectedWorkerIds.value);
  emit('update:selectedWorkers', selectedWorkerIds.value);
};

// 선택 초기화
const clearSelection = () => {
  selectedWorkerIds.value = [];
  emit('update:selectedWorkers', []);
};

// 전체 근무자 모두 선택
const selectAllWorkers = async () => {
  try {
    await workerStore.fetchAllWorkers();
    const workerIds = workers.value.map(worker => worker.workerId);
    selectedWorkerIds.value = selectedWorkerIds.value.length === workerIds.length ? [] : workerIds;
    emit('update:selectedWorkers', selectedWorkerIds.value);
  } catch (error) {
    console.error('Failed to fetch all workers:', error);
  }
};

// 다음 근무자 모두 선택
const selectNextWorkers = async () => {
  try {
    await workerStore.fetchNextWorkers();
    const nextWorkerIds = workerStore.nextWorkers.map(worker => worker.workerId);
    const allNextWorkersSelected = nextWorkerIds.every(id =>
      selectedWorkerIds.value.includes(id)
    );

    if (allNextWorkersSelected) {
      selectedWorkerIds.value = selectedWorkerIds.value.filter(id => !nextWorkerIds.includes(id));
    } else {
      selectedWorkerIds.value = [...new Set([...selectedWorkerIds.value, ...nextWorkerIds])];
    }
    emit('update:selectedWorkers', selectedWorkerIds.value);
  } catch (error) {
    console.error('Failed to fetch next workers:', error);
  }
};

// workers 변경 감지
watch(workers, (newWorkers) => {
  console.log('Workers updated:', newWorkers);
});

// 컴포넌트 마운트 시 워커 목록 가져오기
onMounted(async () => {
  try {
    await workerStore.fetchAllWorkers();
    console.log('Loaded workers:', workers.value);
  } catch (error) {
    console.error('Failed to load workers:', error);
  }
});
</script>

<style scoped>
.worker-selection-panel {
  border-radius: 12px;
  padding: 1.25rem;
  position: relative;
  z-index: 1;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.panel-header {
  margin-bottom: 1.25rem;
}

.selection-actions {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.action-btn {
  padding: 0.625rem 1.25rem;
  background-color: #F5F5F5;  /* 밝은 중립 색상 */
  color: #2D3250;  /* 진한 네이비 블루 */
  border: 1.5px solid #E0E0E0;  /* 중립 색상 */
  border-radius: 12px;
  cursor: pointer;
  font-size: 0.925rem;
  font-weight: 600;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 120px;
  gap: 0.5rem;
}

.icon {
  color: currentColor;
}

.action-btn:hover {
  background-color: #2D3250;  /* 진한 네이비 블루 */
  color: white;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(45, 50, 80, 0.15);
}

.action-btn:active {
  transform: translateY(0);
}

.dropdown-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(4px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.dropdown {
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 16px 32px rgba(0, 0, 0, 0.1);
  width: 450px;
  max-height: 600px;
  overflow-y: auto;
  z-index: 10000;
  animation: fadeIn 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  padding: 1.5rem;
}

.dropdown-header {
  padding: 1rem 1.25rem;
  background-color: white;
  color: #2D3250;  /* 진한 네이비 블루 */
  border-top-left-radius: 16px;
  border-top-right-radius: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #E0E0E0;  /* 중립 색상 */
  margin-bottom: 1rem;
}

.dropdown-header h4 {
  font-size: 1.125rem;
  font-weight: 600;
  color: #2D3250;  /* 진한 네이비 블루 */
}

.dropdown-content {
  padding: 0.5rem;
}

.dropdown-search {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #E0E0E0;  /* 중립 색상 */
  border-radius: 12px;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  background-color: #F5F5F5;  /* 밝은 중립 색상 */
}

.dropdown-search:focus {
  border-color: #F9B17A;  /* 따뜻한 오렌지 */
  outline: none;
  background-color: white;
  box-shadow: 0 2px 8px rgba(249, 177, 122, 0.1);
}

.dropdown-options {
  margin-top: 1.25rem;
  display: grid;
  gap: 0.5rem;
  max-height: 350px;
  overflow-y: auto;
  padding: 0.5rem;
}

.dropdown-options::-webkit-scrollbar {
  width: 6px;
}

.dropdown-options::-webkit-scrollbar-track {
  background: #F5F5F5;  /* 밝은 중립 색상 */
  border-radius: 3px;
}

.dropdown-options::-webkit-scrollbar-thumb {
  background: #E0E0E0;  /* 중립 색상 */
  border-radius: 3px;
}

.dropdown-option {
  padding: 0.75rem 1rem;
  cursor: pointer;
  border-radius: 10px;
  transition: all 0.2s ease;
  font-size: 0.95rem;
  color: #424769;  /* 중간 톤 네이비 블루 */
  background: #F5F5F5;  /* 밝은 중립 색상 */
  border: 1px solid transparent;
  display: flex;
  align-items: center;
}

.dropdown-option:hover {
  background-color: #E0E0E0;  /* 중립 색상 */
  border-color: #424769;  /* 중간 톤 네이비 블루 */
  transform: translateX(2px);
}

.dropdown-option.selected {
  background-color: #2D3250;  /* 진한 네이비 블루 */
  color: white;
  border-color: #424769;  /* 중간 톤 네이비 블루 */
  font-weight: 500;
}

.close-btn {
  padding: 0.75rem 1.5rem;
  background-color: #2D3250;  /* 진한 네이비 블루 */
  color: white;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 600;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  align-self: flex-end;
  margin-top: 1.5rem;
  box-shadow: 0 4px 12px rgba(45, 50, 80, 0.2);
}

.close-btn:hover {
  background-color: #424769;  /* 중간 톤 네이비 블루 */
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(45, 50, 80, 0.25);
}

.close-btn:active {
  transform: translateY(0);
}

.clear-btn {
  padding: 0.625rem;
  background-color: #F5F5F5;  /* 밝은 중립 색상 */
  color: #424769;  /* 중간 톤 네이비 블루 */
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.clear-btn:hover {
  background-color: #E0E0E0;  /* 중립 색상 */
  color: #2D3250;  /* 진한 네이비 블루 */
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.selected-summary {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}
</style>