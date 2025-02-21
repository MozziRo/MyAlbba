<template>
  <Modal>
    <template #header>
      <h3 class="modal-title">{{ modalTitle }}</h3>
    </template>

    <template #body>
      <div class="schedule-container">
        <EmployeeSelection
          :workers="workers"
          v-model:selectedEmployees="selectedEmployees"
          :isEditMode="!!initialSchedule"
        />

        <DateSelection
          v-model:selectedDays="selectedDays"
          :currentDate="selectedDate"
          :isEditMode="!!initialSchedule"
        />

        <TimeSelection
          v-model:selectedStartTime="selectedStartTime"
          v-model:selectedEndTime="selectedEndTime"
          :availableEndTimeSlots="availableEndTimeSlots"
          @update-end-times="updateEndTimeOptions"
          @time-selected="handleTimeSelection"
        />
      </div>

      <!-- 스케줄 요약 -->
      <div v-if="schedulesByDate.length > 0" class="schedule-summary">
        <div
          v-for="(daySchedule, index) in schedulesByDate"
          :key="index"
          class="schedule-card"
        >
          <div class="schedule-card-header">
            <span class="schedule-date">{{ daySchedule.date }}일</span>
            <button
              class="remove-schedule"
              @click="removeDaySchedule(index)"
            >
              ×
            </button>
          </div>
          <div class="schedule-details">
            <div
              v-for="(schedule, schedIndex) in daySchedule.schedules"
              :key="schedIndex"
              class="schedule-item"
            >
              <p class="schedule-time">
                {{ formatTime(schedule.startTime) }} - {{ formatTime(schedule.endTime) }}
              </p>
              <div class="employee-list">
                <span
                  v-for="employee in schedule.employees"
                  :key="employee.workerId"
                  class="employee-tag"
                >
                  {{ employee.name }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 액션 버튼 -->
      <div class="modal-actions">
        <button class="button cancel" @click="handleCancel">취소</button>
        <button
          class="button confirm"
          :disabled="schedulesByDate.length === 0"
          @click="handleConfirm"
        >
          {{ mode === 'edit' ? '수정' : '확인' }}
        </button>
      </div>
    </template>
  </Modal>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import Modal from '@/components/common/Modal.vue';
import EmployeeSelection from './EmployeeSelection.vue';
import DateSelection from './DateSelection.vue';
import TimeSelection from './TimeSelection.vue';
import { useWorkerStore } from '@/store/worker/workerStore';
import { useScheduleStore } from '@/store/member/scheduleStore';
import { formatDate, formatTime } from '@/common/apis/utils/date';

const emit = defineEmits(['close', 'refresh']);
const workerStore = useWorkerStore();
const scheduleStore = useScheduleStore();

const props = defineProps({
  initialSchedule: {
    type: Object,
    default: null
  },
  selectedDate: {
    type: Date,
    required: true
  },
  mode: {
    type: String,
    default: 'register',
    validator: (value) => ['register', 'add', 'edit'].includes(value)
  },
});

// State
const selectedEmployees = ref([]);
const selectedDays = ref([]);
const selectedStartTime = ref('');
const selectedEndTime = ref('');
const availableEndTimeSlots = ref([]);
const schedulesByDate = ref([]);

// Computed
const workers = computed(() => workerStore.workers);
const currentDate = computed(() => props.selectedDate);
const currentYear = computed(() => currentDate.value.getFullYear());
const currentMonth = computed(() => currentDate.value.getMonth() + 1);

const modalTitle = computed(() => {
  switch(props.mode) {
    case 'register': return '스케줄 등록';
    case 'add': return '스케줄 추가';
    case 'edit': return '스케줄 수정';
    default: return '스케줄';
  }
});

// Methods
const updateEndTimeOptions = () => {
  if (selectedStartTime.value) {
    const startIndex = timeSlots.value.indexOf(selectedStartTime.value);
    availableEndTimeSlots.value = timeSlots.value.slice(startIndex + 1);
    selectedEndTime.value = '';
  }
};

const handleTimeSelection = () => {
  if (isValidScheduleSelection()) {
    addSchedules();
    if (!props.initialSchedule) {
      resetSelections();
    }
  }
};

const isValidScheduleSelection = () => {
  return selectedEmployees.value.length > 0 &&
         selectedDays.value.length > 0 &&
         selectedStartTime.value &&
         selectedEndTime.value;
};

const addSchedules = () => {
  // 수정 모드일 경우 기존 스케줄을 모두 제거
  if (props.initialSchedule) {
    schedulesByDate.value = [];
  }

  selectedDays.value.forEach(day => {
    const existingDayIndex = schedulesByDate.value.findIndex(ds => ds.date === day);
    const newSchedule = {
      employees: [...selectedEmployees.value],
      startTime: selectedStartTime.value,
      endTime: selectedEndTime.value
    };

    if (existingDayIndex > -1) {
      schedulesByDate.value[existingDayIndex].schedules.push(newSchedule);
    } else {
      schedulesByDate.value.push({
        date: day,
        schedules: [newSchedule]
      });
    }
  });
};

const resetSelections = () => {
  if (!props.initialSchedule) {
    selectedEmployees.value = [];
    selectedDays.value = [];
    selectedStartTime.value = '';
    selectedEndTime.value = '';
    availableEndTimeSlots.value = [];
  }
};

const removeDaySchedule = (index) => {
  schedulesByDate.value.splice(index, 1);
};

const handleCancel = () => {
  emit('close');
  resetForm();
};

const handleConfirm = async () => {
  if (schedulesByDate.value.length === 0) {
    alert('스케줄을 먼저 추가해주세요.');
    return;
  }

  try {
    const schedules = formatSchedulesForSubmission();
    console.log('전송할 스케줄 데이터:', schedules);

    if (props.mode === 'edit') {
      if (props.initialSchedule) {
        // 배열이 아닌 단일 객체로 전송
        await scheduleStore.addConfirmedSchedule(schedules[0]);
        await scheduleStore.deleteConfirmedSchedule(props.initialSchedule.confirmedScheduleId);
      }
    } else if (props.mode === 'register') {
      await scheduleStore.registerConfirmedSchedule(schedules);
    } else {
      await scheduleStore.addConfirmedSchedule(schedules[0]); // 여기도 단일 객체로
    }

    emit('close');
    emit('refresh');
    resetForm();
  } catch (error) {
    console.error('스케줄 처리 중 오류 발생:', error);
    if (error.response) {
      alert(`스케줄 처리 실패: ${error.response.data.message || '서버 오류가 발생했습니다.'}`);
    } else {
      alert('스케줄 처리에 실패했습니다. 잠시 후 다시 시도해주세요.');
    }
  }
};

const formatSchedulesForSubmission = () => {
  const workerStore = useWorkerStore();

  return schedulesByDate.value.flatMap(daySchedule =>
    daySchedule.schedules.flatMap(schedule =>
      schedule.employees.map(employee => {
        const worker = workerStore.getWorkerById(employee.workerId);
        if (!worker) {
          throw new Error(`Worker not found with ID: ${employee.workerId}`);
        }

        return {
          workerId: worker.workerId,
          startTime: schedule.startTime.includes(':00') ? schedule.startTime : `${schedule.startTime}:00`,
          endTime: schedule.endTime.includes(':00') ? schedule.endTime : `${schedule.endTime}:00`,
          date: formatDate(new Date(currentYear.value, currentMonth.value - 1, parseInt(daySchedule.date)))
        };
      })
    )
  );
};

const resetForm = () => {
  selectedEmployees.value = [];
  selectedDays.value = [];
  selectedStartTime.value = '';
  selectedEndTime.value = '';
  availableEndTimeSlots.value = [];
  schedulesByDate.value = [];
};

// TimeSlots computed property
const timeSlots = computed(() => {
  const slots = [];
  for (let hour = 0; hour < 24; hour++) {
    for (let minute = 0; minute < 60; minute += 30) {
      const formattedHour = hour.toString().padStart(2, '0');
      const formattedMinute = minute.toString().padStart(2, '0');
      slots.push(`${formattedHour}:${formattedMinute}`);
    }
  }
  return slots;
});

// 초기화
onMounted(async () => {
  await workerStore.fetchAllWorkers();

  // 수정 모드인 경우 초기값 설정
  if (props.initialSchedule) {
    selectedEmployees.value = [{
      workerId: props.initialSchedule.workerId,
      name: props.initialSchedule.workerName
    }];

    const scheduleDate = new Date(props.initialSchedule.date);
    selectedDays.value = [scheduleDate.getDate()];
    selectedStartTime.value = props.initialSchedule.startTime.slice(0, 5);
    selectedEndTime.value = props.initialSchedule.endTime.slice(0, 5);

    // 스케줄 요약에 추가
    schedulesByDate.value = [{
      date: scheduleDate.getDate(),
      schedules: [{
        employees: selectedEmployees.value,
        startTime: selectedStartTime.value,
        endTime: selectedEndTime.value
      }]
    }];
  }
});

defineExpose({
  resetForm
});
</script>

<style scoped>
.modal-title {
  font-size: 18px;
  font-weight: 600;
  color: #111827;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
}

.schedule-container {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  gap: 20px;
  padding: 20px;
  background: white;
  margin: 0 24px;
  border: 2px solid #e5e7eb;
  border-radius: 8px;
}

.schedule-summary {
  display: flex;
  gap: 16px;
  padding: 24px;
  overflow-x: auto;
  scrollbar-width: thin;
}

.schedule-card {
  flex: 0 0 280px;
  background: white;
  border-radius: 8px;
  border: 2px solid #e5e7eb;
  padding: 20px;
  transition: all 0.2s ease;
}

.schedule-card:hover {
  border-color: #9ca3af;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.schedule-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #e5e7eb;
}

.schedule-date {
  font-size: 15px;
  font-weight: 600;
  color: #111827;
}

.remove-schedule {
  width: 32px;
  height: 32px;
  border: 2px solid #e5e7eb;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s;
  color: #6b7280;
  font-size: 18px;
}

.remove-schedule:hover {
  background: #fee2e2;
  color: #dc2626;
  border-color: #dc2626;
}

.schedule-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.schedule-item {
  padding: 16px;
  background: #f9fafb;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  transition: all 0.2s;
}

.schedule-item:hover {
  border-color: #9ca3af;
  background: #f3f4f6;
}

.schedule-time {
  font-size: 14px;
  font-weight: 600;
  color: #111827;
  margin-bottom: 12px;
}

.employee-list {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.employee-tag {
  font-size: 13px;
  padding: 6px 12px;
  background: #f3f4f6;
  color: #111827;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  font-weight: 500;
}

.modal-actions {
  padding: 20px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid #e5e7eb;
  background: #f9fafb;
}

.button {
  min-width: 80px;
  height: 40px;
  padding: 0 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.button.cancel {
  background: white;
  border: 2px solid #e5e7eb;
  color: #111827;
}

.button.cancel:hover {
  background: #f3f4f6;
  border-color: #d1d5db;
}

.button.confirm {
  background: var(--primary);
  color: white;
}

.button.confirm:hover:not(:disabled) {
  background: var(--primary-dark);
}

.button.confirm:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

@media (max-width: 900px) {
  .schedule-container {
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 16px;
    margin: 16px;
  }

  .schedule-summary {
    padding: 16px;
  }

  .schedule-card {
    flex: 0 0 260px;
  }

  .modal-actions {
    padding: 16px;
  }

  .button {
    flex: 1;
  }
}
</style>
