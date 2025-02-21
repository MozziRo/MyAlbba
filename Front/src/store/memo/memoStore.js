// src/stores/memoStore.js
import { defineStore } from 'pinia';
import { ref } from 'vue';
import { addMemo, getReceivedMemos, getAllMemos, getSentMemos, updateMemoStatus } from '@/common/apis/api/memo.js';

export const useMemoStore = defineStore('memoStore', () => {
  // 완료된 메모를 저장할 로컬스토리지 키
  const COMPLETED_MEMOS_KEY = 'completed_memos';
  
  // 상태 변수 정의
  const receivedMemos = ref([]);
  const sentMemos = ref([]);
  const loading = ref(false);
  const error = ref(null);

  // 로컬스토리지에서 완료된 메모 가져오기
  const getCompletedMemos = () => {
    const completedMemos = localStorage.getItem(COMPLETED_MEMOS_KEY);
    return completedMemos ? JSON.parse(completedMemos) : [];
  };

  // 로컬스토리지에 완료된 메모 저장
  const saveCompletedMemo = (memo) => {
    const completedMemos = getCompletedMemos();
    // 중복 체크
    if (!completedMemos.some(m => m.id === memo.id)) {
      completedMemos.push({ ...memo, status: 'DONE' });
      localStorage.setItem(COMPLETED_MEMOS_KEY, JSON.stringify(completedMemos));
    }
  };

  // 메모 ID로 완료 상태 확인
  const isMemoCompleted = (memoId) => {
    const completedMemos = getCompletedMemos();
    return completedMemos.some(memo => memo.id === memoId);
  };

  // 완료된 메모 삭제
  const removeCompletedMemo = (memoId) => {
    const completedMemos = getCompletedMemos();
    const filtered = completedMemos.filter(memo => memo.id !== memoId);
    localStorage.setItem(COMPLETED_MEMOS_KEY, JSON.stringify(filtered));
  };

  // 알림장 새로운 내용 등록
  const addMemoAction = async (memo) => {
    const response = await addMemo(memo);
    if (response) {
      return true;
    }
    return false;
  };

  // 메모 상태 변경 함수
  const updateMemoStatusAction = async (targetMemo) => {
    try {
      const response = await updateMemoStatus(targetMemo.id);

      if (response.status === 200) {
        // 완료된 메모를 로컬스토리지에 저장
        saveCompletedMemo(targetMemo);

        // 발신함 메모 상태 업데이트
        sentMemos.value = sentMemos.value.map(memo => 
          memo.id === targetMemo.id 
            ? { ...memo, status: 'DONE' }
            : memo
        );

        // 수신함 메모 필터링
        receivedMemos.value = receivedMemos.value.filter(
          memo => memo.id !== targetMemo.id
        );

        return true;
      }
      return false;
    } catch (error) {
      console.error('Error updating memo status:', error);
      return false;
    }
  };

  // 메모 조회
  const fetchMemos = async () => {
    const [received, sent] = await Promise.all([
      getReceivedMemos(),
      getSentMemos()
    ]);

    // 로컬스토리지에서 완료된 메모 가져오기
    const completedMemos = getCompletedMemos();
    
    // 수신함에 완료된 메모 추가
    receivedMemos.value = [
      ...received,
      ...completedMemos.filter(memo => 
        // 수신함에 없는 완료된 메모만 추가
        !received.some(r => r.id === memo.id)
      )
    ];

    sentMemos.value = sent;
  };

  // 사장님의 메모들을 모두 가져오는 함수
  const fetchAllMemos = async () => {
    receivedMemos.value = await getAllMemos();
  };

  return {
    // 상태 변수 반환
    receivedMemos,
    sentMemos,
    loading,
    error,

    // 액션 반환
    addMemoAction,
    fetchMemos,
    fetchAllMemos,
    updateMemoStatusAction,
    isMemoCompleted,
    removeCompletedMemo,
  };
});