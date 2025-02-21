import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getNotices, createNotice, getNoticeById, updateNotice, deleteNotice } from '@/common/apis/api/notice';

export const useNoticeStore = defineStore('notice', () => {
  const notices = ref([]);
  const currentNotice = ref(null);
  const loading = ref(false);
  const error = ref(null);


  const fetchNotices = async () => {
    try {
      const response = await getNotices();
      notices.value = response;
    } catch (err) {
      error.value = err;
    }
  };


  const fetchNoticeById = async (id) => {
    try {
      currentNotice.value = await getNoticeById(id);

    } catch (err) {
      error.value = err;
    }
  };


  const addNotice = async (noticeData) => {
    try {
      const response = await createNotice(noticeData);

      notices.value.unshift(response);
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  const editNotice = async (noticeData) => {
    const index = notices.value.findIndex(n => n.id === noticeData.id);
    if (index !== -1) {
      notices.value[index] = { ...notices.value[index], ...noticeData };
    }

    loading.value = true;
    try {
      await updateNotice(noticeData);
      const index = notices.value.findIndex(n => n.id === noticeData.id);
      if (index !== -1) {
        notices.value[index] = noticeData;
      }
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  const removeNotice = async (id) => {
    notices.value = notices.value.filter(n => n.id !== id);

    loading.value = true;
    try {
      await deleteNotice(id);
      await fetchNotices();
    } catch (err) {
      error.value = err;
    } finally {
      loading.value = false;
    }
  };

  return {
    notices,
    currentNotice,
    loading,
    error,
    fetchNotices,
    fetchNoticeById,
    addNotice,
    editNotice,
    removeNotice,
  };
});
