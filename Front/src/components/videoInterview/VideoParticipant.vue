<template>
  <div
    class="video-wrapper"
    :class="{
      'local-video': isLocal,
      'enlarged': isEnlarged
    }"
    @click="$emit('toggle-enlarge')"
  >
    <video
      ref="videoRef"
      :autoplay="true"
      :muted="isLocal"
      :srcObject="stream"
    ></video>
    <div class="video-label">
      {{ label }}
      <span v-if="isLocal">(나)</span>
    </div>
    <div class="mic-status" v-if="isLocal">
      {{ isAudioOn ? '🎤' : '🔇' }}
    </div>
    <div class="video-controls">
      <button v-show="!isLocal" @click.stop="toggleEnlarge">
        {{ isEnlarged ? '🔄' : '⬆️' }}
      </button>
    </div>
  </div>
  <div v-if="isEnlarged" class="overlay" @click="$emit('toggle-enlarge')"></div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useLocalVideoStore } from '@/store/video/localVideoStore.js'

const props = defineProps({
  stream: {
    type: MediaStream,
    required: true
  },
  label: {
    type: String,
    required: true
  },
  isLocal: {
    type: Boolean,
    default: false
  },
  isEnlarged: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['toggle-enlarge'])
const videoRef = ref(null)

const localVideoStore = useLocalVideoStore()
const isAudioOn = computed(() => localVideoStore.isAudioOn)

onMounted(() => {
  if (videoRef.value && props.stream) {
    videoRef.value.srcObject = props.stream
  }
})

watch(() => props.stream, (newStream) => {
  if (videoRef.value && newStream) {
    videoRef.value.srcObject = newStream
  }
})

const toggleEnlarge = () => {
  emit('toggle-enlarge')
}
</script>

<style scoped>
.video-wrapper {
  position: relative;
  aspect-ratio: 16/9;
  background: #2D3250;  /* 진한 네이비 블루 */
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 16px rgba(0,0,0,0.3);
  transition: all 0.3s ease;
  cursor: pointer;
}

.video-wrapper.enlarged {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80vw;
  height: 80vh;
  z-index: 1001;
}

.video-wrapper video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-label {
  position: absolute;
  bottom: 1rem;
  left: 1rem;
  background: rgba(45, 50, 80, 0.6);  /* 진한 네이비 블루 투명 버전 */
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  color: white;
}

.video-controls {
  position: absolute;
  top: 1rem;
  right: 1rem;
  display: flex;
  gap: 0.5rem;
}

.video-controls button {
  background: rgba(45, 50, 80, 0.6);  /* 진한 네이비 블루 투명 버전 */
  border: none;
  color: white;
  padding: 0.5rem;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
}

.video-controls button:hover {
  background: rgba(66, 71, 105, 0.8);  /* 중간 톤 네이비 블루 투명 버전 */
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.7);
  z-index: 999;
}

.mic-status {
  position: absolute;
  bottom: 1rem;
  right: 1rem;
  background: rgba(45, 50, 80, 0.6);  /* 진한 네이비 블루 투명 버전 */
  padding: 0.5rem;
  border-radius: 50%;
  font-size: 0.9rem;
}
</style>
