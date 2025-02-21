<template>
  <div class="emoticon-picker">
    <span v-for="(emoticon, index) in emoticons" :key="index" @click="selectEmoticon(emoticon)">
      {{ emoticon }}
    </span>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue'

const emits = defineEmits(['select'])

const emoticons = ['😊', '😂', '😍', '👍', '🎉', '😢']

const selectEmoticon = (emoticon) => {
  emits('select', emoticon)
}
</script>

<style scoped>
.emoticon-picker {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 0.5rem;
  background-color: #F5F5F5;  /* 밝은 중립 색상 */
}

.emoticon-picker span {
  font-size: 1.5rem;
  cursor: pointer;
  transition: transform 0.2s;
  padding: 0.25rem;
  border-radius: 8px;
}

.emoticon-picker span:hover {
  transform: scale(1.3);
  color: #2D3250;  /* 진한 네이비 블루 */
}
</style>
