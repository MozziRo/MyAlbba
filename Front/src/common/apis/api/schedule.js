import axiosInstance from '../utils/axiosInstance';
import { API_URLS } from '../utils/apiConfig';

/**
 * 스케줄 등록(confirmed) - 사장님
 * @param {Object} scheduleData - 등록할 스케줄 데이터
 * @returns {Promise} 등록된 스케줄 정보
 * @path POST /api/schedules/confirm
 */
export const registerSchedule = async (scheduleData) => {
    const response = await axiosInstance.post(`${API_URLS.SCHEDULE}/confirm`, scheduleData);
    return response.data;
};

/**
 * 스케줄 추가(confirmed) - 사장님
 * @param {Object} scheduleData - 추가할 스케줄 데이터
 * @returns {Promise} 추가된 스케줄 정보
 * @path POST /api/schedules/confirm/new
 */
export const addSchedule = async (scheduleData) => {
    const response = await axiosInstance.post(`${API_URLS.SCHEDULE}/confirm/new`, scheduleData);
    return response.data;
};

/**
 * 스케줄 삭제(confirmed) - 사장님
 * @param {string} confirmedScheduleId - 삭제할 스케줄 ID
 * @returns {Promise} 삭제 결과
 * @path DELETE /api/schedules/confirm/{confirmedScheduleId}
 */
export const deleteSchedule = async (confirmedScheduleId) => {
    const response = await axiosInstance.delete(`${API_URLS.SCHEDULE}/confirm/${confirmedScheduleId}`);
    return response.data;
};

/**
 * 전체 스케줄 조회(confirmed) - 알바생, 사장님
 * @returns {Promise} 전체 스케줄 목록
 * @path GET /api/schedules/confirm
 */
export const getAllSchedules = async () => {
    console.log("schedule.js : 전체 스케줄 조회")
    const response = await axiosInstance.get(`${API_URLS.SCHEDULE}/confirm`);
    return response.data;
};

/**
 * 내 스케줄 조회(confirmed) - 알바생
 * @returns {Promise} 내 스케줄 목록
 * @path GET /api/schedules/confirm/my
 */
export const getMySchedules = async () => {
    const response = await axiosInstance.get(`${API_URLS.SCHEDULE}/confirm/my`);
    return response.data;
};

/**
 * 대타 스케줄 신청(substitute) - 알바생
 * @param {Object} substituteData - 대타 신청 정보
 * @returns {Promise} 신청 결과
 * @path POST /api/schedules/substitute
 */
export const requestSubstitute = async (substituteData) => {
    const response = await axiosInstance.post(`${API_URLS.SCHEDULE}/substitute`, substituteData);
    return response.data;
};

/**
 * 신청된 대타 스케줄 조회(substitute) - 알바생, 사장님
 * @returns {Promise} 대타 신청 목록
 * @path GET /api/schedules/substitute
 */
export const getSubstituteSchedules = async () => {
    const response = await axiosInstance.get(`${API_URLS.SCHEDULE}/substitute`);
    return response.data;
};

/**
 * 등록된 대타에 지원 등록(confirmed) - 알바생
 * @param {Object} applyData - 대타 지원 정보
 * @returns {Promise} 지원 결과
 * @path PUT /api/schedules/confirm
 */
export const applyForSubstitute = async (applyData) => {
    const response = await axiosInstance.put(`${API_URLS.SCHEDULE}/confirm`, applyData);
    return response.data;
};