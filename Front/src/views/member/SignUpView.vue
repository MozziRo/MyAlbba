<template>
  <div class="signup-container">
    <!-- 프로그레스 바 -->
    <ProgressBar
      :steps="progressSteps"
      :current-step="currentStep"
    />

    <!-- 진행상황에 따른 컴포넌트를 동적으로 렌더링한다.-->
    <div class="signup-content">
      <component
        :is="currentStepComponent"
        v-model="selectedType"
        :emailDuplicateResponse="emailDuplicateResponse"
        @prev="prevStep"
        @next="nextStep"
        @submit="handleSubmit"
        @check-duplicate-email="hanldeDuplicateEmail"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useMemberStore } from '@/store/member/memberStore'
// import { useRouter } from 'vue-router'
import ProgressBar from '@/components/common/ProgressBar.vue'
import SignUpTypeSelection from '@/components/member/SignUpTypeSelection.vue'
import workerInfoForm from '@/components/member/WorkerInfoForm.vue'
import ownerWorkerForm from '@/components/member/OwnerInfoForm.vue'
import SignUpCompletion from '@/components/member/SignUpCompletion.vue'

// const router = useRouter() // 라우터 사용
const currentStep = ref(1) // 1스텝으로 시작
const selectedType = ref('') // 현재 선택된 타입
const formData = ref({}); // 클라이언트에게 입력 받은 폼데이터
const emailDuplicateResponse = ref({}); // 가입하려는 이메일이 중복되었는지 체크

const memberStore = useMemberStore();

// 현재 단계에 따른 컴포넌트 결정
const currentStepComponent = computed(() => {
  switch (currentStep.value) {
    case 1:
      return SignUpTypeSelection
    case 2:
      return selectedType.value === 'employee'
        ? workerInfoForm
        : ownerWorkerForm
    case 3:
      return SignUpCompletion
    default:
      return SignUpTypeSelection
  }
})

// 프로그레스 설정
const progressSteps = [
  '회원유형 선택',
  '정보입력',
  '가입완료'
]

// 다음 스텝으로
const nextStep = () => {
  currentStep.value++
}

// 이전 스텝으로
const prevStep = () => {
  currentStep.value--
}

//////////////////////////////////// 로직 ////////////////////////////////////////

// 서버에 formData 전달
const handleSubmit = async (baseData) => {

  console.log(baseData) // debug

  formData.value = baseData; // 폼 데이터 받기
  const signUpResult = await memberStore.register(formData.value); // API 호출 (회원가입)

  if (signUpResult) {
    console.log('회원가입 성공:', memberStore.currentUser);
    nextStep(); // 성공 시 다음 단계로

  } else {
    alert('회원가입에 실패했습니다. 다시 시도해 주세요.');
  }
};



// 이메일 중복 검사
const hanldeDuplicateEmail = async (emailObj) => {
  try {
    emailDuplicateResponse.value = await memberStore.checkEmail(emailObj); // API 호출 (이메일 중복 체크)
  } catch (error) {
    console.error('이메일 중복 체크 실패:', error);
    alert(error.message || '이메일 중복 체크에 실패하였습니다. 다시 시도해 주세요.');
  }
}


/////////////////////////////////////////////////////////////////////////////////

</script>

<!-- 스타일 -->
<style>
.signup-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 20px;
}

.signup-content {
  margin-top: 40px;
}

.completion {
  text-align: center;
  padding: 40px 0;
}

.success-icon {
  font-size: 48px;
  color: #424769;
  margin-bottom: 20px;
}

.login-button {
  margin-top: 20px;
  padding: 15px 40px;
  border: none;
  background-color: #424769;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}

.progress-bar {
  height: 4px;
  background-color: var(--primary);
}

.next-button {
  background-color: var(--primary);
  color: white;
}

.next-button:hover {
  background-color: var(--primary-dark);
}
</style>
