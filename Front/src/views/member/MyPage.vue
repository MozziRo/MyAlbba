<template>
  <div class="my-page">
    <h3>나의 <span style="color: var(--primary);">Albba</span></h3>

    <OwnerMyPage v-if="userRole === 'owner'" />
    <WorkerMyPage v-else-if="userRole === 'worker'" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/store/member/memberStore'
import OwnerMyPage from '@/components/myPage/owner/OwnerMyPage.vue'
import WorkerMyPage from '@/components/myPage/worker/WorkerMyPage.vue'
import { getCurrentUser } from '@/common/apis/utils/tokenUtils'

const memberStore = useMemberStore()
const userRole = ref("")

onMounted(() => {
  userRole.value = getCurrentUser().role
})


</script>

<style scoped>
.my-page {
  padding: 20px;
}

h3 {
  margin-bottom: 20px;
  color: #333;
  text-align: center; /* 가운데 정렬 */
  font-family: 'Arial', sans-serif; /* 폰트 패밀리 설정 */
  font-size: 28px; /* 폰트 크기 조정 */
}

.tab-container {
  max-width: 600px;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddd;
}

.tab-button {
  padding: 10px 20px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 16px;
  color: #666;
}

.tab-button.active {
  color: var(--primary);
  border-bottom: 2px solid var(--primary);
}

.form-container {
  max-width: 500px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #666;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-input:focus {
  border-color: var(--primary);
  outline: none;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.submit-button {
  padding: 10px 20px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.submit-button:hover {
  background-color: var(--primary-dark);
}

.delete-button {
  padding: 10px 20px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.delete-button:hover {
  background-color: #c82333;
}
</style>
