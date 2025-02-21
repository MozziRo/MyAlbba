<template>
  <section class="section employee-section">
    <h4 class="section-title">
      <i class="icon-user"></i> 직원 선택
    </h4>
    <div v-if="workerStore.loading" class="loading">
      불러오는 중...
    </div>
    <div v-else-if="workerStore.error" class="error">
      {{ workerStore.error }}
    </div>
    <div v-else class="employee-grid">
      <button
        v-for="worker in workerStore.workers"
        :key="worker.workerId"
        class="employee-button"
        :class="{ 'selected': isSelected(worker) }"
        @click="toggleEmployee(worker)"
      >
        <div class="employee-avatar">
          {{ worker.name[0] }}
        </div>
        <span class="employee-name">{{ worker.name }}</span>
      </button>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useWorkerStore } from '@/store/worker/workerStore';

const workerStore = useWorkerStore();

const props = defineProps({
  selectedEmployees: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['update:selectedEmployees']);

const selectedEmployees = ref(props.selectedEmployees);

const filteredWorkers = computed(() => {
  return workerStore.workers.filter(worker => 
    worker.id !== 'all' && worker.workerId // workerId가 있는 실제 직원만 필터링
  );
});

const isSelected = (worker) => {
  return selectedEmployees.value.some(emp => emp.workerId === worker.workerId);
};

const toggleEmployee = (worker) => {
  const newSelection = [...selectedEmployees.value];
  const index = newSelection.findIndex(emp => emp.workerId === worker.workerId);
  if (index > -1) {
    newSelection.splice(index, 1);
  } else {
    newSelection.push(worker);
  }
  selectedEmployees.value = newSelection;
  emit('update:selectedEmployees', newSelection);
};

onMounted(async () => {
  await workerStore.fetchAllWorkers();
});
</script>

<style scoped>
.employee-section {
  background-color: var(--color-white);
  border-radius: var(--border-radius);
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--color-primary);
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
}

.employee-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
  max-height: 400px;
  overflow-y: auto;
  padding: 4px;
}

.employee-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border: 2px solid transparent;
  border-radius: var(--border-radius);
  background-color: var(--color-white);
  cursor: pointer;
  transition: all 0.3s ease;
}

.employee-button:hover {
  background-color: var(--color-primary-light);
  border-color: var(--color-primary);
  transform: translateY(-2px);
}

.employee-button.selected {
  background-color: #e8f5e9;
  border: 3px solid var(--color-primary);
  color: var(--color-primary);
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(7, 112, 42, 0.2);
}

.employee-button.selected .employee-avatar {
  background-color: var(--color-primary);
  transform: scale(1.15);
  box-shadow: 0 3px 6px rgba(7, 112, 42, 0.3);
}

.employee-button.selected .employee-name {
  font-weight: 700;
  font-size: 15px;
  color: var(--color-primary);
}

.employee-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--color-primary);
  color: var(--color-white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: 600;
}

.employee-name {
  font-size: 14px;
  text-align: center;
  word-break: keep-all;
}

.loading {
  text-align: center;
  padding: 20px;
  color: var(--color-primary);
}

.error {
  text-align: center;
  padding: 20px;
  color: var(--color-error);
}

@media (max-width: 900px) {
  .employee-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }

  .employee-button {
    padding: 8px;
  }

  .employee-avatar {
    width: 32px;
    height: 32px;
    font-size: 14px;
  }

  .employee-name {
    font-size: 12px;
  }
}
</style>