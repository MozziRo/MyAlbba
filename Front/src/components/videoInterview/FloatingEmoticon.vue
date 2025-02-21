<template>
  <div 
    class="floating-emoticon"
    :style="style"
    @animationend="$emit('remove')"
  >
    {{ emoticon }}
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  emoticon: String,
  startPosition: {
    type: Object,
    default: () => ({ x: 0, y: 0 })
  }
})

const style = computed(() => ({
  left: `${props.startPosition.x}px`,
  top: `${props.startPosition.y}px`,
  '--random-x': `${Math.random() * 200 - 100}px`,
  '--random-y': `${-100 - Math.random() * 100}px`,
  '--random-rotate': `${Math.random() * 360}deg`
}))
</script>

<style scoped>
.floating-emoticon {
  position: fixed;
  font-size: 2rem;
  pointer-events: none;
  z-index: 1000;
  color: #2D3250;  /* 진한 네이비 블루 */
  animation: float 2s ease-out forwards;
}

@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
    opacity: 1;
    color: #424769;  /* 중간 톤 네이비 블루 */
  }
  100% {
    transform: translate(var(--random-x), var(--random-y)) rotate(var(--random-rotate));
    opacity: 0;
    color: #676F9D;  /* 밝은 네이비 블루 */
  }
}
</style> 