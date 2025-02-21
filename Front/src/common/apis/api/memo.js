import axiosInstance from '../utils/axiosInstance';  // 미리 만들어둔 axios 인스턴스를 import
import { API_URLS } from '../utils/apiConfig';  // API URL 설정을 import

/**
 * 알림장 새로운 내용 등록
 * @param {Object} memo - 알림장 내용
 *
 * memo
 * {
	  "content": "알림장 내용",
	  "workers" : [1, 2, 3] // 보내고자 하는 알바생의 id 값
   }
 */
export const addMemo = async (memo) => {
  const response = await axiosInstance.post(`${API_URLS.MEMO}`, memo);
  return response.status === 201;
};

/**
 * 알림장 등록된 업무 조회
 *
[
	{
		"id": "메모 id",
		"content": "알림장 내용",
		"status" : "NOT DONE",
		"creator" : "작성자 이름",
		"createdAt": "2025-01-23T10:15:30"
	},
	{
		"id": "메모 id",
		"content": "알림장 내용",
		"status" : "NOT DONE",
		"creator" : "작성자 이름",
		"createdAt": "2025-01-23T10:15:30"
	},
	...
]
 */
export const getReceivedMemos = async () => {
  const response = await axiosInstance.get(`${API_URLS.MEMO}/my`);
  return response.data;
};

// 사장님이 알바생들의 모든 알림장을 요청
export const getAllMemos = async () => {
	const response = await axiosInstance.get(`${API_URLS.MEMO}`);
	return response.data;
  };

export const getSentMemos = async () => {
	const response = await axiosInstance.get(`${API_URLS.MEMO}/my/send`);
	return response.data;
};

/**
 * 알림장 완료 여부 업데이트
 * @param {String} memoId - 알림장의 고유 ID
 */
export const updateMemoStatus = async (id) => {
  const response = await axiosInstance.put(`${API_URLS.MEMO}/my`,
	null,
	 {
		params: {
			id // 쿼리 스트링으로 memoId 전달
		}
	});
  return response;
};
