<template>
  <div class="participants-list" :class="{ 'participants-list-open': isOpen }">
    <div class="participants-toggle" @click="toggleList">
      <span>👨🏻‍👩🏻‍👧🏻‍👦🏻</span>
    </div>
    <div class="participants-content">
      <h3>참가자 목록</h3>
      <div class="participants">
        <div v-for="participant in participants" :key="participant.id" class="participant">
          <span class="status" :class="{ speaking: participant.isSpeaking }"></span>
          <span class="name">{{ participant.name }}</span>
          <div class="media-status">
            <span :class="{ 'media-off': !participant.isVideoOn }">🎥</span>
            <span :class="{ 'media-off': !participant.isAudioOn }">🎤</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  participants: {
    type: Array,
    required: true
  }
})

const isOpen = ref(false)

const toggleList = () => {
  isOpen.value = !isOpen.value
}
</script>

<style scoped>
.participants-list {
  position: fixed;
  top: 70px;
  right: -320px;
  width: 320px;
  height: calc(100vh - 70px);
  background-color: #2D3250;  /* 진한 네이비 블루 */
  box-shadow: -2px 0 8px rgba(0,0,0,0.2);
  transition: right 0.3s ease;
  display: flex;
  z-index: 100;
}

.participants-list-open {
  right: 0;
}

.participants-toggle {
  position: absolute;
  left: -40px;
  top: 30%;
  width: 40px;
  height: 40px;
  background-color: #424769;  /* 중간 톤 네이비 블루 */
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border-radius: 8px 0 0 8px;
}

.participants-content {
  flex: 1;
  padding: 1rem;
  color: #fff;
}

.participant {
  display: flex;
  align-items: center;
  padding: 0.8rem;
  margin-bottom: 0.5rem;
  background-color: #424769;  /* 중간 톤 네이비 블루 */
  border-radius: 8px;
  transition: all 0.3s ease;
}

.status {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #676F9D;  /* 밝은 네이비 블루 */
  margin-right: 0.8rem;
}

.status.speaking {
  background-color: #F9B17A;  /* 따뜻한 오렌지 */
  box-shadow: 0 0 8px #F9B17A;
}

.name {
  flex: 1;
  color: #fff;
}

.media-status {
  display: flex;
  gap: 0.5rem;
}

.media-status span {
  opacity: 0.8;
}

.media-status .media-off {
  opacity: 0.3;
}
</style>
