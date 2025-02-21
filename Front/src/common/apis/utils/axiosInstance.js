import axios from 'axios';
import { API_BASE_URL } from '../utils/apiConfig';

// console.log("axiosInstance에 들어옴")
// Axios 인스턴스 생성
const axiosInstance = axios.create({
  baseURL: `${API_BASE_URL}`, // 기본 API URL
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터
// 요청 보내기 전에 헤더에 무조건 Bearer 토큰을 넣어서 요청한다.
axiosInstance.interceptors.request.use(
  (config) => {
    const tokenString = localStorage.getItem('access_token');
    const tokenObject = JSON.parse(tokenString);
    const currentTime = Date.now();

    if(tokenObject) {
      // 토큰의 만료시간을 검사하고 헤더에 추가
      if(currentTime < tokenObject.expiration) {
        if (tokenObject.access_token) {
          config.headers['Authorization'] = `Bearer ${tokenObject.access_token}`; // 요청 헤더에 Bearer 토큰 추가
          config.headers['authorization'] = `Bearer ${tokenObject.access_token}`; // 요청 헤더에 Bearer 토큰 추가
          // console.log(tokenObject.access_token) // 디버그 토큰 출력
        }
      } else {
        console.log('토큰이 만료되어 삭제되었습니다.');
        localStorage.removeItem('access_token');
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response, // 성공 응답은 그대로 반환

  async (error) => {
    const { response, config } = error;

    // 1. 서버 응답이 없는 경우 (네트워크 에러)
    if (!response) {
      alert('네트워크 연결을 확인해주세요.');
      return Promise.reject(error);
    }

    // 2. 인증 에러 (401) 처리
    // if (response.status === 401) {
    //   return handleAuthError(error);
    // }

    // 3. 서버 에러 (500) 처리
    if (response.status === 500) {
      alert('서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
      return Promise.reject(error);
    }

    // // 4. 이메일 중복 에러 (409) 처리
    // if(response.status === 409) {
    //   return Promise.reject(error);
    // }

    // // 5. 인증번호 확인 에러(400) 처리
    // if(response.status === 400) {
    //   return Promise.reject(error);
    // }

    // 4. 기타 에러 처리
    console.log('요청 처리 중 오류가 발생했습니다.');
    return Promise.reject(error);
  }
);

// 인증 에러 처리 함수
async function handleAuthError(error) {
  const { config } = error;

  // 이미 토큰 재발급을 시도한 요청인 경우
  if (config._retry) {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    window.location.href = '/login';
    return Promise.reject(new Error('인증이 만료되었습니다.'));
  }

  const refreshToken = localStorage.getItem('refresh_token');
  if (!refreshToken) {
    window.location.href = '/login';
    return Promise.reject(new Error('리프레시 토큰이 없습니다.'));
  }

  try {
    config._retry = true;
    const refreshResponse = await axios.post('/auth/refresh', { refresh_token: refreshToken });
    const { access_token } = refreshResponse.data;

    localStorage.setItem('access_token', access_token);
    config.headers['Authorization'] = `Bearer ${access_token}`;

    return axios(config);
  } catch (err) {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    window.location.href = '/login';
    return Promise.reject(err);
  }
}

export default axiosInstance;
