<template>
  <footer class="footer">
    <div class="footer-content">
      <div class="footer-section">
        <h3>Albba</h3>
        <p>근무 관리의 새로운 기준</p>
      </div>
      <div class="footer-section">
        <h4>고객 지원</h4>
        <ul>
          <li><router-link to="/info/support" style="color: #00B386;">이용 가이드</router-link></li>
          <li><router-link to="/info/support" style="color: #00B386;">자주 묻는 질문</router-link></li>
          <li><router-link to="/info/support" style="color: #00B386;">고객센터</router-link></li>
        </ul>
      </div>
      <div class="footer-section">
        <h4>회사 정보</h4>
        <ul>
          <li><router-link to="/info/company" style="color: #00B386;">회사 소개</router-link></li>
          <li><router-link to="/info/company" style="color: #00B386;">이용약관</router-link></li>
          <li><router-link to="/info/company" style="color: #00B386;">개인정보처리방침</router-link></li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2024 Albba. All rights reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background-color: #2D3250;
  color: white;
  padding: 1rem 0;
  position: relative;
  width: 100%;
  z-index: 100;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  padding: 0 1rem;
}

.footer-section h3 {
  margin: 0 0 1rem 0;
  font-size: 1.5rem;
}

.footer-section h4 {
  margin: 0 0 1rem 0;
  font-size: 1.2rem;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin-bottom: 0.5rem;
  cursor: pointer;
  opacity: 0.8;
  transition: opacity 0.3s;
}

.footer-section li:hover {
  opacity: 1;
}

.footer-section a {
  color: #676F9D !important;
  text-decoration: none;
}

.footer-section a:hover {
  color: #F9B17A !important;
}

.footer-bottom {
  text-align: center;
  margin-top: 1rem;
  padding-top: 0.5rem;
  border-top: 1px solid rgba(255,255,255,0.1);
}

.footer-bottom p {
  margin: 0;
  opacity: 0.8;
  font-size: 0.8rem;
}
</style>
