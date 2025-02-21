<template>
    <div v-if="isOpen" class="modal-overlay">
      <div class="modal-content">
        <p v-html="message"></p>
        <div class="modal-actions">
          <button class="confirm-btn" @click="confirm">확인</button>
          <button class="cancel-btn" @click="close">취소</button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue'
  
  defineProps({
    isOpen: Boolean,
    message: String
  })
  
  const emit = defineEmits(['confirm', 'close'])
  
  const confirm = () => emit('confirm')
  const close = () => emit('close')
  </script>
  
  <style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(45, 50, 80, 0.5);  /* primary-dark 활용 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  width: 20%;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  box-shadow: 0px 4px 6px rgba(66, 71, 105, 0.1);  /* primary 컬러 활용 */
}

.modal-actions {
  margin-top: 30px;
  display: flex;
  justify-content: space-around;
}

.confirm-btn {
  background: #424769;
  color: white;
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 5px;
}

.cancel-btn {
  background: #9E9E9E;  /* neutral-dark 사용 */
  border: none;
  padding: 8px 16px;
  cursor: pointer;
  border-radius: 5px;
}

.confirm-btn:hover {
  background: #2D3250;
}

.cancel-btn:hover {
  background: #676F9D;  /* primary-light 사용 */
}
  </style>
  