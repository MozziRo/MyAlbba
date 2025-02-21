<template>
    <div class="modal-overlay">
        <v-card class="modal-content modal-frame">
            <v-card-text class="pa-4 text-center">
                <v-progress-circular
                    indeterminate
                    color="primary"
                    size="50"
                    width="5"
                    class="loading-icon"
                ></v-progress-circular>
                <h3 class="modal-title">
                    가게의 최신 리뷰를 열심히 불러오고 있습니다!
                </h3>
                <div class="modal-text">
                    <p class="content">약 1분정도 소요됩니다</p>
                    <p class="content">기다리는 동안 다른 작업을 수행할 수 있습니다</p>
                </div>
                <button @click="closeModal"
                    class="modal-btn"
                >
                    확인</button>
            </v-card-text>
        </v-card>
    </div>
</template>

<script setup>
import { defineEmits } from 'vue'

const emit = defineEmits(['close'])

const closeModal = () => {
  emit('close');
}

</script>

<style>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(66, 71, 105, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-frame {
  width: 40%;
}

.modal-title {
  padding: 1rem;
}

.modal-text {
  padding: 1rem;
}

.loading-icon {
  margin: 1rem;
  animation: spin 5s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.modal-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #424769;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-btn:hover {
  background-color: #2D3250;
}

</style>