<template>
  <div class="company-info">
    <section class="intro">
      <h2>회사 정보</h2>
      <p>
        Albba는 혁신적인 근무 관리 솔루션을 제공하여
        고객님의 업무 효율을 극대화하는 데 기여하고 있습니다.
        당사의 비전과 미션, 그리고 경영 철학에 대해 아래에서 확인해 주세요.
      </p>
    </section>
    <section class="company-details">
      <h3>회사 소개</h3>
      <p>
        Albba는 최신 IT 기술을 도입한 근무 관리 시스템으로, 인력 배치와 일정 관리의 혁신을 추구합니다.
        고객의 성공적인 비즈니스 운영을 돕기 위해 끊임없이 연구 및 개선하고 있습니다.
      </p>

      <h3>이용약관</h3>
      <p>
        서비스를 이용하시기 전에 꼭 이용약관을 확인해 주세요.
        이용약관은 고객님의 권리와 의무를 명확하게 규정하고 있으며,
        모든 이용자는 이를 숙지한 후 서비스를 이용하셔야 합니다.
      </p>

      <h3>개인정보처리방침</h3>
      <p>
        당사는 고객님의 개인정보 보호를 최우선으로 생각하며,
        관련 법령에 따라 안전하게 관리하고 있습니다.
        개인정보 수집, 이용, 보관에 관한 상세한 내용은 개인정보처리방침을 참고해 주세요.
      </p>
    </section>
  </div>
</template>

<script setup>
</script>

<style scoped>
.company-info {
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

.company-details {
  margin-top: 2rem;
}

.company-details h3 {
  font-size: 1.5rem;
  color: var(--primary);
  margin-top: 1.5rem;
}

.company-details p {
  font-size: 1rem;
  color: #444;
  line-height: 1.6;
}
</style>
