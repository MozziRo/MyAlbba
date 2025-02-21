import axiosInstance from '../utils/axiosInstance';
import { API_URLS } from '../utils/apiConfig';

/**
 * @typedef {Object} MonthlyPayment
 * @property {number} salaryId - 급여 고유 ID
 * @property {number} workerId - 직원 고유 ID
 * @property {string} workerName - 직원 이름
 * @property {string} workerAccount - 계좌번호
 * @property {string} salaryDate - 급여 지급일
 * @property {number} totalHours - 총 근무시간
 * @property {number} monthlySalary - 월급 금액
 * @property {'DONE' | 'NOT_DONE'} status - 정산 상태
 */

/**
 * @typedef {Object} DailyPayment
 * @property {number} workerId - 직원 고유 ID
 * @property {string} workerName - 직원 이름
 * @property {string} date - 근무일
 * @property {number} hours - 근무시간
 * @property {number} dailyAmount - 일급 금액
 */

/**
 * 모든 알바생 월급 조회
 * @returns {Promise<MonthlyPayment[]>} - 모든 알바생 월급 정보 목록
 */
export const getAllMonthlyPayments = async () => {
  const response = await axiosInstance.get(`${API_URLS.SALARY}/monthly`)
  return response.data
}

/**
 * 모든 알바생 일급 조회
 * @returns {Promise<DailyPayment[]>} - 모든 알바생 일급 정보 목록
 */
export const getAllDailyPayments = async () => {
  const response = await axiosInstance.get(`${API_URLS.SALARY}/daily`)
  return response.data
}

/**
 * 내 월급 조회
 * @returns {Promise<MonthlyPayment[]>} - 내 월급 정보 목록
 */
export const getMyMonthlyPayment = async () => {
  const response = await axiosInstance.get(`${API_URLS.SALARY}/monthly/my`)
  return response.data
}

/**
 * 내 일급 전체 조회
 * @returns {Promise<DailyPayment[]>} - 내 일급 정보 목록
 */
export const getMyDailyPayments = async () => {
  const response = await axiosInstance.get(`${API_URLS.SALARY}/daily/my`)
  return response.data
}

/**
 * 월급 정산 완료
 * @param {number} salaryId - 정산할 급여의 ID
 * @returns {Promise<{salaryId: number}>} - 정산 완료된 급여 ID
 */
export const completeSalarySettlement = async (salaryId) => {
  const response = await axiosInstance.put(`${API_URLS.SALARY}/monthly/complete/${salaryId}`)
  return response.data
}

/**
 * 사장님 은행 조회회
 */
export const getOwnerBankName = async () => {
  const response = await axiosInstance.get(`${API_URLS.SALARY}/bank/my`)
  return response.data
}

