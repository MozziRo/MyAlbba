<!-- 알바생 마이페이지 -->
<template>
  <div class="worker-profile">
    <div class="profile-section">
      <div class="profile-header">
        <h3><span style="color: #00B386;">{{ workerInfo.name }}</span>님의 정보</h3>
      </div>

      <div class="info-grid">
        <!-- 수정 불가능한 정보 -->
        <div class="info-group readonly">
          <label>이메일</label>
          <div class="info-value">{{ workerInfo.email }}</div>
        </div>

        <div class="info-group readonly">
          <label>시급</label>
          <div class="info-value">{{ formatCurrency(workerInfo.hourlySalary) }}원</div>
        </div>

        <div class="info-group readonly">
          <label>세금</label>
          <div class="info-value">{{ workerInfo.tax }}%</div>
        </div>

        <div class="info-group readonly">
          <label>보험</label>
          <div class="info-value">{{ workerInfo.insurance }}%</div>
        </div>

        <div class="info-group readonly">
          <label>주휴수당</label>
          <div class="info-value">{{ workerInfo.holidayPay ? '지급' : '미지급' }}</div>
        </div>
      </div>

      <!-- 수정 가능한 정보 폼 -->
      <form @submit.prevent="updateWorkerPersonalInfo" class="edit-form">
        <span></span>

        <div class="form-group">
          <label>비밀 번호</label>
          <input
            v-model="editableInfo.password"
            type="password"
            class="form-input"
            placeholder="비밀번호를 입력해주세요"
            pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$"
            title="최소 8자, 영문자, 숫자, 특수문자를 포함해야 합니다"
          />
        </div>
        <!-- 휴대폰 번호 -->
        <div class="form-group">
          <label>휴대폰 번호</label>
          <input
            v-model="editableInfo.phoneNumber"
            type="tel"
            class="form-input"
            placeholder="- 없이 입력해주세요"
            pattern="^01[0-9]{8,9}$"
            title="올바른 휴대폰 번호를 입력해주세요"
            maxlength="11"
          />
        </div>

        <div class="form-group">
          <label>은행</label>
          <input
            v-model="editableInfo.bank"
            type="text"
            class="form-input"
            placeholder="은행명을 입력해주세요"
            pattern="^[가-힣]{2,}은행$"
            title="올바른 은행명을 입력해주세요"
          />
        </div>

        <div class="form-group">
          <label>계좌번호</label>
          <input
            v-model="editableInfo.account"
            type="text"
            class="form-input"
            placeholder="- 없이 입력해주세요"
            pattern="^[0-9]+$"
            title="숫자만 입력 가능합니다"
          />
        </div>

        <button type="submit" class="submit-button">
          정보 업데이트
        </button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getWorkerMyInfo, updateWorkerInfo } from '@/common/apis/api/member'
import { Worker, WorkerUpdateRequest } from '@/common/types/member'

// 전체 정보를 담는 ref
const workerInfo = ref<Worker>({
  email: '-1',
  name: '비회원',
  phoneNumber: '010-0000-0000',
  bank: '은행',
  account: '1234-5678-9012',
  hourlySalary: 10000,
  tax: 3.3,
  insurance: 1.5,
  holidayPay: false
})

// 수정 가능한 필드만 computed로 관리
const editableInfo = computed<WorkerUpdateRequest>(() => ({
  password: "",  // password는 별도 처리 필요할 수 있음
  phoneNumber: workerInfo.value.phoneNumber,
  bank: workerInfo.value.bank,
  account: workerInfo.value.account
}))

// 내 정보 가져오기
const fetchWorkerInfo = async () => {
  try {
    const data = await getWorkerMyInfo()
    workerInfo.value = data
  } catch (error) {
    console.error('알바생 정보 로딩 실패', error)
  }
}
// 마운트 시 내 정보 가져오기
onMounted(async () => {
  await fetchWorkerInfo()
})


// 정보 업데이트 PUT
const updateWorkerPersonalInfo = async () => {
  try {
    console.log(editableInfo.value)
    await updateWorkerInfo(editableInfo.value)
    // 성공 시 전체 정보 다시 불러오기
    await fetchWorkerInfo()
  } catch (error) {
    console.error('정보 업데이트 실패', error)
  }
}

// TODO: util에 몰아 넣고 싶다..
const formatCurrency = (value: number) => {
  return value.toLocaleString('ko-KR')
}

</script>

<style scoped>
.worker-profile {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-section {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 32px;
}

.profile-header {
  margin-bottom: 32px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.profile-header h3 {
  color: #333;
  font-size: 24px;
  margin: 0;
}

.profile-header h3 span {
  color: #424769; 
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
  margin-bottom: 40px;
}

.info-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.info-group label {
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.info-value {
  font-size: 16px;
  color: #333;
  font-weight: 600;
}

.edit-form {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.edit-form h4 {
  color: #333;
  margin-bottom: 24px;
  font-size: 18px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #666;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.2s ease;
}

.form-input:focus {
  border-color:  #424769; 
  outline: none;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.1); 
}

.tab-button.active {
  background-color: #424769;  
}

.submit-button {
  width: 100%;
  padding: 14px;
  background-color:#424769; 
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.submit-button:hover {
  background-color:  #2D3250;
}

.readonly .info-value {
  padding: 8px 0;
}
</style>
