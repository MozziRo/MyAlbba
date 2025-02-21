import axiosInstance from '@/common/apis/utils/axiosInstance';
import { API_URLS } from '@/common/apis/utils/apiConfig';

// {
// 	"name":"멀티캠퍼스 역삼",
// 	"address":"서울 강남구 테헤란로 212",
// 	"phoneNumber":"02-3429-5114",
// 	"payday":"10",
// }
//가게 정보 등록
export const registerStore = async (data) => {
  const response = await axiosInstance.post(API_URLS.STORE, data);
  return response.data;
};

// 가게 정보 조회
export const getStore = async () => {
  const response = await axiosInstance.get(API_URLS.STORE);
  return response.data;
};

//가게 정보 수정
// {
// 	"name":"멀티캠퍼스 역삼",
// 	"address":"서울 강남구 테헤란로 212",
// 	"phoneNumber":"02-3429-5114",
// 	"payday":"10",
// }
export const updateStore = async (data) => {
  const response = await axiosInstance.put(API_URLS.STORE, data);
  return response.data;
};
