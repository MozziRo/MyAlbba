// store/worker/workerStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getAllWorkers, getNextWorkers, getMyInfoWorker } from '@/common/apis/api/worker';

export const useWorkerStore = defineStore('workerStore', () => {
  const workers = ref([]);
  const nextWorkers = ref([]);
  const worker = ref(null);
  const error = ref(null);
  const loading = ref(false);

  /**
   * 전체 알바생 조회
   */
  const fetchAllWorkers = async () => {
    try {
      loading.value = true;
      const response = await getAllWorkers();
      workers.value = response;
      
    } catch (err) {
      console.error('Error fetching workers:', err);
      error.value = err.message || '직원 목록 조회 실패';
      throw err;
    } finally {
      loading.value = false;
    }

    return true;
  };

  /**
   * 다음 타임 알바생 조회
   */
  const fetchNextWorkers = async () => {
    try {
      loading.value = true;
      const response = await getNextWorkers();
      nextWorkers.value = response.map(worker => ({
        workerId: worker.id,
        name: worker.name
      }));
    } catch (err) {
      error.value = err.message || '다음 시간 직원 목록 조회 실패';
      throw err;
    } finally {
      loading.value = false;
    }
  };


   /**
   * 다음 근무자 이름 목록 반환
   */
   const getNextWorkersNames = () => {
    return nextWorkers.value.map(worker => worker.name);
  };


  /**
   * ID로 직원 찾기
   */
  const getWorkerById = (workerId) => {
    return workers.value.find(worker => worker.workerId === workerId);
  };

  /**
   * 현재 worker 정보 조회
   */
  const fetchWorkerInfo = async () => {
    try {
      loading.value = true;
      const response = await getMyInfoWorker();
      console.log('워커 정보 응답:', response);
      worker.value = response;
    } catch (err) {
      console.error('워커 정보 조회 실패:', err);
      error.value = err.message;
      throw err;
    } finally {
      loading.value = false;
    }
  };

  return {
    workers,
    nextWorkers,
    worker,  
    error,
    loading,
    fetchWorkerInfo, 
    fetchAllWorkers,
    fetchNextWorkers,
    getWorkerById
  };
});