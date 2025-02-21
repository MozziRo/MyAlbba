<template>
  <div
    class="type-card"
    @click="$emit('select')"
    :class="{ selected: isSelected }"
  >
    <div class="card-icon">{{ cardData.icon }}</div>
    <h3>{{ cardData.title }}</h3>
    <p>{{ cardData.description }}</p>
  </div>
</template>

<script setup>

// 이 카드는 타입, 아이콘, 제목, 설명을 포함한 객체를 받아서 그려주는 카드입니다.
// 알겠습니까?
defineProps({
  cardData: {
    type: Object,
    required: true,
    validator: (value) => {
      return ['type', 'icon', 'title', 'description'].every(key => key in value)
    }
  },
  isSelected: {
    type: Boolean,
    default: false
  }
})

// 선택 시 select emit을 부모에게 전달한다.
defineEmits(['select'])
</script>

<style scoped>
.type-card {
  width: 240px;
  padding: 30px;
  border-radius: 12px;
  border: 2px solid #ddd;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: white;
  font-family: 'Pretendard';
}

.type-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(66, 71, 105, 0.1);
}

.type-card.selected {
  border-color: #424769;
  background-color: rgba(66, 71, 105, 0.05);
}

.card-icon {
  font-size: 48px;
  margin-bottom: 20px;
}

.type-card h3 {
  margin: 0;
  margin-bottom: 10px;
  color: #333;
}

.type-card p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

@media (max-width: 768px) {
  .type-card {
    width: 100%;
    max-width: 280px;
  }
}
</style>
