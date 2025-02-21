// src/stores/memberStore.js
import { defineStore } from 'pinia';
import { ref, computed, watch } from 'vue';
import { signUpOwner, signUpWorker, login, sendCode, verifyCode, checkDuplicateEmail, getMyInfo } from '@/common/apis/api/auth';  // auth.js에서 정의된 API 함수들 import
import { getCurrentUser, cleanToken, decodeToken, getTokenFromLocalStorage } from '@/common/apis/utils/tokenUtils';

// 공통 에러 처리 함수
const handleApiError = (err) => {
  return err.response?.data?.message || '서버 요청 실패';
};

export const useMemberStore = defineStore('memberStore', () => {
  // 상태 정의
  const user = ref({
    name: '비회원',
    role: '',
  })  // 테스트를 위해 기본값 설정
  const error = ref(null);  // 에러 메시지
  const isAuthenticated = ref(false);  // 인증 상태
  const verificationCodeSent = ref(false);  // 인증 코드 발송 여부
  const responseMessage = ref('');  // API 응답 메시지

  const isOwner = computed(() => user.value?.role === '사장');
  const isWorker = computed(() => user.value?.role === '알바생');

  const fetchUserInfo = async () => {
    const token = getTokenFromLocalStorage();
    if (!token) {
      return;
    }
    
    try {
      const data = await getMyInfo(); // 백엔드에서 사용자 정보 가져오기
      
      user.value = {
        name: data.name,
        role: data.role === 'worker' ? '알바생' : '사장'
      };
  
      isAuthenticated.value = true;
    } catch (err) {
      console.error("사용자 정보를 불러오는 데 실패했습니다.", err);
      logout();
    }
  };

  // 멤버의 속성을 가져오는 함수
  const getMemberName = () => {
    const token = getTokenFromLocalStorage()
    if (token) {
      const decodedToken = decodeToken(token)
      return decodedToken.userName
    }
    return "비회원"
  }
  const getMemberRole = () => {
    const token = getTokenFromLocalStorage()
    if (token) {
      const decodedToken = decodeToken(token)
      return decodedToken.userRole
    }
    return ""
  }
  const getMemberEmail = () => {
    const token = getTokenFromLocalStorage()
    if (token) {
      const decodedToken = decodeToken(token)
      return decodedToken.userEmail
    }
    return ""
  }


  // 인증 코드 요청 (알바생)
  const sendCodeAction = async (ownerEmail) => {
    try {
      const responseData = await sendCode(ownerEmail);
      responseMessage.value = responseData;
      verificationCodeSent.value = true;
      return true;
    } catch (err) {
      error.value = handleApiError(err);
      return false;
    }
  };

  // 인증 코드 수락 (사장)
  const verifyAccount = async (data) => {
    try {
      const responseData = await verifyCode(data);
      isAuthenticated.value = true;
      responseMessage.value = responseData.message;
      return true;
    } catch (err) {
      error.value = handleApiError(err);
      return false;
    }
  };

  // 이메일 중복 체크
  const checkEmail = async (emailObj) => {
    try {
      const responseData = await checkDuplicateEmail(emailObj); // 이메일 중복체크 API 호출
      return responseData
    } catch (err) {
      error.value = handleApiError(err); // 공통 에러 메시지 설정
    }
  };

  // 회원가입
  const register = async (userData) => {
    try {
      if(userData.role === 'owner') {
        await signUpOwner(userData);
      } else {
        await signUpWorker(userData);
      }
      return true;
    } catch (err) {
      error.value = handleApiError(err); // 공통 에러 메시지 설정
    }
    return false;
  };

  // 로그인
  const loginAction = async (credentials) => {
    try {
      const response = await login(credentials); // 로그인 요청
      isAuthenticated.value = true; // 인증 상태 설정

      // 로컬 스토리지에 엑세스 토큰과 리프레시 토큰을 저장한다.
      const authHeader = response.headers['authorization'] || response.headers['Authorization'];

      if (authHeader) {
        const cleanedToken = cleanToken(authHeader)

        // JWT 토큰에서 만료시간 추출
        const tokenPayload = JSON.parse(atob(cleanedToken.split('.')[1]));
        const expiryInMs = tokenPayload.exp * 1000;
        const currentTime = Date.now();
        const expirationTime = expiryInMs + currentTime;

        // access_token과 만료시간을 localStorage에 저장
        localStorage.setItem('access_token', JSON.stringify({ access_token: cleanedToken, expiration: expirationTime}));

        console.log('로그인 성공, access_token 저장됨:', cleanedToken);
      } else {
        console.error('Authorization 헤더에 토큰이 없습니다.');
      }

      // 로컬 스토리지의 엑세스 토큰을 이용해 사용자 정보를 가져온다.
      user.value = getCurrentUser();

      return true
    } catch (err) {
      error.value = handleApiError(err); // 공통 에러 메시지 설정
    }
    return false;
  };

  // 로그아웃
  const logout = () => {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    user.value = {name: '', role: ''};
    isAuthenticated.value = false;
  };

  // Getter 정의
  const currentUser = computed(() => user.value);
  const errorMessage = computed(() => error.value);
  const isVerificationCodeSent = computed(() => verificationCodeSent.value);

  return {
    // State
    user,
    error,
    isAuthenticated,
    verificationCodeSent,

    // Methods
    checkEmail,
    register,
    loginAction,
    sendCodeAction,
    verifyAccount,
    logout,
    fetchUserInfo,

    // Getters
    currentUser,
    errorMessage,
    isVerificationCodeSent,
    isOwner,
    isWorker,
    getMemberName,
    getMemberRole,
    getMemberEmail
  };
});
