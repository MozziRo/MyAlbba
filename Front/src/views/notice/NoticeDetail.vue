<style scoped>
.notice-header {
  padding: 1rem 10vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 200px;
  background-color: #fcf4ed;
}

.notice {
  padding: 1rem 10vw;
}

.title {
  color: #333D4B
}

.notice-content {
  display: flex;
  flex-direction: column;
  align-items: left;
  padding: 0 4rem;
  padding-bottom: 3rem;
}

.d-flex.justify-space-between.align-center.pa-4 {
  padding: 1rem 0 !important;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
}

.btn-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 2rem 4rem 0;
  min-height: 80px; /* 고정 높이 추가 */
}

.pagination {
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: #333;
  border-radius: 10px;
  border: 1px solid #ddd;
  cursor: pointer;
}

.pagination button:hover {
  background-color: var(--primary);
  color: white;
}

.pagination button:disabled {
  background-color: #f9f9f9;
  color: #ccc;
  border-color: #ddd;
  cursor: not-allowed;
}

.pagination .prev-next-btn:hover {
  background-color: #ffffff;
  border-color: #2D3250;
  color: #2D3250;
}

.pagination .active {
  border: 1px solid #424769;
  color: #333;
}

.notice-add-btn {
  margin-left: auto;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.notice-add-btn:hover {
  background-color: var(--primary-dark);
}

.notice-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-title {
  font-size: larger;
  font-weight: bold;
  color: #4E5968;
}

.notice-date {
  padding-top: 1rem;
  color: #9A9F9C;
}

.notice-item {
  transition: all 0.2s ease;
  padding: 1rem 0;
  border-bottom: 1px solid #C0C6C2;
  margin-bottom: 10px;
}

.no-item {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: larger;
  font-weight: bolder;
}

.notice-item:hover {
  background-color: #f5f5f5;
  transform: scale(1.01);
}

@media (max-width: 768px) {
  .notice-content {
    padding: 0 1rem;
  }

  .d-flex.justify-space-between.align-center.pa-4 {
    flex-direction: column;
    align-items: flex-start;
  }

  .btn-layout {
    padding: 2rem 1rem 0;
    flex-direction: column;
  }

  .notice-add-btn {
    margin-top: 1rem;
    margin-left: 0;
    width: 100%;
  }
}
</style><template>
  <div class="notice-header">
    <h3>사장님의 공지사항을 확인해주세요!</h3>
    <span>사장님이 중요한 소식을 전해주셨어요! 지금 바로 확인해보세요, 놓치면 안될지도 몰라요!</span>
  </div>
  <div class="notice">
    <div class="notice-content">
      <div class="d-flex justify-space-between align-center pa-4">
        <div class="d-flex align-center">
          <v-icon color="#333D4B" size="32" class="me-3">mdi-bullhorn</v-icon>
          <h1 class="title">공지사항</h1>
        </div>
        <button 
          v-if="isOwner" 
          class="notice-add-btn"
          @click="openCreateModal"
        >
          글 작성
        </button>
      </div>

      <ul class="notice-list">
        <!-- 공지사항이 있는 경우 -->
        <li class="notice-item"
          v-if="noticeStore.notices.length > 0"
          v-for="notice in paginatedNotices"
          :key="notice.id"
          @click="openNoticeModal(notice)"
        >
          <div class="notice-title">{{ notice.title }}</div>
          <div class="notice-date">{{ formatDate(notice.createdAt) }}</div>
        </li>
        <!-- 공지사항이 없는 경우 -->
        <div v-else>
          <li class="no-item">
            <v-icon size="48" color="#B0BEC5">mdi-information-outline</v-icon>
            <p>현재 등록된 공지사항이 없습니다!</p>
            <p>새로운 공지를 기다려주세요</p>
          </li>
        </div>
      </ul>

      <div class="btn-layout">
        <!-- 페이지 버튼 -->
        <div v-if="noticeStore.notices.length > 0" class="pagination">
          <button
            class="pagination-btn prev-next-btn"
            @click="goToPage(currentPage - 1)"
            :disabled="currentPage === 1"
          >
            이전
          </button>
          <button
            v-for="page in totalPages"
            :key="page"
            class="page-btn"
            :class="{ active: page === currentPage }"
            @click="goToPage(page)"
          >
            {{ page }}
          </button>
          <button
            class="pagination-btn prev-next-btn"
            @click="goToPage(currentPage + 1)"
            :disabled="currentPage === totalPages"
          >
            다음
          </button>
        </div>
      </div>
    </div>

    <!-- 모달 컴포넌트들 -->
    <NoticeDetailModal
      :notice="selectedNotice"
      v-model="selectedNotice"
      :is-owner="isOwner"
      @update="handleUpdate"
      @delete="handleDelete"
    />

    <CreateNoticeModal
      v-model="showCreateModal"
      @submit="handleCreate"
      @close="closeModal"
    />
  </div>
</template>

<script setup>
import { useNoticeStore } from '@/store/notice/noticeStore';
import { ref, onMounted, computed } from 'vue';
import { storeToRefs } from 'pinia';
import { useMemberStore } from '@/store/member/memberStore';
import NoticeDetailModal from '@/components/notice/NoticeDetailModal.vue';
import CreateNoticeModal from '@/components/notice/CreateNoticeModal.vue';

const noticeStore = useNoticeStore();
const memberStore = useMemberStore();
const showCreateModal = ref(false);
const selectedNotice = ref(null);

const { isOwner } = storeToRefs(memberStore);

onMounted(() => {
  noticeStore.fetchNotices();
});

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const openNoticeModal = (notice) => {
  selectedNotice.value = notice;
};

const openCreateModal = () => {
  showCreateModal.value = true;
};

const closeModal = () => {
  showCreateModal.value = false;
  selectedNotice.value = null;
};

const handleCreate = async (noticeData) => {
  await noticeStore.addNotice(noticeData);
  closeModal();
};

const handleUpdate = async (noticeData) => {
  await noticeStore.editNotice(noticeData);
  closeModal();
};

const handleDelete = async (id) => {
  await noticeStore.removeNotice(id);
  closeModal();
};

// 페이지 이동
const currentPage = ref(1);
const itemsPerPage = 5;

const paginatedNotices = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return noticeStore.notices.slice(start, end);
});

const totalPages = computed(() => {
  return Math.ceil(noticeStore.notices.length / itemsPerPage);
});

const goToPage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
  }
};
</script>

<style scoped>
.notice-header {
  padding: 1rem 10vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 200px;
  background-color: #fcf4ed;
}

.notice {
  padding: 1rem 10vw;
}

.title {
  color: #333D4B
}

.notice-content {
  display: flex;
  flex-direction: column;
  align-items: left;
  padding: 0 4rem;
  padding-bottom: 3rem;
}

.d-flex.justify-space-between.align-center.pa-4 {
  padding: 1rem 0 !important;
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
}

.btn-layout {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0rem 4rem;
  position: relative;
}

.pagination {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 8px;
}

.pagination button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  color: #333;
  border-radius: 10px;
  border: 1px solid #ddd;
  cursor: pointer;
}

.pagination button:hover {
  background-color: var(--primary);
  color: white;
}

.pagination button:disabled {
  background-color: #f9f9f9;
  color: #ccc;
  border-color: #ddd;
  cursor: not-allowed;
}

.pagination .prev-next-btn:hover {
  background-color: #ffffff;
  border-color: #2D3250;
  color: #2D3250;
}

.pagination .active {
  border: 1px solid #424769;
  color: #333;
}

.notice-add-btn {
  margin-left: auto;
  padding: 0.5rem 1rem;
  font-size: 1rem;
  min-width: 100px;
  background-color: var(--primary);
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
}

.notice-add-btn:hover {
  background-color: var(--primary-dark);
}

.notice-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.notice-title {
  font-size: larger;
  font-weight: bold;
  color: #4E5968;
}

.notice-date {
  padding-top: 1rem;
  color: #9A9F9C;
}

.notice-item {
  transition: all 0.2s ease;
  padding: 1rem 0;
  border-bottom: 1px solid #C0C6C2;
  margin-bottom: 10px;
}

.no-item {
  padding: 1rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-size: larger;
  font-weight: bolder;
}

.notice-item:hover {
  background-color: #f5f5f5;
  transform: scale(1.01);
}

@media (max-width: 768px) {
  .notice-content {
    padding: 0 1rem;
  }

  .d-flex.justify-space-between.align-center.pa-4 {
    flex-direction: column;
    align-items: flex-start;
  }

  .btn-layout {
    padding: 0 1rem;
  }

  .notice-add-btn {
    margin-top: 1rem;
    margin-left: 0;
    width: 100%;
  }

  .pagination {
    position: static;
    transform: none;
    margin-bottom: 8px;
    justify-content: center;
  }
}
</style>