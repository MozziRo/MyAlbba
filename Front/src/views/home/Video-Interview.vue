<template>
  <div class="video-interview-container">
    <!-- <h1 class="title">화상 면접 기능</h1>
    <p class="subtitle">면접 중 메모를 기록하고 AI 분석을 통해 지원자의 적합성을 확인하세요.</p> -->

    <div class="video-preview">
      <img src="../../assets/images/videoInterview.png" alt="화상 면접 화면" class="video-image" />
    </div>

    <div class="memo-section">
      <textarea v-model="memo" placeholder="면접 메모를 입력하세요..." class="memo-input"></textarea>
    </div>

    <button class="end-button" @click="analyzeMemo">면접 종료 및 분석</button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const memo = ref('');

const analyzeMemo = () => {
  alert("AI 분석이 시작됩니다. 결과를 기다려 주세요.");
};
</script>

<style scoped>
.video-interview-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  background: #676975;
  color: #fff;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 800px;
  max-height: 600px;
  margin: 0 auto;
}

.title {
  font-size: 1.8rem;
  font-weight: bold;
}

.subtitle {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.video-preview {
  width: 100%;
  max-width: 720px;
  height: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  overflow: hidden;
}

.video-image {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 8px;
}

.memo-section {
  width: 100%;
  border-color: #fff;
  height: fit-content;
}

.memo-input {
  width: 100%;
  height: 100px;
  padding: 0.5rem;
  border-radius: 8px;
  background-color: #fff;
  border: none;
  resize: none;
}

.end-button {
  margin-top: 1rem;
  padding: 0.8rem 1.5rem;
  font-size: 1.2rem;
  background-color: #F9B17A;
  color: #2D3250;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.end-button:hover {
  background-color: #FCDDBD;
  transform: scale(1.05);
}
</style>
