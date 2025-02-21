<template>
  <div class="schedule-page">
    <div class="schedule-header">
      <div class="view-controls">
        <button
          class="view-button"
          :class="{ active: viewMode === 'month' }"
          @click="viewMode = 'month'"
        >
          월간
        </button>
        <button
          v-if="memberStore.isWorker"
          class="view-button"
          :class="{ active: viewMode === 'week' }"
          @click="viewMode = 'week'"
        >
          주간
        </button>
      </div>

      <div class="header-controls">
        <button
          v-if="memberStore.isOwner"
          class="icon-button"
          @click="openCreateModal"
        >
          <Plus class="w-6 h-6" />
        </button>
        <button
          class="icon-button"
          @click="handleSearch"
        >
          <Search class="w-6 h-6" />
        </button>
      </div>
    </div>

    <!-- 달력 뷰 -->
    <div class="calendar-container">
      <Calendar
        v-if="viewMode === 'month'"
        :current-user-id="currentUserId"
        :selected-date="selectedDate"
        @select-date="handleDateSelect"
        @request-substitute="handleSubstituteRequest"
      />
      <WeekCalendar
        v-if="viewMode === 'week' && memberStore.isWorker"
        :current-user-id="currentUserId"
        :selected-date="selectedDate"
        @select-date="handleDateSelect"
        @request-substitute="handleSubstituteRequest"
      />
    </div>

    <CreateScheduleModal
      v-if="isCreateModalOpen"
      mode="register"
      :selectedDate="selectedDate || new Date()"
      @close="closeCreateModal"
      @create="handleScheduleCreate"
      ref="scheduleModalRef"
    />

    <!-- 검색 모달 -->
    <SearchModal
      v-if="isSearchModalOpen"
      @close="closeSearchModal"
      @select-date="handleSearchDateSelect"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { useMemberStore } from '@/store/member/memberStore';
import Calendar from '@/views/Calendar/Calendar.vue';
import WeekCalendar from '@/views/Schedule/WeekCalendar.vue';
import CreateScheduleModal from '@/views/Schedule/ScheduleModal.vue';
import SearchModal from '@/views/Schedule/SearchModal.vue';
import { Plus, Search } from 'lucide-vue-next';
import { formatDate } from '@/common/apis/utils/date';

const memberStore = useMemberStore();
const scheduleStore = useScheduleStore();
const viewMode = ref('month');
const selectedDate = ref(null);
const currentUserId = ref(memberStore.user?.workerId);
const isCreateModalOpen = ref(false);
const isSearchModalOpen = ref(false);
const scheduleModalRef = ref(null);

// 사장님이면 항상 월간 뷰로 설정
watch(() => memberStore.isOwner, (isOwner) => {
  if (isOwner) {
    viewMode.value = 'month';
  }
});

onMounted(async () => {
  try {
    // 사용자 정보를 먼저 불러옴
    await memberStore.fetchUserInfo();

    // 사용자 정보가 로드된 후 ID 설정
    currentUserId.value = memberStore.user?.id;

    if (memberStore.isOwner) {
      viewMode.value = 'month';
    }

    await scheduleStore.fetchAllSchedules();
  } catch (error) {
    console.error('데이터 로드 실패:', error);
  }
});

const openCreateModal = () => {
  isCreateModalOpen.value = true;
};

const closeCreateModal = () => {
  isCreateModalOpen.value = false;
};

const handleSearch = () => {
  isSearchModalOpen.value = true;
};

const closeSearchModal = () => {
  isSearchModalOpen.value = false;
};

const handleSearchDateSelect = (date) => {
  selectedDate.value = date;
  closeSearchModal();
};

const handleScheduleCreate = async (scheduleData) => {
  try {
    await scheduleStore.createSchedule(scheduleData);
    closeCreateModal();
    await scheduleStore.fetchAllSchedules();
  } catch (error) {
    console.error('스케줄 생성 실패:', error);
    alert('스케줄 생성 중 오류가 발생했습니다.');
  }
};

const handleDateSelect = (date) => {
  selectedDate.value = date;
};

const handleSubstituteRequest = async (scheduleId) => {
  try {
    await scheduleStore.requestSubstituteSchedule(scheduleId);
    await scheduleStore.fetchAllSchedules();
  } catch (error) {
    console.error('대타 신청 실패:', error);
    alert('대타 신청 중 오류가 발생했습니다.');
  }
};
</script>

<style scoped>
.schedule-page {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.schedule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.view-controls {
  display: flex;
  gap: 1rem;
}

.view-button {
  padding: 0.5rem 1.5rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.view-button.active {
  background: var(--primary);
  color: white;
  border-color: var(--primary);
}

.header-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.icon-button {
  padding: 0.5rem;
  border: none;
  border-radius: 8px;
  background: var(--primary);
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon-button:hover {
  background: var(--primary-dark);
}

.confirm-button {
  padding: 0.5rem 1.5rem;
  border: none;
  border-radius: 8px;
  background: #07702ad2;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.confirm-button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.confirm-button:hover:not(:disabled) {
  background: #045a21;
}

.calendar-container {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

@media (max-width: 768px) {
  .schedule-page {
    padding: 1rem;
  }

  .schedule-header {
    flex-direction: column;
    gap: 1rem;
  }

  .view-controls {
    width: 100%;
    justify-content: center;
  }

  .header-controls {
    width: 100%;
    justify-content: center;
    margin-top: 1rem;
  }

  .calendar-container {
    padding: 1rem;
  }

  .create-button,
  .confirm-button {
    flex: 1;
  }
}
</style>
