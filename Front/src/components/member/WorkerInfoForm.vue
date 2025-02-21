<template>
  <baseInfoForm @prev="$emit('prev')" @submit="handleSubmit" @check-duplicate-email="handleEmail" :emailDuplicateResponse="emailDuplicateResponse">
    <template #email-verification-section>
      <div class="form-group">
        <h3 class="title">사장님과 연동하기</h3>
        <div class="email-input-wrapper">
          <input
            type="email"
            id="ownerEmail"
            v-model="ownerEmail"
            placeholder="사장님 이메일을 입력하세요"
            required
            :disabled="isVerified"
          >
          <button
            type="button"
            @click="sendVerificationCode"
            v-if="!isVerified"
          >
            인증번호 발송
          </button>
        </div>
        <div :style="{ color: ownerEmail === false ? '#00B386' : 'rgb(255, 69, 0)' }">
          {{ sendVerificationCodeResponse }}
        </div>
      </div>
      <div class="form-group" v-if="isCodeSent && !isVerified">
        <div class="email-input-wrapper">
          <input
            type="text"
            id="verificationCode"
            v-model="verificationCode.code"
            placeholder="사장님께 전송된 인증코드를 입력해주세요"
            required
          >
          <button
            type="button"
            @click="checkVerificationCode"
          >
            인증번호 확인
          </button>
        </div>
        <div :style="{ color: ownerEmail === false ? '#00B386' : 'rgb(255, 69, 0)' }">
          {{ checkVerificationCodeResponse }}
        </div>
      </div>
    </template>
    <template #additional-fields>
        <div class="form-group">
          <input
            type="text"
            id="account"
            v-model="workerData.account"
            placeholder="계좌번호를 - 없이 입력해주세요"
            required
          >
        </div>
    </template>
  </baseInfoForm>
</template>

<script setup>
import { ref, defineProps } from 'vue';
import baseInfoForm from './baseInfoForm.vue';
import { useMemberStore } from '@/store/member/memberStore';

const emit = defineEmits(['prev', 'submit', 'check-duplicate-email'])
const memberStore = useMemberStore()

const ownerEmail = ref('')
const isCodeSent = ref(false)
const isVerified = ref(false)
const verificationCode = ref({
  ownerEmail : '',
  code: ''
});

const sendVerificationCodeResponse = ref('')
const checkVerificationCodeResponse = ref('')

const props = defineProps({
  emailDuplicateResponse: Object
})

const workerData = ref({
  account: '',
});

const handleSubmit = (baseData) => {
  console.log(baseData);
  emit('submit', { role: 'worker', ownerEmail: ownerEmail.value, ...baseData, ...workerData.value })
}

const handleEmail = (inputEmail) => {
  console.log(inputEmail);
  emit('check-duplicate-email', {
    role: 'worker',
    email: inputEmail
  })
}

const sendVerificationCode = async () => {
  if (!ownerEmail.value) {
    sendVerificationCodeResponse.value = "사장님 이메일을 입력해주세요"
    return;
  }

  sendVerificationCodeResponse.value = "인증번호 전송 중입니다. 잠시만 기다려주세요.";
  try {
    await memberStore.sendCodeAction(ownerEmail.value);
    isCodeSent.value = true;
    verificationCode.value.ownerEmail = ownerEmail.value;
    sendVerificationCodeResponse.value = "";
  } catch (error) {
    alert('인증번호 발송에 실패했습니다.');
  }
}

const checkVerificationCode = async () => {
  if(!verificationCode.value) {
    checkVerificationCodeResponse.value = "인증번호를 입력해주세요"
    return;
  }

  checkVerificationCodeResponse.value = ''

  try {
    isVerified.value = await memberStore.verifyAccount(verificationCode.value);
    if(!isVerified.value) {
      checkVerificationCodeResponse.value = "잘못된 인증번호입니다, 인증번호를 다시 발송하여 주십시오"
    } else {
      checkVerificationCodeResponse.value = "인증 확인되었습니다."
    }
  } catch (error) {
    alert('인증번호 확인에 실패했습니다.');
  }
}

</script>

<style scoped>
.base-form button {
  padding: 0 1.5rem;
  background-color: #424769;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #2D3250;
}

</style>
