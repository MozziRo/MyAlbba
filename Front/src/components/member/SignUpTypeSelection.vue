<!-- 이 SFC는 공통 컴포넌트인 TypeSelecionCard를 이용하여
     회원 가입 유형을 나누는 컴포넌트 입니다. -->

<template>
  <div class="type-selection">
    <div class="cards-container">

      <!-- 렌더링할 카드들 -->
      <TypeSelectionCard
        v-for="card in typeCards"
        :key="card.type"
        :card-data="card"
        :is-selected="modelValue === card.type"
        @select="selectType(card.type)"
      />
    </div>

    <button
      class="next-button"
      :disabled="!modelValue"
      @click="$emit('next')"
    >
      다음 단계
    </button>
  </div>
</template>





<!-- 스크립트 -->
<script setup>
import { ref } from 'vue'
import TypeSelectionCard from '@/components/common/TypeSelectionCard.vue'

defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue', 'next'])

// 회원 유형 카드 데이터
const typeCards = ref([
  {
    type: 'employee',
    icon: '👤',
    title: '알바생',
    description: '편리한 인수인계, 더 나은 알바 생활을 시작하세요'
  },
  {
    type: 'employer',
    icon: '💼',
    title: '사장님',
    description: '알바생 관리, 내 알빠다에서 한 번에 해결하세요'
  }
])

const selectType = (type) => {
  emit('update:modelValue', type)
}
</script>

<!-- 스타일 -->
<style scoped>
.type-selection {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px;
}

.cards-container {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.next-button {
  width: 200px;
  padding: 15px;
  border: none;
  border-radius: 8px;
  background-color: #424769;  /* 이전: #00B386 */
  color: white;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-family: 'Pretendard';
}

.next-button:disabled {
  background-color: rgba(66, 71, 105, 0.5);  /* 이전: #80D9BF */
  cursor: not-allowed;
}

.next-button:not(:disabled):hover {
  background-color: #2D3250;  /* 이전: #007F5E */
}

@media (max-width: 768px) {
  .cards-container {
    flex-direction: column;
    align-items: center;
  }
}
</style>
