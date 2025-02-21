<template>
  <baseInfoForm @prev="$emit('prev')" @submit="handleSubmit" @check-duplicate-email="handleEmail" :emailDuplicateResponse="emailDuplicateResponse">
    <template #additional-fields>
      <h3 class="title">가게 정보 입력</h3>
      <div class="form-group">
        <input
          type="text"
          id="storeName"
          v-model="storeData.storeName"
          placeholder="가게 이름을 입력해주세요"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          id="storeAddress"
          v-model="storeData.storeAddress"
          placeholder="가게 주소를 입력해주세요"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="text"
          id="storePhoneNumber"
          v-model="storeData.storePhoneNumber"
          placeholder="가게 전화번호를 - 없이 입력해주세요"
          required
        >
      </div>
      <div class="form-group">
        <input
          type="number"
          id="payday"
          v-model="storeData.payday"
          placeholder="월급 정산일을 숫자만 입력해주세요"
          required
        >
      </div>
    </template>
  </BaseInfoForm>
</template>

<script setup>
import baseInfoForm from './baseInfoForm.vue';
import { ref, defineProps } from 'vue';

const props = defineProps({
  emailDuplicateResponse: Object
})

const storeData = ref({
  storeName: '',
  storeAddress: '',
  storePhoneNumber: '',
  payday: '',
})

const emit = defineEmits(['prev', 'submit', 'check-duplicate-email'])

const handleSubmit = (baseData) => {
  emit('submit', { ...storeData.value, ...baseData, role: 'owner' })
}

const handleEmail = (inputEmail) => {
  emit('check-duplicate-email', {
    role: 'owner', email: inputEmail,
  })
}

</script>

<style>

</style>
