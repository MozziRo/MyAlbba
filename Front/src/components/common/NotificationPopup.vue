<template>
  <div
    v-show="isVisible"
    class="notification-popup"
    :style="positionStyle"
    :class="{ 'fade-out': isFading }"
  >
    {{ message }}
  </div>
</template>

<script setup>
import { ref, watch, computed, onBeforeUnmount } from 'vue'

const props = defineProps({
  message: {
    type: String,
    required: true
  },
  show: {
    type: Boolean,
    default: false
  },
  position: {
    type: Object,
    default: () => ({
      x: 0,
      y: 0
    })
  },
  duration: {
    type: Number,
    default: 500
  }
})

const emit = defineEmits(['update:show'])

const isVisible = ref(false)
const isFading = ref(false)
let timeoutId = null

const positionStyle = computed(() => ({
  position: 'fixed',
  top: `${props.position.y}px`,
  left: `${props.position.x}px`,
  transform: 'translate(-50%, -100%)'
}))

watch(() => props.show, (newValue) => {
  if (newValue) {
    if (timeoutId) {
      clearTimeout(timeoutId)
    }
    isFading.value = false
    isVisible.value = true
    showNotification()
  }
})

const showNotification = () => {
  timeoutId = setTimeout(() => {
    isFading.value = true
    timeoutId = setTimeout(() => {
      isVisible.value = false
      isFading.value = false
      timeoutId = null
      emit('update:show', false)
    }, 300)
  }, props.duration)
}

onBeforeUnmount(() => {
  if (timeoutId) {
    clearTimeout(timeoutId)
  }
})
</script>

<style scoped>
.notification-popup {
  background-color: rgba(66, 71, 105, 0.95);
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 0.875rem;
  z-index: 9999;
  pointer-events: none;
  transition: opacity 0.2s ease;
  box-shadow: 0 4px 12px rgba(66, 71, 105, 0.2);
}

.fade-out {
  opacity: 0;
}
</style>
