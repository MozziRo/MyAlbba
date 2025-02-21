<template>
  <form @submit.prevent="handleSubmit" class="base-form">

    <!-- 추가 필드를 위한 slot -->
    <slot name="email-verification-section"></slot>

    <!-- 공통 입력 필드 -->
    <h3 class="title">회원 정보 입력</h3>
    <div class="form-group">
      <div class="email-input-wrapper">
        <input
          type="email"
          id="email"
          v-model="formData.email"
          placeholder="이메일을 입력해주세요"
          required
        >
        <button
          type="button"
          class="check-duplicate-button"
          @click="checkDuplicateEmail"
        >
          중복확인
        </button>
      </div>
      <div :style="{ color: emailDuplicateResponse?.isDuplicate === false ? '#00B386' : 'rgb(255, 69, 0)' }">
        {{emailDuplicateResponse.message}}
      </div>
    </div>

    <div class="form-group">
      <input
        type="password"
        id="password"
        v-model="formData.password"
        placeholder="비밀번호를 입력해주세요"
        required
        @input="validatePassword"
      >
      <div class="password-validation" :class="{ 'invalid': !isPasswordValid }">
        {{ passwordMessage }}
      </div>
    </div>

    <div class="form-group">
      <input
        type="text"
        id="name"
        v-model="formData.name"
        placeholder="이름을 입력해주세요"
        required
      >
    </div>

    <div class="form-group">
      <input
        type="tel"
        id="phone"
        v-model="formData.phoneNumber"
        placeholder="전화번호를 - 없이 입력해주세요"
        required
      >
    </div>

    <div class="form-group">
      <input
        type="text"
        id="bank"
        v-model="formData.bank"
        placeholder="사용하는 은행을 입력해주세요"
        required
      >
    </div>

    <!-- 추가 필드를 위한 slot -->
    <slot name="additional-fields"></slot>

    <!-- 버튼 그룹 -->
    <div class="button-group">
      <button type="button" class="back-button" @click="$emit('prev')">
        이전
      </button>
      <button type="submit" class="next-button">
        가입하기
      </button>
    </div>
  </form>
</template>

<script setup>
import { reactive, ref } from 'vue'

// 부모에게 이전 또는 제출 이벤트가 발생했을 때 에밋으로 전달한다.
const emit = defineEmits(['prev', 'submit', 'check-duplicate-email'])
const props = defineProps({
  emailDuplicateResponse: Object
})

const formData = reactive({
  email: '',
  password: '',
  name: '',
  phoneNumber: '',
})

// 유효성 검사사
const isPasswordValid = ref(true)
const passwordMessage = ref('')

const validatePassword = () => {
  const password = formData.password
  const hasNumber = /\d/.test(password)
  const hasLetter = /[a-zA-Z]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password)

  if (password.length < 8) {
    isPasswordValid.value = false
    passwordMessage.value = '비밀번호는 최소 8자 이상이어야 합니다.'
  } else if (!(hasNumber && hasLetter && hasSpecial)) {
    isPasswordValid.value = false
    passwordMessage.value = '비밀번호는 숫자, 문자, 특수문자를 모두 포함해야 합니다.'
  } else {
    isPasswordValid.value = true
    passwordMessage.value = '사용 가능한 비밀번호입니다.'
  }
}

const handleSubmit = () => {
  if (!isPasswordValid.value) {
    return
  }
  if(props.emailDuplicateResponse && props.emailDuplicateResponse.isDuplicate === false) {
    emit('submit', formData) // 부모에게 formData 전달
  } else {
    props.emailDuplicateResponse.message = "이메일 중복확인이 필요합니다"
  }
}

// 이메일 중복 확인 처리
const checkDuplicateEmail = () => {
  emit('check-duplicate-email', formData.email)
}
</script>

<style>
.base-form {
  max-width: 480px;
  margin: 0 auto;
  padding: 2rem;
  background: #ffffff;
  border-radius: 12px;
  font-family: 'Pretendard';
}

.base-form .title {
  margin-bottom: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.base-form input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.3s ease;
}

input:focus {
  outline: none;
  border-color: #424769;  /* 이전: #00B386 */
  background: #ffffff;
}

.email-input-wrapper {
  display: flex;
  gap: 8px;
}

.email-input-wrapper input {
  flex: 1;
}

.base-form .check-duplicate-button {
  padding: 0 1.5rem;
  background-color: #424769;  /* 이전: #00B386 */
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

.check-duplicate-button:hover {
  background-color: #2D3250;  /* 이전: #007F5E */
}

.button-group {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.back-button, .next-button {
  flex: 1;
  padding: 0.875rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.base-form .back-button {
  border: 1px solid #ddd;
  background-color: white;
  color: #888888;
}

.back-button:hover {
  background-color: #f5f5f5;
}

.base-form .next-button {
  border: none;
  background-color: #424769;  /* 이전: #00B386 */
  color: white;
}

.next-button:hover {
  background-color: #2D3250;  /* 이전: #007F5E */
}

.password-validation {
  font-size: 0.9rem;
  margin-top: 0.5rem;
  color: #424769;  /* 이전: #00B386 */
}

.password-validation.invalid {
  color: #F9B17A;  /* 이전: rgb(255, 69, 0) */
}
</style>
