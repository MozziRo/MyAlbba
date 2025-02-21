<template>
  <SalaryNoticeBar />

  <div class="manager-view">
    <div class="table-container">
      <SalaryTable />
    </div>
  </div>
</template>

<script setup>
import SalaryTable from './SalaryTable.vue'
import SalaryNoticeBar from './SalaryNoticeBar.vue'
</script>

<style scoped>
.manager-view .salary-notice {
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.manager-view .table-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>
