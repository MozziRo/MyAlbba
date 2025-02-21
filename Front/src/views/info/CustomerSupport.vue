<template>
  <div class="customer-support">
    <section class="intro">
      <h2>고객 지원</h2>
      <p>
        Albba는 고객님의 편리한 근무 관리를 위해 항상 최상의 지원을 제공합니다.
        문제 해결 및 원활한 서비스 이용을 위해 아래의 고객 지원 정보를 참고해 주시기 바랍니다.
      </p>
    </section>
    <section class="support-details">
      <h3>이용 가이드</h3>
      <p>
        서비스 이용 방법에 대한 단계별 가이드를 제공합니다. 화면 예시와 함께 자세한 설명을 통해 초보자도 쉽게
        서비스를 활용하실 수 있습니다.
      </p>

      <h3>자주 묻는 질문 (FAQ)</h3>
      <ul>
        <li>
          <strong>Q:</strong> Albba의 주요 기능은 무엇인가요?<br />
          <strong>A:</strong> 근무 관리, 인수인계, 실시간 알림, 화상면접 등 다양한 기능을 제공합니다.
        </li>
        <li>
          <strong>Q:</strong> 고객 지원은 어떻게 받나요?<br />
          <strong>A:</strong> 웹사이트 내 고객센터와 FAQ에서 빠른 답변을 확인하실 수 있습니다.
        </li>
        <li>
          <strong>Q:</strong> 서비스 이용 중 문제가 발생하면 어떻게 해야 하나요?<br />
          <strong>A:</strong> 24시간 운영되는 고객센터(전화 및 이메일)를 통해 문의해 주시면 신속하게 도와드립니다.
        </li>
      </ul>

      <h3>고객센터 연락처</h3>
      <p>
        전화: 02-1234-5678<br />
        이메일: support@albba.co.kr<br />
        운영 시간: 평일 09:00 ~ 18:00 (토,일,공휴일 제외)
      </p>
    </section>
  </div>
</template>

<script setup>
</script>

<style scoped>
.customer-support {
  background-color: #ffffff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  font-family: 'Pretendard', sans-serif;
}

.intro h2 {
  font-size: 2rem;
  color: #333;
  margin-bottom: 1rem;
}

.intro p {
  font-size: 1rem;
  color: #555;
  line-height: 1.5;
}

.support-details {
  margin-top: 2rem;
}

.support-details h3 {
  font-size: 1.5rem;
  color: var(--primary);
  margin-top: 1.5rem;
}

.support-details p,
.support-details li {
  font-size: 1rem;
  color: #444;
  line-height: 1.6;
}

.support-details ul {
  list-style-type: disc;
  padding-left: 1.5rem;
  margin-top: 1rem;
}
</style>
