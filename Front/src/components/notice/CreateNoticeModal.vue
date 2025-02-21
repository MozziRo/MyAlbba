<template>
  <v-dialog v-model="dialog" persistent max-width="600px">
    <v-card>
      <v-toolbar color="white">
        <v-spacer></v-spacer>
        <v-btn icon @click="close">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>

      <v-card-text class="pa-4">
        <form class="new-notice-layout" @submit.prevent="handleSubmit">
          <input class="new-notice-title"
            v-model="form.title"
            :class="{ 'is-invalid': !titleValid }"
            placeholder="제목을 입력해주세요"
          />

          <textarea class="new-notice-content"
            v-model="form.content"
            :class="{ 'is-invalid': !contentValid }"
            rows="8"
            placeholder="내용을 입력해주세요"
          ></textarea>

          <v-card-actions class="pa-4">
            <button class="notice-register-btn" type="submit">
              등록
            </button>
          </v-card-actions>
        </form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue';

const emit = defineEmits(['close', 'submit']);

const dialog = ref(false);
const form = reactive({ title: '', content: '' });  

const close = () => {
  form.title = '';
  form.content = '';
  dialog.value = false;
  emit('close');
};

// 제목과 내용 유효성 검사
const titleValid = computed(() => form.title.trim().length > 0);
const contentValid = computed(() => form.content.trim().length > 0);

const handleSubmit = () => {
  if (!form.title || !form.content) {
    return;
  }
  emit('submit', { ...form });
  close();
};

const open = () => {
  dialog.value = true;
};
</script>

<style scoped>
.notice-btns {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.new-notice-layout {
  display: flex;
  flex-direction: column;
  padding: 2rem;
  padding-top: 0rem;
}

.new-notice-title {
  margin-bottom: 2rem;
  font-weight: bold;
  color: #4E5968;
  padding: 0.75rem;
  border: 1px solid #888888;
  border-radius: 10px;
}

.new-notice-title:focus {
  border: 1.2px solid;
  border-color: #424769;
  outline: none;
}

.new-notice-content {
  height: 200px;
  color: #4E5968;
  padding: 0.75rem;
  border: 1px solid #888888;
  border-radius: 10px;
}

.new-notice-content:focus {
  border: 1.2px solid;
  border-color: #424769;
  outline: none;
}

.new-notice-title.is-invalid,
.new-notice-content.is-invalid {
  border: none;
  background-color: #EAEDEB;
  font-weight: bold;
}

.notice-register-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #424769;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.notice-register-btn:hover {
  background-color: #2D3250;
}
</style>
