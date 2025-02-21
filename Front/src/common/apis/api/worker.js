import axiosInstance from '../utils/axiosInstance';  // 미리 만들어둔 axios 인스턴스를 import
import { API_URLS } from '../utils/apiConfig';  // API URL 설정을 import

/**
 * 모든 알바생 조회
 * @returns {Promise<Array>} workers - 알바생 목록
 */
export const getAllWorkers = async () => {
  try {
    const response = await axiosInstance.get(`${API_URLS.AUTH}`);
    return response.data;
  } catch (error) {
    console.error('알바생 목록 조회 실패:', error);
    throw error;
  }
 };

/**
 * 내 다음 시간 알바생 조회
 * @returns {Promise<Array>} nextWorkers - 다음 시간 알바생 목록
 */
export const getNextWorkers = async () => {
  try {
    const response = await axiosInstance.get(`${API_URLS.MEMO}/my/next`);
    return response.data;
  } catch (error) {
    console.error('다음 시간 알바생 목록 조회 실패:', error);
    throw error;
  }
 };
 
/////////////////////////////////////////////////////////////////////////////////
/// 추가 ///
// 알바생 본인 정보 수정
export const updateWorkerInfo = async (data) => {
  const response = await axiosInstance.put(`${API_URLS.AUTH}/worker`, data);
  return response.data;
};

// 알바생 정보 조회
export const getWorkerInfo = async (workerId) => {
  const response = await axiosInstance.get(`${API_URLS.AUTH}/${workerId}`);
  return response.data;
};

// 알바생 본인 정보 조회
export const getMyInfoWorker = async () => {
  const response = await axiosInstance.get(`${API_URLS.AUTH}/worker/my`);
  return response.data;
};

// 사장님 본인 정보 조회
export const getMyInfoOwner = async () => {
  const response = await axiosInstance.get(`${API_URLS.AUTH}/owner/my`);
  return response.data;
};

// 알바생 월급 정보 조회
export const getMySalary = async () => {
  const response = await axiosInstance.get(`${API_URLS.AUTH}/worker/salary`);
  return response.data;
};

// 요청 및 응답
/**
 * @typedef {Object} WorkerSalaryInfo
 * @param {number} workerId - 알바생PK
 * @param {number} hourlySalary - 기존/수정된 시급
 * @param {number} tax - 기존/수정된 세금, EX) 0 or 3.3
 * @param {number} insurance - 기존/수정된 4대보험, EX) 0 or 9.4
 * @param {boolean} holidayPay - 기존/수정된 주휴수당, EX) true or false
**/
// 알바생 월급 정보 수정
export const updateWorkerSalary = async (data) => {
  const response = await axiosInstance.put(`${API_URLS.AUTH}/worker`, data);
  return response.data;
};
