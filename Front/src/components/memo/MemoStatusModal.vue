<template>
  <div class="modal-overlay">
    <v-card class="modal-content">
      <v-toolbar color="white">
        <v-spacer></v-spacer>
        <v-btn icon @click="closeModal">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>

      <v-card-text class="pa-4">
        <h3 class="modal-message">
          {{ props.memo.creator }}님이 남기신 알림을 <br>
          완료로 변경하시겠습니까?
        </h3>
        <div class="modal-btns">
          <v-card-actions class="pa-4">
            <button class="modal-left-btn" @click="closeModal">
              취소
            </button>
            <button class="modal-right-btn" @click="emitUpdateStatus">
              변경
            </button>
          </v-card-actions>
        </div>
      </v-card-text>
    </v-card>
  </div>
</template>

<script setup>
const props = defineProps({
  memo: {
    type: Object,
    required: true,
  },
});

const emit = defineEmits(['close', 'update-status']);

const closeModal = () => {
  emit('close');
};

const emitUpdateStatus = () => {
  emit('update-status', props.memo);
  emit('close');
};
</script>

<style>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(45, 50, 80, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(66, 71, 105, 0.1);
  /* width: 300px; */
  /* max-width: 90%; */
  text-align: center;
  z-index: 1001;
}

.modal-message {
  padding: 2rem;
  padding-top: 0px;
}

.modal-btns {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 1rem;
}

.modal-left-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #9E9E9E;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.modal-left-btn:hover {
  background-color: #676F9D;
}

.modal-right-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #424769;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.modal-right-btn:hover {
  background-color: #2D3250;
}
</style>