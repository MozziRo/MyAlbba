<template>
  <div class="footer-page-layout">
    <header class="layout-header">
      <div class="header-content">
        <RouterLink to="/" class="home-button">내알빠다</RouterLink>
        <h1 class="service-info-title">Albba 서비스 정보</h1>
      </div>
    </header>
    <main class="layout-content">
      <router-view />
    </main>
    <TheFooter />
  </div>
</template>

<script setup>
import TheFooter from '@/components/common/TheFooter.vue'
</script>

<style scoped>
.footer-page-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.layout-header {
  background-color: #333;
  color: white;
  padding: 1rem;
  text-align: center;
  font-family: 'Pretendard', sans-serif;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: center; /* 가운데 정렬 */
}

.service-info-title {
  margin-left: 10px;
  font-size: 1.5rem; /* 글씨 크기 키우기 */
}

.layout-content {
  flex: 1;
  padding: 2rem;
  background-color: #f8f8f8;
  font-family: 'Pretendard', sans-serif;
}

.home-button {
  color: var(--primary);
  text-decoration: none;
  font-size: 1rem; /* 반응형 폰트 크기 */
  padding: 8px 12px;
  border-radius: 5px;
  white-space: nowrap;
  text-align: left; /* 왼쪽으로 정렬 */
}
</style>
