import { defineStore } from 'pinia'
import { ref } from 'vue'

interface MemoData {
  applicantName: string
  memoText: string
  timestamp: string
  id?: number
}

export const useInterviewStore = defineStore('interview', () => {
  const currentMemo = ref<MemoData>({
    applicantName: '',
    memoText: '',
    timestamp: ''
  })

  const savedMemos = ref<MemoData[]>([])

  const setMemo = (memo: Partial<MemoData>) => {
    currentMemo.value = {
      ...currentMemo.value,
      ...memo,
      timestamp: new Date().toLocaleString()
    }
  }

  const saveMemo = (memo: Partial<MemoData>) => {
    savedMemos.value.push({
      ...memo,
      id: Date.now(),
      timestamp: new Date().toLocaleString()
    } as MemoData)
  }

  const clearMemo = () => {
    currentMemo.value = {
      applicantName: '',
      memoText: '',
      timestamp: ''
    }
  }

  return {
    currentMemo,
    savedMemos,
    setMemo,
    saveMemo,
    clearMemo
  }
})
