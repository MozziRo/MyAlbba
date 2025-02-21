<!-- DayDetail.vue -->
<template>
  <div class="day-detail">
    <div class="detail-header">
      <div class="detail-date">
        <h3>{{ formatDetailDate(date) }}</h3>
        <span class="schedule-count">{{ daySchedules.length }}개의 일정</span>
      </div>
      <div class="header-actions">
        <button
          v-if="memberStore.isOwner"
          class="add-button"
          @click="handleAdd"
        >
          <span class="plus-icon">+</span>
          <span class="button-text">스케줄 추가</span>
        </button>
        <button class="close-button" @click="$emit('close')">×</button>
      </div>
    </div>

    <div class="detail-content">
      <!-- 오전 섹션 -->
      <div class="time-section">
        <div class="section-header">
          <h4>오전 <span class="section-count">{{ morningSchedules.length }}개</span></h4>
        </div>
        <div class="schedule-list">
          <div
            v-for="schedule in morningSchedules"
            :key="schedule.confirmedScheduleId"
            class="schedule-item morning"
          >
            <div class="name-tag morning">
              {{ schedule.workerName.split(' ')[1] || schedule.workerName }}
            </div>
            <div class="time morning">
              {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
            </div>
            <div class="schedule-actions">
              <button
                v-if="memberStore.isOwner"
                class="action-button edit"
                @click="handleEdit(schedule)"
              >
                ✎
              </button>
              <button
                v-if="memberStore.isOwner"
                class="action-button delete"
                @click="handleDelete(schedule)"
              >
                ×
              </button>
              <!-- 내 스케줄일 경우 대타 신청 버튼 -->
              <button
                v-if="memberStore.isWorker && isMySchedule(schedule.confirmedScheduleId) && !hasSubstituteRequest(schedule.confirmedScheduleId)"
                class="substitute-request-button"
                @click="handleSubstituteRequest(schedule.confirmedScheduleId)"
              >
                대타 신청
              </button>
              <!-- 다른 사람의 대타 요청된 스케줄일 경우 대타 지원 버튼 -->
              <button
                v-if="memberStore.isWorker && !isMySchedule(schedule.confirmedScheduleId) && hasSubstituteRequest(schedule.confirmedScheduleId)"
                class="substitute-apply-button"
                @click="handleSubstituteApply(getSubstituteRequest(schedule.confirmedScheduleId)?.substituteScheduleId)"
              >
                대타 지원
              </button>
            </div>
          </div>
          <div v-if="!morningSchedules.length" class="empty-state">
            오전 일정이 없습니다
          </div>
        </div>
      </div>

      <!-- 오후 섹션 -->
      <div class="time-section">
        <div class="section-header">
          <h4>오후 <span class="section-count">{{ afternoonSchedules.length }}개</span></h4>
        </div>
        <div class="schedule-list">
          <div
            v-for="schedule in afternoonSchedules"
            :key="schedule.confirmedScheduleId"
            class="schedule-item afternoon"
          >
            <div class="name-tag afternoon">
              {{ schedule.workerName.split(' ')[1] || schedule.workerName }}
            </div>
            <div class="time afternoon">
              {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
            </div>
            <div class="schedule-actions">
              <button
                v-if="memberStore.isOwner"
                class="action-button edit"
                @click="handleEdit(schedule)"
              >
                ✎
              </button>
              <button
                v-if="memberStore.isOwner"
                class="action-button delete"
                @click="handleDelete(schedule)"
              >
                ×
              </button>
              <!-- 내 스케줄일 경우 대타 신청 버튼 -->
              <button
                v-if="memberStore.isWorker && isMySchedule(schedule.confirmedScheduleId) && !hasSubstituteRequest(schedule.confirmedScheduleId)"
                class="substitute-request-button"
                @click="handleSubstituteRequest(schedule.confirmedScheduleId)"
              >
                대타 신청
              </button>
              <!-- 다른 사람의 대타 요청된 스케줄일 경우 대타 지원 버튼 -->
              <button
                v-if="memberStore.isWorker && !isMySchedule(schedule.confirmedScheduleId) && hasSubstituteRequest(schedule.confirmedScheduleId)"
                class="substitute-apply-button"
                @click="handleSubstituteApply(getSubstituteRequest(schedule.confirmedScheduleId)?.substituteScheduleId)"
              >
                대타 지원
              </button>
            </div>
          </div>
          <div v-if="!afternoonSchedules.length" class="empty-state">
            오후 일정이 없습니다
          </div>
        </div>
      </div>
    </div>

    <ScheduleModal
      v-if="showModal"
      :initialSchedule="editingSchedule"
      :selectedDate="date"
      :mode="modalMode"
      @close="handleModalClose"
      @refresh="$emit('refresh')"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { useMemberStore } from '@/store/member/memberStore';
import { useWorkerStore } from '@/store/worker/workerStore';
import { formatTime, formatDetailDate, formatDate } from '@/common/apis/utils/date';
import ScheduleModal from '@/views/Schedule/ScheduleModal.vue';

const memberStore = useMemberStore();
const workerStore = useWorkerStore();
const scheduleStore = useScheduleStore();
const showModal = ref(false);
const editingSchedule = ref(null);
const modalMode = ref('add');

const props = defineProps({
  date: {
    type: Date,
    required: true
  }
});

const emit = defineEmits(['close', 'refresh']);

// Computed
const daySchedules = computed(() => {
  const selectedDate = formatDate(props.date);
  return scheduleStore.schedules.filter(schedule => schedule.date === selectedDate);
});

const morningSchedules = computed(() => getSortedSchedules(schedule => parseInt(schedule.startTime.split(':')[0]) < 12));
const afternoonSchedules = computed(() => getSortedSchedules(schedule => parseInt(schedule.startTime.split(':')[0]) >= 12));

// Methods
const getSortedSchedules = (filterFn) => {
  return daySchedules.value
    .filter(filterFn)
    .sort((a, b) => {
      const timeA = a.startTime.split(':').map(Number);
      const timeB = b.startTime.split(':').map(Number);
      if (timeA[0] !== timeB[0]) return timeA[0] - timeB[0];
      return timeA[1] - timeB[1];
    });
};

const handleAdd = () => {
  editingSchedule.value = null;
  modalMode.value = 'add';
  showModal.value = true;
};

const handleEdit = (schedule) => {
  editingSchedule.value = { ...schedule };
  modalMode.value = 'edit';
  showModal.value = true;
};

const handleDelete = async (schedule) => {
  if (confirm('정말로 이 스케줄을 삭제하시겠습니까?')) {
    try {
      await scheduleStore.deleteConfirmedSchedule(schedule.confirmedScheduleId);
      emit('refresh');
    } catch (error) {
      console.error('스케줄 삭제 실패:', error);
      alert('스케줄 삭제에 실패했습니다.');
    }
  }
};

const handleModalClose = () => {
  showModal.value = false;
  editingSchedule.value = null;
};

const handleSubstituteRequest = async (confirmedScheduleId) => {
  try {
    await scheduleStore.requestSubstituteSchedule(confirmedScheduleId);
    alert('대타 신청이 완료되었습니다.');

    // 데이터 즉시 새로고침
    await Promise.all([
      scheduleStore.fetchAllSchedules(),
      scheduleStore.fetchSubstituteRequests()
    ]);

    emit('refresh');
  } catch (error) {
    console.error('대타 신청 실패:', error);
    alert('대타 신청에 실패했습니다.');
  }
};

const handleSubstituteApply = async (substituteScheduleId) => {
  if (!substituteScheduleId) {
    alert('대타 요청 정보를 찾을 수 없습니다.');
    return;
  }
  try {
    await scheduleStore.applyToSubstitute(substituteScheduleId);
    alert('대타 지원이 완료되었습니다.');

    // 데이터 즉시 새로고침
    await Promise.all([
      scheduleStore.fetchAllSchedules(),
      scheduleStore.fetchSubstituteRequests()
    ]);

    emit('refresh');
  } catch (error) {
    console.error('대타 지원 실패:', error);
    alert('대타 지원에 실패했습니다.');
  }
};

const isMySchedule = (confirmedScheduleId) => {
  const schedule = scheduleStore.schedules.find(s => s.confirmedScheduleId === confirmedScheduleId);
  return schedule?.workerId === workerStore.worker?.workerId;
};

const hasSubstituteRequest = (confirmedScheduleId) => {
  return scheduleStore.substituteRequests.some(request =>
    request.confirmedScheduleId === confirmedScheduleId
  );
};

const getSubstituteRequest = (confirmedScheduleId) => {
  return scheduleStore.substituteRequests.find(request =>
    request.confirmedScheduleId === confirmedScheduleId
  );
};

onMounted(async () => {
  if (memberStore.isWorker) {
    await workerStore.fetchWorkerInfo();
  }
});
</script>

<style scoped>
.day-detail {
  padding: 2rem;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(66, 71, 105, 0.08);
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid rgba(66, 71, 105, 0.1);
}

.detail-date h3 {
  font-size: 1.5rem;
  font-weight: 600;
  color: #2D3250;
  margin: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.add-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: #424769;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.add-button:hover {
  background: #2D3250;
  transform: translateY(-1px);
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
}

.time-section {
  position: relative;
  margin-bottom: 0.5rem;
  padding: 0.5rem;
  border-radius: 12px;
  background: #FAFBFF;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.section-header h4 {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2D3250;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin: 0;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.schedule-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  border-radius: 8px;
  gap: 1rem;
}

 .schedule-item.afternoon{
  background:  rgba(157, 200, 234, 0.15);
  border-left: 3px solid #424769;
}

 .schedule-item.morning {
  background: rgba(249, 177, 122, 0.12);  
  border-left: 3px solid #fc7d06;
}

.name-tag.afternoon {
  color: #424769;
}

.name-tag.morning {
  color: #f78418;
}

.time {
  font-size: 0.875rem;
}

.time.afternoon {
  color: #424769;
}

.time.morning {
  color:  #fc7d06;
}

.schedule-actions {
  margin-left: auto;
  display: flex;
  gap: 0.5rem;
}

.action-button {
  background: transparent;
  border: none;
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-button.edit {
  color: #424769;
}

.action-button.edit:hover {
  background: rgba(240, 243, 248, 0.6);
}

.action-button.delete {
  color: #424769;
}

.action-button.delete:hover {
  background: rgba(248, 243, 240, 0.6);
  color: #E8A17B;
}

.empty-state {
  text-align: center;
  padding: 2rem;
  background: #F8F9FA;
  border-radius: 12px;
  color: #9E9E9E;
  font-size: 0.95rem;
}

.close-button {
  background: none;
  border: none;
  color: #9E9E9E;
  font-size: 1.5rem;
  cursor: pointer;
  transition: color 0.2s ease;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border-radius: 6px;
}

.close-button:hover {
  color: #424769;
  background: rgba(240, 243, 248, 0.4);
}

.substitute-request-button,
.substitute-apply-button {
  padding: 0.4rem 0.8rem;
  border-radius: 6px;
  font-size: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  display: inline-flex;
  align-items: center;
  gap: 0.3rem;
  box-shadow: 0 2px 4px rgba(66, 71, 105, 0.1);
}

.substitute-request-button {
  background: linear-gradient(135deg, #424769, #2D3250);
  color: white;
}

.substitute-request-button:hover {
  background: linear-gradient(135deg, #2D3250, #2D3250);
  transform: translateY(-1px);
  box-shadow: 0 4px 6px rgba(66, 71, 105, 0.2);
}

.substitute-request-button:active {
  transform: translateY(0);
}

.substitute-apply-button {
  background: linear-gradient(135deg, #f87d3b, #fc8f4b);
  color: white;
}

.substitute-apply-button:hover {
  background: linear-gradient(135deg, #f66206, #f88e51);
  transform: translateY(-1px);
  box-shadow: 0 4px 6px rgba(232, 161, 123, 0.2);
} 

.substitute-apply-button:active {
  transform: translateY(0);
}

.substitute-request-button::before {
  content: '↻';
  font-size: 0.9rem;
}

.substitute-apply-button::before {
  content: '✓';
  font-size: 0.9rem;
}

.schedule-add-btn {
  position: absolute;
  top: 1rem;
  right: 1rem;
  padding: 0.5rem 1rem;
  background: #424769;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.schedule-add-btn:hover {
  background: #2D3250;
  transform: translateY(-1px);
}

.event-count {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 24px;
  height: 24px;
  padding: 0 8px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

/* 이벤트 카운트 배지도 동일한 톤으로 */
.event-count.morning {
  background: rgba(240, 243, 248, 0.3);
  color: #424769;
}

.event-count.afternoon {
  background: rgba(255, 246, 241, 0.3);
  color: #FF9D6C;
}

.schedules-container {
  background: white;
  border-radius: 12px;
  padding: 1rem;
  margin-top: 1rem;
  box-shadow: 0 2px 4px rgba(66, 71, 105, 0.05);
}

.date-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #2D3250;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

@media (max-width: 768px) {
  .day-detail {
    padding: 1.5rem;
  }

  .detail-date h3 {
    font-size: 1.25rem;
  }

  .time-section {
    padding: 1rem;
  }

  .schedule-actions {
    opacity: 1;
  }

  .action-button {
    width: 28px;
    height: 28px;
  }

  .substitute-request-button,
  .substitute-apply-button {
    padding: 0.25rem 0.5rem;
    font-size: 0.7rem;
  }

  .schedules-container {
    padding: 0.75rem;
  }

  .date-title {
    font-size: 1.1rem;
  }

  .event-count {
    min-width: 20px;
    height: 20px;
    font-size: 0.7rem;
  }
}

@media (max-width: 480px) {
  .day-detail {
    padding: 1rem;
  }

  .substitute-request-button,
  .substitute-apply-button {
    padding: 0.2rem 0.4rem;
    font-size: 0.65rem;
  }
}
</style>
