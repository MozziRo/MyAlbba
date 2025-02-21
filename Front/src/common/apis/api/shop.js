import axiosInstance from '../utils/axiosInstance';  // 미리 만들어둔 axios 인스턴스를 import
import { API_URLS } from '../utils/apiConfig';

/**
 * 가게 등록
 * @param {Object} storeData
 * {
	"name":"가게 이름",
	"address":"가게 주소",
	"phoneNumber":"가게 전화번호",
	"payday":"월급 정산 날짜",
}
 */
export const registerStore = async (storeData) => {
    const response = await axiosInstance.post(`${API_URLS.STORE}`, storeData);  // POST /api/auth
    return response.data;  // API 응답 데이터 반환
  };