<template>
  <v-dialog v-model="dialogVisible" max-width="600" @click:outside="close">
    <v-card>
      <v-toolbar color="white">
        <v-spacer></v-spacer>
        <v-btn icon @click="close">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-toolbar>
      <v-card-text class="pa-4">
        <template v-if="!isEditing">
          <div class="notice-header">
            <h3 class="notice-title">{{ props.notice?.title }}</h3>
            <div class="notice-date">
              {{ formatDate(props.notice?.createdAt) }}
            </div>
          </div>

          <div class="notice-content">
            {{ props.notice?.content }}
          </div>
        </template>

        <template v-else>
          <form class="notice-modify-layout" @submit.prevent="handleSave">
            <input
              class="notice-modify-title"
              v-model="editForm.title"
              :class="{ 'is-invalid': !titleValid }"
              placeholder="제목을 입력해주세요"
            />

            <textarea
              class="notice-modify-content"
              v-model="editForm.content"
              :class="{ 'is-invalid': !contentValid }"
              rows="8"
              placeholder="내용을 입력해주세요"
            ></textarea>
          </form>
        </template>
      </v-card-text>

      <div class="notice-btns">
        <v-card-actions class="pa-4">
          <template v-if="!isEditing && isOwner">
            <button class="notice-left-btn" @click="startEditing">
              수정
            </button>
            <button class="notice-right-btn" @click="confirmDelete">
              삭제
            </button>
          </template>

          <template v-if="isEditing">
            <button class="notice-left-btn" @click="cancelEdit">
              취소
            </button>
            <button class="notice-right-btn" @click="handleSave">
              저장
            </button>
          </template>
        </v-card-actions>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  notice: {
    type: Object,
    default: null,
  },
  isOwner: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['update:modelValue', 'update', 'delete']);

const isEditing = ref(false);
const editForm = ref({ title: '', content: '' }); // Default structure to prevent null

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};

const close = () => {
  isEditing.value = false;
  emit('update:modelValue', null);
};

const startEditing = () => {
  if (props.notice) {
    editForm.value = { ...props.notice }; // Make sure notice is available
  }
  isEditing.value = true;
};

const cancelEdit = () => {
  isEditing.value = false;
  editForm.value = { title: '', content: '' }; // Reset form on cancel
};

// 제목과 내용 유효성 검사
const titleValid = computed(() => editForm.value.title.trim().length > 0);
const contentValid = computed(() => editForm.value.content.trim().length > 0);

const handleSave = () => {
  if (!editForm.value.title || !editForm.value.content) {
    return;
  }
  emit('update', editForm.value);
  isEditing.value = false;
  close();
};

const confirmDelete = () => {
  if (confirm('정말 삭제하시겠습니까?')) {
    emit('delete', props.notice.id);
    close();
  }
};
</script>

<style scoped>
.modal-header {
  padding-left: 2rem;
  font-weight: bold;
}

.notice-header {
  padding: 2rem;
  padding-top: 0px;
  border-bottom: 1px solid #C0C6C2;
}

.notice-title {
  font-weight: bold;
  color: #4E5968;
}

.notice-date {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  color: #666;
}

.notice-content {
  padding: 2rem;
  height: 200px;
  color: #4E5968;
}

.notice-modify-layout {
  display: flex;
  flex-direction: column;
  padding: 2rem;
  padding-top: 0rem;
}

.notice-modify-title {
  margin-bottom: 2rem;
  font-weight: bold;
  color: #4E5968;
  padding: 0.75rem;
  border: 1px solid #888888;
  border-radius: 10px;
}

.notice-modify-title:focus {
  border: 1.2px solid;
  border-color: #424769;
  outline: none;
}

.notice-modify-content {
  height: 200px;
  color: #4E5968;
  padding: 0.75rem;
  border: 1px solid #888888;
  border-radius: 10px;
}

.notice-modify-content:focus {
  border: 1.2px solid;
  border-color: #424769;
  outline: none;
}

.notice-modify-title.is-invalid,
.notice-modify-content.is-invalid {
  border: none;
  background-color: #EAEDEB;
  font-weight: bold;
}

.notice-btns {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.notice-left-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #C0C6C2;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.notice-left-btn:hover {
  background-color: #767A77;
}

.notice-right-btn {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: #424769;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.notice-right-btn:hover {
  background-color: #2D3250;
}
</style>
