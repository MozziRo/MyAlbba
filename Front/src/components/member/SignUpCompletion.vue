<template>
  <div class="completion">
    <div class="success-icon">✓</div>
    <h2>가입이 완료되었습니다!</h2>
    <p>로그인 후 서비스를 이용해주세요.</p>
    <button class="login-button" @click="goToLogin">
      로그인하기
    </button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLogin = () => {
  router.replace('/login')
}
</script>

<style scoped>
.completion {
  text-align: center;
  padding: 40px 0;
  font-family: 'Pretendard';
}

.success-icon {
  font-size: 48px;
  color: #424769;  /* 이전: #00B386 */
  margin-bottom: 20px;
}

.login-button {
  margin-top: 20px;
  padding: 15px 40px;
  border: none;
  background-color: #424769;  /* 이전: #00B386 */
  color: white;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-button:hover {
  background-color: #2D3250;  /* 이전: #007F5E */
}
</style>
