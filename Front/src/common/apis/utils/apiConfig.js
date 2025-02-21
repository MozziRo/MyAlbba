export const API_BASE_URL = window.env?window.env.VITE_APP_API_URL:"https://default.api.url";
export const API_WS_URL = window.env?window.env.VITE_APP_WS_URL:"https://default.ws.url";
export const API_PURE_URL = window.env?window.env.VITE_APP_PURE_URL:"https://default.pure.url";

export const API_URLS = {
  AUTH: `${API_BASE_URL}/auth`,
  STORE: `${API_BASE_URL}/store`,
  SCHEDULE: `${API_BASE_URL}/schedules`,
  MEMO: `${API_BASE_URL}/memo`,
  NOTICE: `${API_BASE_URL}/notice`,
  SALARY: `${API_BASE_URL}/salary`,
  WORKER: `${API_BASE_URL}/worker`,
  WEBRTC: `${API_BASE_URL}/webrtc`,
  REVIEW: `${API_BASE_URL}/review`,
  PURE: `${API_PURE_URL}`
};