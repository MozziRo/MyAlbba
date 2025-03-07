<template>
  <div class="features-grid">
    <!-- 스케줄 카드 -->
    <div class="widget">
        <h3 class="title">
            <v-icon color="#666" size="24">
                mdi-calendar
            </v-icon>
            전체 스케줄
        </h3>
        <div class="feature-card" @click="goToSchedule">
            <ScheduleCard />
        </div>
    </div>
    <!-- 알림장 카드 -->
    <div class="widget">
        <h3 class="title">
            <v-icon color="#666" size="24">
                mdi-pencil-box-outline
            </v-icon>
            알림장
        </h3>
        <div class="feature-card" @click="goToMemo">
            <MemoPreviewCard />
        </div>
    </div>
    <!-- 월급 -->
    <div class="widget">
        <h3 class="title">
            <v-icon color="#666" size="24">
                mdi-cash-multiple
            </v-icon>
            월급
        </h3>
        <div class="feature-card" @click="goToSalary">
            <SalaryPreviewCard />
        </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import ScheduleCard from '@/views/Schedule/ScheduleCard.vue'
import MemoPreviewCard from '@/components/main/MemoPreviewCard.vue'
import SalaryPreviewCard from '@/components/main/SalaryPreviewCard.vue'

const router = useRouter()

const goToSchedule = () => {
  router.push({ name: 'schedules' });
}

const goToMemo = () => {
  router.push({ name: 'memo' });
}

const goToSalary = () => {
  router.push({ name: 'salary' });
}
</script>

<style scoped>
.title {
  color: #424769;
  padding: 1rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
  margin-top: 1rem;
  padding: 0 1.5rem;
}

.widget {
  /* 모든 위젯이 똑같은 공간을 차지하도록 설정 */
  width: 100%;
  max-width: none;
}

.feature-card {
  height: 375px;
  width: 100%; /* 모든 카드가 동일한 너비를 가지도록 설정 */
  background: white;
  padding: 1.5rem;
  border-radius: 30px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s;
  border: 1px solid #e3e3e3;
  box-shadow: 0 5px 10px rgba(66, 71, 105, 0.2);
  /* 카드 크기를 고정 */
  box-sizing: border-box;
}

.feature-card:hover {
  transform: translateY(-4px);
}
</style>
