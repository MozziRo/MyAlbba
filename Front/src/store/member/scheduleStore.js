import { defineStore } from 'pinia';
import { useWorkerStore } from '@/store/worker/workerStore';
import { ref, computed } from 'vue';
import { formatDate } from '@/common/apis/utils/date';
import { 
  getAllSchedules, 
  getMySchedules, 
  registerSchedule, 
  addSchedule, 
  deleteSchedule,
  requestSubstitute,
  getSubstituteSchedules,
  applyForSubstitute
} from '@/common/apis/api/schedule';

/**
 * API 에러 처리를 위한 유틸리티 함수
 * @param {Error} err - 발생한 에러 객체
 * @returns {string} 에러 메시지
 */
const handleApiError = (err) => {
  if (err.response?.data?.message) {
    return err.response.data.message;
  }
  if (err.message) {
    return err.message;
  }
  return '서버 요청 실패';
};

export const useScheduleStore = defineStore('scheduleStore', () => {
  // State 정의
  const workerStore = useWorkerStore();
  const schedules = ref([]); // 전체 스케줄 목록
  const mySchedules = ref([]); // 내 스케줄 목록
  const substituteRequests = ref([]); // 대타 요청 목록
  const selectedDate = ref(null); // 선택된 날짜
  const error = ref(null); // 에러 메시지
  const loading = ref(false); // 로딩 상태
  const notifications = ref([]); // 알림 목록
  const currentUserId = ref(null); // 현재 사용자 ID
  
  const fetchAllSchedules = async () => {
    try {
      loading.value = true;
      const data = await getAllSchedules();
      schedules.value = data;
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  const fetchMySchedules = async () => {
    try {
      loading.value = true;
      const data = await getMySchedules();
      mySchedules.value = data;
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 스케줄 등록 (confirmed) - 사장님
   * @param {Array} scheduleDataArray - 등록할 스케줄 데이터 배열
   */
  const registerConfirmedSchedule = async (scheduleDataArray) => {
    try {
      loading.value = true;
      await registerSchedule(scheduleDataArray);
      await fetchAllSchedules();
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 스케줄 추가 (confirmed) - 사장님
   * @param {Array} scheduleDataArray - 추가할 스케줄 데이터 배열
   */
  const addConfirmedSchedule = async (scheduleDataArray) => {
    try {
      loading.value = true;
      await addSchedule(scheduleDataArray);
      await fetchAllSchedules();
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 스케줄 삭제 (confirmed) - 사장님
   * @param {string} confirmedScheduleId - 삭제할 스케줄 ID
   */
  const deleteConfirmedSchedule = async (confirmedScheduleId) => {
    try {
      loading.value = true;
      await deleteSchedule(confirmedScheduleId);
      await fetchAllSchedules();
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 대타 신청 (substitute) - 알바생
   * @param {string} confirmedScheduleId - 대타 신청할 스케줄 ID
   */
  const requestSubstituteSchedule = async (confirmedScheduleId) => {
    try {
      loading.value = true;
      const schedule = schedules.value.find(s => s.confirmedScheduleId === confirmedScheduleId);
      if (!schedule) throw new Error('스케줄을 찾을 수 없습니다.');
      
      const currentWorker = workerStore.worker;
      if (!currentWorker) throw new Error('워커 정보를 찾을 수 없습니다.');
      
      if (schedule.workerId !== currentWorker.workerId) {
        throw new Error('자신의 스케줄만 대타 신청할 수 있습니다.');
      }

      const response = await requestSubstitute({ confirmedScheduleId });

      notifications.value.push({
        id: Date.now(),
        type: 'substitute_request',
        confirmedScheduleId: confirmedScheduleId,
        message: `대타 신청이 등록되었습니다.`,
        createdAt: new Date().toISOString()
      });

      await fetchSubstituteRequests();
      return response;
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 대타 신청 목록 조회 (substitute) - 알바생, 사장님
   */
  const fetchSubstituteRequests = async () => {
    try {
      loading.value = true;
      const data = await getSubstituteSchedules();
      substituteRequests.value = data;
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };
  
  /**
   * 대타 지원하기 (confirmed) - 알바생
   * @param {string} substituteScheduleId - 지원할 대타 요청 ID
   */
  const applyToSubstitute = async (substituteScheduleId) => {
    try {
      loading.value = true;
      const request = substituteRequests.value.find(r => r.substituteScheduleId === substituteScheduleId);
      if (!request) throw new Error('대타 요청을 찾을 수 없습니다.');
      
      if (!workerStore.worker) {
        throw new Error('워커 정보를 찾을 수 없습니다.');
      }

      if (request.requesterId === workerStore.worker.workerId) {
        throw new Error('자신의 대타 요청에는 지원할 수 없습니다.');
      }

      const response = await applyForSubstitute({
        substituteScheduleId: substituteScheduleId
      });

      await fetchSubstituteRequests();
      return response;
    } catch (err) {
      error.value = handleApiError(err);
      throw err;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 선택된 날짜 설정
   * @param {string} date - 선택할 날짜
   */
  const setSelectedDate = (date) => {
    selectedDate.value = date;
  };

  /**
   * 현재 사용자 ID 설정
   * @param {string} userId - 사용자 ID
   */
  const setCurrentUserId = (userId) => {
    currentUserId.value = userId;
  };

  // Computed Properties
  /**
   * 특정 날짜에 알림이 있는지 확인
   */
  const hasNotificationOnDate = computed(() => (date) => {
    return notifications.value.some(notification => {
      if (notification.type !== 'substitute_request') return false;

      const relatedSchedule = schedules.value.find(s => s.confirmedScheduleId === notification.confirmedScheduleId);
      if (!relatedSchedule) return false;

      const relatedRequest = substituteRequests.value.find(
        r => r.confirmedScheduleId === notification.confirmedScheduleId
      );

      return relatedSchedule.date === formatDate(date) && !relatedRequest;
    });
  });

  /**
   * 특정 날짜의 대타 요청 목록 조회
   * @param {string} date - 조회할 날짜
   * @returns {Array} 대타 요청 목록
   */
  const getSubstituteRequestsForDate = computed(() => (date) => {
    const formattedDate = formatDate(date);
    return substituteRequests.value.filter(request =>
      request.date === formattedDate
    );
  });

  /**
   * 특정 스케줄이 현재 사용자의 것인지 확인
   */
  const isMySchedule = computed(() => (confirmedScheduleId) => {
    const schedule = schedules.value.find(s => s.confirmedScheduleId === confirmedScheduleId);
    return schedule?.workerId === currentUserId.value;
  });

  /**
   * 대타 신청이 가능한지 확인
   */
  const canRequestSubstitute = computed(() => (confirmedScheduleId) => {
    const schedule = schedules.value.find(s => s.confirmedScheduleId === confirmedScheduleId);
    if (!schedule) return false;

    const existingRequest = substituteRequests.value.find(
      r => r.confirmedScheduleId === confirmedScheduleId
    );

    return schedule.workerId === currentUserId.value && !existingRequest;
  });

  /**
   * 대타 지원이 가능한지 확인
   */
  const canApplyToSubstitute = computed(() => (substituteScheduleId) => {
    const request = substituteRequests.value.find(r => r.substituteScheduleId === substituteScheduleId);
    if (!request) return false;

    return request.requesterId !== currentUserId.value;
  });

  return {
    // State
    schedules,            // 전체 스케줄 목록
    mySchedules,          // 내 스케줄 목록
    substituteRequests,   // 대타 요청 목록
    selectedDate,         // 선택된 날짜
    error,               // 에러 메시지
    loading,             // 로딩 상태
    notifications,        // 알림 목록
    currentUserId,       // 현재 사용자 ID

    // Actions (메서드)
    fetchAllSchedules,             // 전체 스케줄 조회
    fetchMySchedules,              // 내 스케줄 조회
    registerConfirmedSchedule,     // 스케줄 등록 (사장님)
    addConfirmedSchedule,          // 스케줄 추가 (사장님)
    deleteConfirmedSchedule,       // 스케줄 삭제 (사장님)
    requestSubstituteSchedule,     // 대타 신청
    fetchSubstituteRequests,       // 대타 신청 목록 조회
    applyToSubstitute,            // 대타 지원하기
    setSelectedDate,               // 선택된 날짜 설정
    setCurrentUserId,             // 현재 사용자 ID 설정

    // Computed (계산된 속성)
    hasNotificationOnDate,         // 특정 날짜에 알림이 있는지 확인
    getSubstituteRequestsForDate,  // 특정 날짜의 대타 요청 목록 조회
    isMySchedule,                 // 특정 스케줄이 내 것인지 확인
    canRequestSubstitute,         // 대타 신청 가능 여부 확인
    canApplyToSubstitute          // 대타 지원 가능 여부 확인
  };
});