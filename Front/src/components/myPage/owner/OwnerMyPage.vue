<template>
  <div class="owner-section">
    <div class="profile-section">
      <div class="profile-header">
        <h3><span style="color: #424769;">사장님</span> 정보</h3>
      </div>

      <!-- 내 정보 섹션 -->
      <div class="info-grid">
        <div class="info-group readonly">
          <label>이메일</label>
          <div class="info-value">{{ ownerPersonal.email }}</div>
        </div>
        <div class="info-group readonly">
          <label>이름</label>
          <div class="info-value">{{ ownerPersonal.name }}</div>
        </div>
        <div class="info-group readonly">
          <label>전화번호</label>
          <div class="info-value">{{ ownerPersonal.phoneNumber }}</div>
        </div>
      </div>

      <!-- 가게 정보 섹션 -->
      <div class="store-section">
        <h4>가게 정보</h4>
        <div class="info-grid">
          <div class="info-group readonly">
            <label>가게명</label>
            <div class="info-value">{{ storeInfo.name }}</div>
          </div>
          <div class="info-group readonly">
            <label>주소</label>
            <div class="info-value">{{ storeInfo.address }}</div>
          </div>
          <div class="info-group readonly">
            <label>가게 전화번호</label>
            <div class="info-value">{{ storeInfo.phoneNumber }}</div>
          </div>
          <div class="info-group readonly">
            <label>급여 지급일</label>
            <div class="info-value">매월 {{ storeInfo.payday }}일</div>
          </div>
        </div>
      </div>

      <!-- 정보 수정 탭 -->
      <div class="edit-section">
        <div class="tabs">
          <button
            :class="['tab-button', { active: activeTab === 'personal' }]"
            @click="activeTab = 'personal'"
          >
            내 정보 수정
          </button>
          <button
            :class="['tab-button', { active: activeTab === 'store' }]"
            @click="activeTab = 'store'"
          >
            가게 정보 수정
          </button>
        </div>

        <!-- 내 정보 수정 폼 -->
        <div v-show="activeTab === 'personal'" class="edit-form">
          <form @submit.prevent="updateOwnerPersonalInfo">
            <div class="form-group">
              <label>비밀번호</label>
              <input
                v-model="editableInfo.password"
                type="password"
                class="form-input"
                placeholder="새 비밀번호를 입력해주세요"
                pattern="^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$"
                title="최소 8자, 영문자, 숫자, 특수문자를 포함해야 합니다"
              />
            </div>
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
            <button type="submit" class="submit-button">정보 업데이트</button>
          </form>
        </div>

        <!-- 가게 정보 수정 폼 -->
        <div v-show="activeTab === 'store'" class="edit-form">
          <form @submit.prevent="updateStoreInfo">
            <div class="form-group">
              <label>가게 이름</label>
              <input
                v-model="storeEditableInfo.name"
                type="text"
                class="form-input"
                placeholder="가게 이름을 입력해주세요"
                pattern="^[가-힣a-zA-Z0-9\s]{1,}$"
                title="한글, 영문, 숫자만 입력 가능합니다"
              />
            </div>
            <div class="form-group">
              <label>가게 주소</label>
              <input
                v-model="storeEditableInfo.address"
                type="text"
                class="form-input"
                placeholder="가게 주소를 입력해주세요"
                pattern="^[가-힣a-zA-Z0-9\s-,()]{1,}$"
                title="올바른 주소를 입력해주세요"
              />
            </div>
            <div class="form-group">
              <label>가게 전화번호</label>
              <input
                v-model="storeEditableInfo.phoneNumber"
                type="tel"
                class="form-input"
                placeholder="- 없이 입력해주세요"
                pattern="^[0-9]{9,11}$"
                title="올바른 전화번호를 입력해주세요"
                maxlength="11"
              />
            </div>
            <div class="form-group">
              <label>급여 지급일</label>
              <input
                v-model="storeEditableInfo.payday"
                type="number"
                min="1"
                max="31"
                class="form-input"
                placeholder="1-31 사이의 숫자를 입력해주세요"
              />
            </div>
            <div class="button-group">
              <button type="submit" class="submit-button">가게 정보 업데이트</button>
              <button type="button" class="delete-button" @click="deleteStoreInfo">
                가게 정보 삭제
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { getStore, updateStore } from '@/common/apis/api/store'
import { getOwnerMyInfo, updateOwnerInfo } from '@/common/apis/api/member'
import { Owner, OwnerUpdateRequest, StoreInfo } from '@/common/types/member'

const activeTab = ref('personal')

// ref
const ownerPersonal = ref<Owner>({
  email: '',
  name: '',
  phoneNumber: '',
})

// 리퀘스트와 형식 같음
const storeInfo = ref<StoreInfo>({
  name: '',
  address: '',
  phoneNumber: '',
  payday: '',
})

// 리퀘스트
const storeEditableInfo = computed<StoreInfo>(() => ({
  name: '',
  address: '',
  phoneNumber: '',
  payday: '',
}))


const editableInfo = computed<OwnerUpdateRequest>(() => ({
  password: '',
  phoneNumber: ownerPersonal.value.phoneNumber,
}))

const fetchOwnerInfo = async () => {
  try {
    const ownerData = await getOwnerMyInfo()
    // 비밀번호 제외하고 내 정보 업데이트

    ownerPersonal.value.email = ownerData.email
    ownerPersonal.value.name = ownerData.name
    ownerPersonal.value.phoneNumber = ownerData.phoneNumber
  } catch (error) {
    console.error('오너 정보 로딩 실패', error)
  }
}

const fetchStoreInfo = async () => {
  try {
    const storeData = await getStore()
    storeInfo.value.name = storeData.name
    storeInfo.value.address = storeData.address
    storeInfo.value.phoneNumber = storeData.phoneNumber
    storeInfo.value.payday = storeData.payday

  } catch (error) {
    console.error('가게 정보 로딩 실패', error)
  }
}

// 마운트 되었을 때 오너 정보와 가게 정보를 가져온다.
onMounted(async () => {
  await fetchOwnerInfo()
  await fetchStoreInfo()
})

// 업데이트 함수들 PUT
const updateOwnerPersonalInfo = async () => {
  try {
    console.log(editableInfo.value)
    await updateOwnerInfo(editableInfo.value)
    await fetchOwnerInfo()
  } catch (error) {
    console.error('내 정보 업데이트 실패', error)
  }
}


const updateStoreInfo = async () => {
  try {
    console.log(storeEditableInfo.value)
    await updateStore(storeEditableInfo.value)
    await fetchStoreInfo()
  } catch (error) {
    console.error('가게 정보 업데이트 실패', error)
  }
}

const deleteStoreInfo = async () => {
  // 구현 예정
}
</script>

<style scoped>
.owner-section {
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

.store-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.store-section h4 {
  color: #333;
  margin-bottom: 24px;
  font-size: 18px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
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

.edit-section {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #eee;
}

.tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
}

.tab-button {
  padding: 10px 20px;
  border: none;
  background-color: #f0f0f0;
  border-radius: 8px;
  cursor: pointer;
  font-size: 15px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.tab-button.active {
  background-color: #424769; 
  color: white;
}

.edit-form {
  margin-top: 24px;
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
  border-color: #424769;
  outline: none;
  box-shadow: 0 0 0 2px rgba(66, 71, 105, 0.1); 
}

.button-group {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.submit-button {
  flex: 1;
  padding: 14px;
  background-color:  #424769;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.delete-button {
  padding: 14px 24px;
  background-color: #dc3545;
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

.delete-button:hover {
  background-color: #c82333;
}

.readonly .info-value {
  padding: 8px 0;
}
</style>
