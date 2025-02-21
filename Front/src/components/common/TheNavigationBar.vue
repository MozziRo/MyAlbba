<template>
  <nav class="navigation">
    <div class="nav-left menu-links">
        <router-link to="/main" class="nav-item"><h1>내알빠다</h1></router-link>
        <router-link to="/notice" class="nav-item">공지사항</router-link>
        <router-link to="/schedule" class="nav-item">스케줄</router-link>
        <router-link to="/memo" class="nav-item">알림장</router-link>
        <router-link to="/salary" class="nav-item">월급</router-link>
      </div>
    <div class="nav-menu">
      <span class="user-name">{{ memberStore.user.name }} {{ memberStore.user.role }}님, 반갑습니다!</span>
      <div class="dropdown">
        <button class="menu-toggle" @click="toggleDropdown">
          <span class="menu-icon">☰</span>
        </button>
        <div class="dropdown-content" v-show="isDropdownOpen">
          <a href="#" @click="navigateTo('mypage')">마이페이지</a>
          <a href="#" @click="navigateTo('add-components')">위젯 추가</a>
          <a href="#" @click="navigateTo('video-interview')" v-if="memberStore.user && memberStore.user.role === '사장'">화상면접</a>
        </div>
      </div>
      <button @click="logout" class="logout-btn">로그아웃</button>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useMemberStore } from "@/store/member/memberStore.js"

const router = useRouter()
const memberStore = useMemberStore()
const isDropdownOpen = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const navigateTo = (path) => {
  isDropdownOpen.value = false
  router.push(`/${path}`)
}

const logout = () => {
  memberStore.logout()
  router.replace('/login')
}

onMounted(() => {
  memberStore.fetchUserInfo(); // 스토어에서 사용자 정보 가져오기
});

</script>

<style scoped>
.navigation {
  background-color: #ffffff;
  padding: 1rem 10vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
  font-family: 'Pretendard';
  min-height: 70px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

h1 {
 color: #424769;
 margin: 0;
}

.nav-left {
 display: flex;
 align-items: center;
 gap: 2rem;
}

.nav-brand h1 {
 margin: 0;
 font-size: 1.5rem;
 color: #333;
}

.nav-menu {
 display: flex;
 align-items: center;
 gap: 1rem;
}

.user-name {
 color: #666;
 font-size: 1rem;
 white-space: nowrap;
}

.logout-btn {
 padding: 0.5rem 1rem;
 font-size: 1rem;
 min-width: 100px;
 background-color: #424769;
 color: white;
 border: none;
 border-radius: 4px;
 cursor: pointer;
 transition: background-color 0.3s ease;
}

.logout-btn:hover {
 background-color: #2D3250;
}

.dropdown {
 position: relative;
 display: inline-block;
}

.dropdown-content {
 position: absolute;
 right: 0;
 background-color: #ffffff;
 min-width: 160px;
 box-shadow: 0 8px 16px rgba(0,0,0,0.1);
 border-radius: 4px;
 z-index: 1001;
}

.dropdown-content a {
 color: #333;
 padding: 12px 16px;
 text-decoration: none;
 display: block;
 transition: background-color 0.3s;
}

.dropdown-content a:hover {
 background-color: #f5f5f5;
 color: #424769;
}

.menu-toggle {
 background: none;
 border: none;
 font-size: 1.5rem;
 cursor: pointer;
 padding: 0.5rem;
 display: flex;
 align-items: center;
 justify-content: center;
}

.menu-icon {
 color: #333;
}

.navigation {
  background-color: #ffffff;
  padding: 1rem 10vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
  font-family: 'Pretendard';
  min-height: 70px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

/* 메뉴 링크 영역 */
.menu-links {
  display: flex;
  gap: 20px;
  flex-wrap: nowrap; /* 줄바꿈 방지 */
  overflow-x: auto; /* 필요시 가로 스크롤 */
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}

.menu-links::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera */
}

/* 네비게이션 메뉴 아이템 */
.nav-item {
  color: #666;
  text-decoration: none;
  font-size: 1rem;
  padding: 8px 12px;
  position: relative;
  transition: color 0.3s ease;
  white-space: nowrap;
}

/* 로고 스타일 */
.nav-item h1 {
  color: #424769;
  margin: 0;
}

/* 로고를 제외한 메뉴 아이템에 밑줄 효과 적용 */
.nav-item:not(:has(h1))::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: var(--primary);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.nav-item:not(:has(h1)):hover::after {
  transform: scaleX(0.8);
}

.nav-item:not(:has(h1)).router-link-active {
  color: var(--primary);
  font-weight: 500;
}

.nav-item:not(:has(h1)).router-link-active::after {
  transform: scaleX(1);
}

/* 반응형 스타일 */
@media (max-width: 768px) {
  .navigation {
    padding: 1rem 5vw;
  }

  .menu-links {
    gap: 10px;
  }

  .nav-item {
    font-size: 0.875rem;
    padding: 8px 8px;
  }

  .user-name {
    font-size: 0.875rem;
  }

  .logout-btn {
    padding: 0.4rem 0.8rem;
    font-size: 0.875rem;
    min-width: 80px;
  }
}

@media (max-width: 480px) {
  .user-name {
    display: none;
  }
}
</style>
