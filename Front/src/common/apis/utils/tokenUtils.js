/**
 * JWT 토큰을 디코드하여 페이로드를 반환하는 함수
 * @param {string} token - JWT 토큰
 * @returns {Object} 디코드된 토큰 정보 (name, email, role)
 */
export const decodeToken = (token) => {
  try {
    // JWT의 페이로드 부분만 추출 (토큰은 header.payload.signature 형태)
    const base64Payload = token.split('.')[1];
    // Base64 디코드 후 UTF-8로 변환 (한글 깨짐 방지)
    const payload = decodeURIComponent(
      atob(base64Payload)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    // JSON 파싱
    const parsedPayload = JSON.parse(payload);

    // 사용자 정보 반환
    return {
      name: parsedPayload.userName,
      email: parsedPayload.userEmail,
      role: parsedPayload.userRole,
      issuedAt: parsedPayload.iat,    // 필요하다면 발급 시간
      expiration: parsedPayload.exp   // 만료 시간
    };
  } catch (error) {
    console.error('토큰 디코드 실패:', error);
    return null;
  }
};

export const getTokenFromLocalStorage = () => {
  if (localStorage.getItem('access_token')) {
    return localStorage.getItem('access_token')
  }
  return null
}

/**
 * Bearer 토큰에서 실제 JWT 토큰만 추출하는 함수
 * @param {string} token - Bearer 토큰
 * @returns {string} 정제된 JWT 토큰
 */
export const cleanToken = (token) => {
  if (!token) return null;
  return token.replace(/^Bearer\s+/, '');
};

/**
 * 현재 저장된 액세스 토큰에서 사용자 정보를 가져오는 함수
 * @returns {Object|null} 사용자 정보 또는 null
 */
export const getCurrentUser = () => {
  const tokenString = localStorage.getItem('access_token');

  if(!tokenString) return null;

  const tokenObject = JSON.parse(tokenString);
  return decodeToken(tokenObject.access_token);
};
