import axiosInstance from '../utils/axiosInstance'
import { API_URLS } from '../utils/apiConfig'

/**
 * 면접방 생성 (사장님)
 * @returns {Promise<Object>} - 방 정보 { roomId, url }
 */
export const createInterviewRoom = async () => {
  const response = await axiosInstance.get(`${API_URLS.WEBRTC}/create`)
  return response.data
}

/**
 * 면접방 체크 (면접자)
 */
export const checkInterviewRoom = async (roomId) => {
  const response = await axiosInstance.get(`${API_URLS.WEBRTC}/${roomId}/exists`)
  return response.data
}
