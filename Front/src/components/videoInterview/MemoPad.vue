<template>
  <div
    class="memo-pad"
    v-show="isVisible"
    ref="memoPad"
    @dblclick="pinMemo"
    :style="memoPadStyles"
    @mousedown="startDrag"
  >
    <div class="memo-header" ref="dragHandle">
      <div class="header-left">
        <h2 style="color: var(--primary);">메모장</h2>
      </div>
      <div class="header-right">
        <button style="color: var(--primary);" @click.stop="emit('close')">✕</button>
      </div>
    </div>
    <div class="memo-content">
      <div class="input-section">
        <div class="input-group">
          <label>이름</label>
          <input
            v-model="applicantName"
            placeholder="알바생 이름을 입력하세요"
            class="name-input"
          />
        </div>
        <div class="input-group">
          <label>내용</label>
          <textarea
            v-model="memoText"
            placeholder="면접 메모를 입력하세요..."
            :style="{
              height: textareaHeight + 'px'
            }"
          ></textarea>
        </div>
        <div class="button-row">
          <button @click="saveMemo">저장하기</button>
          <button @click="clearMemo">새로 작성</button>
        </div>
      </div>
    </div>
    <p v-if="saveMessage" class="message">{{ saveMessage }}</p>
    <!-- 리사이즈 핸들 -->
    <div class="resize-handle" @mousedown.stop="startResize">
      <span role="img" aria-label="resize" style="font-size: 16px;"></span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useInterviewStore } from '@/store/video/interviewStore'

const props = defineProps({
  isVisible: {
    type: Boolean,
    default: false
  },
  autoSave: {  // 저장 트리거를 위한 prop 추가
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(["close"])

const memoPad = ref<HTMLElement | null>(null)
const dragHandle = ref<HTMLElement | null>(null)
const position = ref({ x: 0, y: 0 })
const isDragging = ref(false)
const dragOffset = ref({ x: 0, y: 0 })

// 크기 조절을 위한 반응형 변수
const padWidth = ref(330)
const padHeight = ref(560)

// 리사이즈 관련 변수
const isResizing = ref(false)
const initialMouse = ref({ x: 0, y: 0 })
const initialDimensions = ref({ width: 0, height: 0 })

const interviewStore = useInterviewStore()
const applicantName = ref('')
const memoText = ref('')
const saveMessage = ref('')

// 단일 로컬스토리지 키로 (이름, 내용)을 저장
const STORAGE_KEY_ALL_MEMOS = 'all_memos'
const STORAGE_KEY_CURRENT = 'current_memo'

// textarea 높이를 위한 반응형 변수 추가
const textareaHeight = ref(200)

// 메모장이 고정됐는지 여부
const isPinned = ref(false)

// isPinned 상태일 경우 opacity: 1로 고정
const memoPadStyles = computed(() => ({
  left: position.value.x + 'px',
  top: position.value.y + 'px',
  width: padWidth.value + 'px',
  height: padHeight.value + 'px',
  opacity: isPinned.value ? '1' : undefined
}))

// 더블클릭 시 메모장의 고정/투명 토글
const pinMemo = (e: MouseEvent) => {
  isPinned.value = !isPinned.value;
  saveMessage.value = isPinned.value ? "메모장이 고정되었습니다" : "메모장이 투명해졌습니다";
  setTimeout(() => (saveMessage.value = ""), 2000);
};

// 드래그 관련 함수들
const startDrag = (e: MouseEvent) => {
  // 드래그핸들이 아닌 영역 클릭시 무시
  if (!dragHandle.value || !dragHandle.value.contains(e.target as Node)) return

  isDragging.value = true
  dragOffset.value = {
    x: e.clientX - position.value.x,
    y: e.clientY - position.value.y
  }

  memoPad.value?.addEventListener('mousemove', onDrag)
  memoPad.value?.addEventListener('mouseup', stopDrag)
}

const onDrag = (e: MouseEvent) => {
  if (!isDragging.value) return
  position.value = {
    x: e.clientX - dragOffset.value.x,
    y: e.clientY - dragOffset.value.y
  }
}

const stopDrag = () => {
  isDragging.value = false
  memoPad.value?.removeEventListener('mousemove', onDrag)
  memoPad.value?.removeEventListener('mouseup', stopDrag)
}

// 리사이즈 관련 함수들
const startResize = (e: MouseEvent) => {
  e.preventDefault()
  isResizing.value = true
  initialMouse.value = { x: e.clientX, y: e.clientY }
  initialDimensions.value = { width: padWidth.value, height: padHeight.value }
  document.addEventListener('mousemove', onResize)
  document.addEventListener('mouseup', stopResize)
}

const onResize = (e: MouseEvent) => {
  if (!isResizing.value) return
  const dx = e.clientX - initialMouse.value.x
  const dy = e.clientY - initialMouse.value.y

  // 최소 크기 설정: 너비 300px, 높이 350px로 설정
  padWidth.value = Math.max(initialDimensions.value.width + dx, 300)
  padHeight.value = Math.max(initialDimensions.value.height + dy, 350)

  // textarea 높이 조절 (패딩, 마진 등을 고려하여 적절한 값 설정)
  textareaHeight.value = Math.max(padHeight.value - 287, 50)
}

const stopResize = () => {
  isResizing.value = false
  document.removeEventListener('mousemove', onResize)
  document.removeEventListener('mouseup', stopResize)
}

const toggleOpacity = () => {
  memoPad.value!.style.opacity = memoPad.value!.style.opacity === '1' ? '0.2' : '1';
}

// 초기 위치 및 크기를 화면 정가운데로 설정
onMounted(() => {
  position.value = {
    x: (window.innerWidth - 300) / 2,
    y: (window.innerHeight - 500) / 2
  }
  padWidth.value = 300
  padHeight.value = 500
})

// store와 동기화 (메모 텍스트 변경시)
watch(() => memoText.value, (newText) => {
  interviewStore.setMemo({
    applicantName: applicantName.value,
    memoText: newText
  })
})

// 자동 저장~~
watch(() => props.autoSave, (newValue) => {
  if (newValue) {
    saveMemo()
  }
})

const saveMemo = () => {
  if (!applicantName.value.trim()) {
    saveMessage.value = "알바생 이름을 입력해주세요!"
    setTimeout(() => (saveMessage.value = ""), 2000)
    return
  }
  if (!memoText.value.trim()) {
    saveMessage.value = "저장할 메모가 없습니다!"
    setTimeout(() => (saveMessage.value = ""), 2000)
    return
  }

  const newMemo = {
    id: Date.now(),
    name: applicantName.value,
    text: memoText.value,
    timestamp: new Date().toLocaleString()
  }
  const stored = localStorage.getItem(STORAGE_KEY_ALL_MEMOS)
  const memosArr = stored ? JSON.parse(stored) : []
  memosArr.push(newMemo)
  localStorage.setItem(STORAGE_KEY_ALL_MEMOS, JSON.stringify(memosArr))

  // 현재 메모 데이터도 별도로 저장 (전체 면접 결과 활용)
  localStorage.setItem(
    STORAGE_KEY_CURRENT,
    JSON.stringify({
      applicantName: applicantName.value,
      memoText: memoText.value,
      timestamp: new Date().toLocaleString()
    })
  )

  // 스토어에도 저장
  interviewStore.saveMemo({
    applicantName: applicantName.value,
    memoText: memoText.value
  })

  saveMessage.value = "메모가 저장되었습니다!"
  setTimeout(() => (saveMessage.value = ""), 2000)
}

const clearMemo = () => {
  memoText.value = ""
  interviewStore.clearMemo()
}
</script>

<style scoped>
.memo-pad {
  position: fixed;
  background: #f5faf9; /* 밝은 중립 색상 */
  border: 2px solid #2D3250;  /* 진한 네이비 블루 */
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
  padding: 1.5rem;
  z-index: 1000;
  user-select: none;
  opacity: 0.2;
}

.memo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: move;
  padding-bottom: 0.5rem;
  border-bottom: 2px dashed #424769;  /* 중간 톤 네이비 블루 */
}

.header-left h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #2D3250;  /* 진한 네이비 블루 */
}

.header-right {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.input-section {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.input-group label {
  font-size: 1rem;
  color: #424769;  /* 중간 톤 네이비 블루 */
}

.name-input,
textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #E0E0E0;  /* 중립 색상 */
  border-radius: 6px;
  font-size: 1rem;
  resize: none;
  box-sizing: border-box;
}

.button-row {
  display: flex;
  gap: 0.5rem;
}

.button-row button {
  flex: 1;
  padding: 0.5rem;
  background: #2D3250;  /* 진한 네이비 블루 */
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.3s;
}

.button-row button:hover {
  background: #424769;  /* 중간 톤 네이비 블루 */
}

.message {
  position: absolute;
  bottom: -30px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 1rem;
}

.resize-handle {
  position: absolute;
  width: 20px;
  height: 20px;
  right: 5px;
  bottom: 5px;
  cursor: se-resize;
  background-color: #424769;  /* 중간 톤 네이비 블루 */
  border-radius: 4px;
}
</style>
