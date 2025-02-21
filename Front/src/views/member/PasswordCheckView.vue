<template>
  <div class="password-check">
    <div class="auth-card">
      <h2>본인 확인</h2>
      <p class="description">개인정보 보호를 위해 비밀번호를 다시 한 번 입력해주세요.</p>

      <form @submit.prevent="verifyPassword" class="auth-form">
        <div class="form-group">
          <label>비밀번호</label>
          <input
            v-model="passwordJson.password"
            type="password"
            class="form-input"
            placeholder="비밀번호를 입력해주세요"
            :class="{ 'error': error }"
            autocomplete="current-password"
          />
          <span v-if="error" class="error-message">{{ error }}</span>
        </div>

        <button
          type="submit"
          class="submit-button"
          :disabled="isSubmitting || !passwordJson.password"
        >
          {{ isSubmitting ? '확인 중...' : '확인' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { passwordCheck } from '@/common/apis/api/auth' // API 구현 필요

const router = useRouter()
const passwordJson = ref({
  password: ''
})
const error = ref('')
const isSubmitting = ref(false)

// 비밀번호 확인
const verifyPassword = async () => {
  if (passwordJson.value.password === '') return

  try {
    isSubmitting.value = true
    error.value = ''

    const status = await passwordCheck(passwordJson.value)
    if (status === 200) {
      // TODO 토큰 생성 필요
      sessionStorage.setItem('verification_token', 'true')
      router.push({ name: 'myPage' })
    } else {
      error.value = '비밀번호가 일치하지 않습니다.'
    }
  } catch (err) {
    error.value = '다시 시도해 주세요.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<style scoped>
.password-check {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20px;
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  width: 100%;
  max-width: 400px;
}

h2 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 24px;
}

.description {
  color: #666;
  margin-bottom: 32px;
  font-size: 14px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-input {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.2s;
}

.form-input:focus {
  border-color: var(--primary);
  outline: none;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.1);
}

.form-input.error {
  border-color: #ff4d4f;
}

.error-message {
  color: #ff4d4f;
  font-size: 12px;
}

.submit-button {
  padding: 12px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-button:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.submit-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
