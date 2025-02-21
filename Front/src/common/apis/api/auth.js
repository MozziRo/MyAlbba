// api/auth.js
import axiosInstance from '../utils/axiosInstance';  // 미리 만들어둔 axios 인스턴스를 import
import { API_URLS, API_BASE_URL } from '../utils/apiConfig';  // API URL 설정을 import

export const sendVerificationCode = async (email) => {
  console.log("Sending request to:", `${API_BASE_URL}/auth/sendcode`);
  return axios.post(`${API_BASE_URL}/auth/sendcode`, { email });
};
/**
 * 연동신청 (알바생)
 * @param {String} ownerEmail - 연동 신청 정보
 {
    "ownerEmail":"사장님Email"
  }
 */
export const sendCode = async (ownerEmail) => {
  console.log(ownerEmail)
  const response = await axiosInstance.post(`${API_URLS.AUTH}/sendcode`, { ownerEmail });  // POST /api/auth/sendcode
  return response.data;  // API 응답 데이터 반환
};

/**
 * 연동수락 (사장)
 * @param {Object} data - 연동 수락 정보
 *
 * 요청
  {
    "ownerEmail":"사장님Email",
    "code":"검증하려는 인증코드"
  }
 */
export const verifyCode = async (data) => {
  try {
    // 인증번호 확인 요청
    await axiosInstance.post(`${API_URLS.AUTH}/verifycode`, data);  // POST /api/auth/link/verifycode

    // 200 OK : 인증번호 확인 성공
    return true;
  } catch (error) {
    // 400 Bad Request: 인증번호 확인 실패
    return false;
  }
};

/**
 * 이메일 중복 확인 요청
  {
    "role":"가입하려는 사용자 구분", EX) "owner" "worker"
    "email":"사용자Email"
  }
 * @param {object} emailObj - 사용자 정보 (이메일)
 */
export const checkDuplicateEmail = async (emailObj) => {
  try {
    // 중복 확인 요청
    await axiosInstance.post(`${API_URLS.AUTH}/check/duplicate`, emailObj);  // POST /api/check

    // 200 OK : 중복되지 않은 이메일
    return { isDuplicate: false, message: '사용 가능한 이메일입니다.'};
  } catch (error) {
    // 409 Conflict: 중복된 이메일
    return { isDuplicate: true, message: '이미 사용 중인 이메일입니다.'};
  }
};

/**
 * 사장 회원가입
 * @param {Object} userData
 * {
	"role":"가입하려는 사용자 구분", EX) "owner" "worker"
	"email":"로그인 이메일",
	"password":"로그인 비밀번호",
	"name":"이름",
	"phoneNumber":"전화번호",
  "storeName",
  "storeAddress",
  "storePhoneNumber",
  "payday"
}
 */
export const signUpOwner = async (userData) => {
  const response = await axiosInstance.post(`${API_URLS.AUTH}/signup/owner`, userData);  // POST /api/auth
  return response;  // API 응답 데이터 반환
};

/**
 * 알바생 회원가입
 * @param {Object} userData
 * {
	"role":"가입하려는 사용자 구분", EX) "owner" "worker"
	"email":"로그인 이메일",
	"password":"로그인 비밀번호",
	"name":"이름",
	"phoneNumber":"전화번호",
  "hourlySalary",
  "bank",
  "account",
  "tax",
  "insurance",
  "holidayPay"
}
 */
export const signUpWorker = async (userData) => {
  const response = await axiosInstance.post(`${API_URLS.AUTH}/signup/worker`, userData);  // POST /api/auth
  return response;  // API 응답 데이터 반환
};

/**
 * 로그인
 * @param {Object} credentials - 로그인 정보
 * {
    "role":"로그인 사용자 구분",
    "email":"로그인 이메일",
    "pasword":"로그인 비밀번호"
}
 */
export const login = async (formData) => {
    const response = await axiosInstance.post(`${API_URLS.PURE}/login`, // POST /login
      formData,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    );
    return response;
};

/**
 * 비밀번호 확인
 * @param {string} password - 비밀번호
 * {
    "password":"비밀번호"
 } @return {number} 200 or 401
 */
export const passwordCheck = async (password) => {
  const response = await axiosInstance.post(`${API_URLS.AUTH}/check/password`, password);
  return response.status;
};



/**
 * 리프레시 토큰을 사용하여 액세스 토큰 갱신
 * @param {string} refreshToken - 리프레시 토큰
 */
export const refreshAccessToken = async (refreshToken) => {
    const response = await axiosInstance.post(`${API_URLS.AUTH}/refresh`, { refresh_token: refreshToken });
    return response.data;
};


export const getMyInfo = async () => {
  const response = await axiosInstance.get(`${API_URLS.AUTH}/my`);
  return response.data;
}
