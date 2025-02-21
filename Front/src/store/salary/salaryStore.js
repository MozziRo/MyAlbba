import { ref } from 'vue'
import { defineStore } from 'pinia'
import { getAllMonthlyPayments, completeSalarySettlement, getOwnerBankName } from '@/common/apis/api/salary'
import { updateWorkerSalary } from '@/common/apis/api/worker'
export const useSalaryStore = defineStore('salary', () => {
  const monthlyPayments = ref([])  // 상태 변수
  const isLoading = ref(false)     // 로딩 상태
  const error = ref(null)          // 오류 메시지
  const ownerBankName = ref(null)

  // 월급 정보 가져와서 저장
  const fetchSalaryList = async () => {
    isLoading.value = true
    try {
      const monthlyPaymentsResponse = await getAllMonthlyPayments()
      monthlyPayments.value = monthlyPaymentsResponse
      ownerBankName.value = await getOwnerBankName()
    } catch (err) {
      error.value = err.message
      console.error('급여 목록 조회 실패:', err)
    } finally {
      isLoading.value = false
    }
  }

  // 알바생 급여 설정 업데이트
  const updateWorkerSalarySettings = async (salaryData) => {
    isLoading.value = true
    try {
      await updateWorkerSalary(salaryData)
      await fetchSalaryList() // 데이터 갱신
      // 딱 바뀐 것만 재렌더링 하면 안되나?
      // 일단은 fetchSalaryList 함수 호출
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  // 월급 정산 완료
  const completeSalaryCalculation = async (salaryId) => {
    isLoading.value = true
    try {
      await completeSalarySettlement(salaryId)
      await fetchSalaryList() // 데이터 갱신
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      isLoading.value = false
    }
  }

  return {
    monthlyPayments,
    ownerBankName,
    isLoading,
    error,
    fetchSalaryList,
    updateWorkerSalarySettings,
    completeSalaryCalculation
  }
})
