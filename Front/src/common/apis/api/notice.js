import axiosInstance from '../utils/axiosInstance';
import { API_URLS } from '../utils/apiConfig';

/**
 * 공지사항 등록
 * @param {Object} noticeData - 공지사항 정보
 * {
 *   "title": "공지 제목",
 *   "content": "공지 내용"
 * }
 */
export const createNotice = async (noticeData) => {
  const response = await axiosInstance.post(`${API_URLS.NOTICE}`, noticeData);
  return response.data;
};

/**
 * 공지사항 조회
 * @returns {Promise<Array>} notices - 공지사항 목록
 * [
	{
		"id": "공지사항ID",
		"ownerId": "사장님ID",
		"title": "공지사항 제목",
		"content": "공지사항 내용",
		"createdAt": "공지사항 생성일"
	},
	{
		"id": "공지사항ID",
		"ownerId": "사장님ID",
		"title": "공지사항 제목",
		"content": "공지사항 내용",
		"createdAt": "공지사항 생성일"
	},
	...
]

 */
export const getNotices = async () => {
  const response = await axiosInstance.get(`${API_URLS.NOTICE}`);
  return response.data;
};

/**
 * 공지사항 세부조회
 * @param {String} id - 공지사항 ID
 * @returns {Promise<Object>} notice - 공지사항 세부 정보
 */
export const getNoticeById = async (id) => {
  const response = await axiosInstance.get(`${API_URLS.NOTICE}/${id}`);
  return response.data;
};

/**
 * 공지사항 수정
 * @param {Object} noticeData - 수정할 공지사항 정보
 * {
 *   "id": "공지사항ID",
 *   "title": "공지사항 제목",
 *   "content": "공지사항 내용"
 * }
 */
export const updateNotice = async (noticeData) => {
  const response = await axiosInstance.put(`${API_URLS.NOTICE}`, noticeData);
  return response.data;
};

/**
 * 공지사항 삭제
 * @param {String} id - 공지사항 ID
 */
export const deleteNotice = async (id) => {
  const response = await axiosInstance.delete(`${API_URLS.NOTICE}/${id}`);
  return response.data;
};
