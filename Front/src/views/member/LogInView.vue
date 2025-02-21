<template>
    <div class="auth-container">
      <div class="auth-box">
        <h2>내알빠다</h2>
        <form @submit.prevent="handleLogin">
          <div class="input-group">
            <input
              type="email"
              id="email"
              v-model="email"
              placeholder="이메일을 입력해주세요"
              required
            >
          </div>
          <div class="input-group">
            <input
              type="password"
              id="password"
              v-model="password"
              placeholder="비밀번호를 입력해주세요"
              required
            >
          </div>

          <button type="submit" class="submit-btn">로그인</button>
        </form>
        <div class="links">
          <RouterLink to="/signup" class="link">회원가입</RouterLink>
          <RouterLink to="/" class="link">홈으로</RouterLink>
        </div>
      </div>
    </div>
  </template>

  <script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useMemberStore } from '@/store/member/memberStore'

  const email = ref('')
  const password = ref('')

  const memberStore = useMemberStore()
  const router = useRouter()

  const handleLogin = async () => {
    console.log('로그인 버튼 클릭')
    const formData = new FormData();
    formData.append('username', email.value);
    formData.append('password', password.value);

    const result = await memberStore.loginAction(formData);

    // 로그인 성공 시 메인 페이지로 이동
    if (result) {
      router.push({ name: 'main' })
    } else {
      alert('로그인 실패')
    }
  }
  </script>

  <style scoped>
  .auth-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    /* background-color: #f5f5f5; */
    padding: 1rem;
    font-family: 'Pretendard'
  }

  .auth-box {
    background: white;
    padding: 2.5rem;
    border: 1px solid #ddd;
    border-radius: 12px;
    width: 100%;
    max-width: 420px;
  }

  h2 {
    text-align: center;
    margin-bottom: 2rem;
    color: var(--primary);
    font-weight: 600;
    font-size: 1.75rem;
  }

  .input-group {
    margin-bottom: 1.5rem;
  }

  label {
    display: block;
    margin-bottom: 0.5rem;
    color: #333;
    font-size: 0.9rem;
    font-weight: 500;
  }

  input {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 0.95rem;
    transition: all 0.3s ease;
  }

  input:focus {
    outline: none;
    border-color: var(--primary);
    background: #ffffff;
  }

  .submit-btn {
    width: 100%;
    padding: 0.875rem;
    background-color: var(--primary);
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.3s ease;
  }

  .submit-btn:hover {
    background-color: var(--primary-dark);
  }

  .links {
    margin-top: 1.5rem;
    display: flex;
    justify-content: space-between;
    padding: 0 0.5rem;
  }

  .link {
    color: var(--neutral-dark);
    text-decoration: none;
    font-size: 0.9rem;
    transition: color 0.3s ease;
  }

  .link:hover {
    color: var(--primary);
  }
  </style>
