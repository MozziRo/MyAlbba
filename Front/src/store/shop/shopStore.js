import { defineStore } from 'pinia';
import { ref } from 'vue';

import { registerStore } from '@/common/apis/api/shop';

export const useShopStore = defineStore('shop', () => {

    const store = ref({});

    // 가게 등록
    const register = async (storeData) => {
      try {
        const responseData = await registerStore(storeData);
        console.log(responseData);
        return true;
      } catch (err) {
        error.value = handleApiError(err); // 공통 에러 메시지 설정
      }
    };

    return {
      register,
    }
})