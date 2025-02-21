<template>
  <div class="not-found">
    <h1>페이지를 찾을 수 없습니다</h1>
    <p>요청하신 페이지가 존재하지 않습니다.</p>
    <router-link to="/">홈으로 돌아가기</router-link>
  </div>
</template>

<style scoped>
.not-found {
  text-align: center;
  margin-top: 50px;
}

.not-found h1 {
  font-size: 2rem;
  color: #f44336;
}

.not-found p {
  font-size: 1.2rem;
}

.not-found a {
  color: #4CAF50;
  text-decoration: underline;
}
</style>
