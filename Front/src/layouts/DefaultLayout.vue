<template>
  <div class="default-layout">
    <TheNavigationBar class="nav-fixed" />
    <div class="main-container">
      <router-view />
    </div>
    <Footer />
  </div>
</template>

<script setup>
import Footer from '@/components/common/TheFooter.vue'
import TheNavigationBar from '@/components/common/TheNavigationBar.vue'
</script>

<style scoped>
.default-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.nav-fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 1000;
}

.main-container {
  flex: 1;
  margin-top: 70px; /* Nav 높이 */
  margin-bottom: 60px; /* Footer 높이 */
  min-height: calc(100vh - 120px); /* 전체 높이에서 Nav와 Footer 높이를 뺌 */
}

.footer-fixed {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 60px;
  z-index: 1000;
}
</style>
