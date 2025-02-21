<template>
  <div class="print-options">
    <label class="fancy-checkbox">
      <input type="checkbox" v-model="modelValue.includeMemo">
      <span class="checkbox-custom"></span>
      <span class="label-text">메모</span>
    </label>
    <label class="fancy-checkbox">
      <input type="checkbox" v-model="modelValue.includeAnalysis">
      <span class="checkbox-custom"></span>
      <span class="label-text">분석 결과</span>
    </label>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.print-options {
  position: absolute;
  right: 20px;
  top: 100px;
  display: flex;
  flex-direction: column;
}

.fancy-checkbox {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin: 0.25rem;
}

.fancy-checkbox input {
  display: none;
}

.checkbox-custom {
  width: 20px;
  height: 20px;
  border: 2px solid #2D3250;  /* 진한 네이비 블루 */
  border-radius: 50%;
  margin-right: 8px;
  position: relative;
  transition: all 0.3s ease;
}

.checkbox-custom::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) scale(0);
  width: 12px;
  height: 12px;
  background: #2D3250;  /* 진한 네이비 블루 */
  border-radius: 50%;
  transition: transform 0.2s ease;
}

.fancy-checkbox input:checked + .checkbox-custom::after {
  transform: translate(-50%, -50%) scale(1);
}

.label-text {
  margin-left: 4px;
  color: #424769;  /* 중간 톤 네이비 블루 */
  font-size: 1rem;
}

.fancy-checkbox:hover .checkbox-custom {
  border-color: #424769;  /* 중간 톤 네이비 블루 */
}

.fancy-checkbox input:checked + .checkbox-custom {
  border-color: #424769;  /* 중간 톤 네이비 블루 */
}

.fancy-checkbox input:checked + .checkbox-custom::after {
  background: #424769;  /* 중간 톤 네이비 블루 */
}
</style>
