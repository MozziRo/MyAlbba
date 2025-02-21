<template>
  <div class="salary-view">
    <OwnerSalary v-if="userRole === 'owner'" />
    <WorkerSalary v-else />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/store/member/memberStore'
import OwnerSalary from '@/components/salary/owner/OwnerSalary.vue'
import WorkerSalary from '@/components/salary/worker/WorkerSalary.vue'
import { getCurrentUser } from '@/common/apis/utils/tokenUtils'

const memberStore = useMemberStore()
const userRole = ref("")

onMounted(() => {
  userRole.value = getCurrentUser().role;
})

</script>

<style scoped>
.salary-view {
  padding: 20px;
}
</style>
