<template>
  <div class="notice-bar" @click="goToNoticeList">
    <div class="notice-box">
      <v-icon
        color="#424769"
        size="24"
        class="me-3 bouncing-icon"
      >
        mdi-bullhorn
      </v-icon>
      
      <v-slide-x-transition group>
        <div v-if="noticeStore.notices.length > 0" class="notice-content">
            {{ currentNotice.title }}
        </div>
        <div v-else class="notice-content">
          <span class="text-subtitle-2 text-grey">
            등록된 공지사항이 없습니다.
          </span>
        </div>
      </v-slide-x-transition>
  
    </div>
  </div>
</template>

<script setup>
import { useNoticeStore } from '@/store/notice/noticeStore';
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';

const noticeStore = useNoticeStore();
const router = useRouter();
const currentIndex = ref(0);
const intervalId = ref(null);

const currentNotice = computed(() => {
  if (noticeStore.notices.length === 0) return null;
  return noticeStore.notices[currentIndex.value];
});

onMounted(async () => {
  await noticeStore.fetchNotices();

  // 3초마다 공지사항 순환
  intervalId.value = setInterval(() => {
    if (noticeStore.notices.length > 0) {
      currentIndex.value = (currentIndex.value + 1) % noticeStore.notices.length;
    }
  }, 3000);
});

onUnmounted(() => {
  if (intervalId.value) {
    clearInterval(intervalId.value);
  }
});

const goToNoticeList = () => {
  router.push({ name: 'NoticeDetail' });
};
</script>

<style scoped>
.notice-bar {
  padding: 0.8rem 10vw;
  background: #fcf4ed;
  cursor: pointer;
  overflow: hidden;
}

.notice-box {
  display: flex;
  padding-left: clamp(0.1rem, 1vw, 3rem);
}

.notice-content {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 80%;
  color: #202020;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

.bouncing-icon {
  color:#F9B17A;
  animation: bounce 2s infinite;
}

@media (max-width: 600px) {
  .notice-content {
    max-width: 60%;
  }
}
</style>
