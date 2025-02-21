<template>
  <div class="stock-widget">
    <!-- <h3 class="widget-title">재고 현황</h3> -->
    <table class="stock-table">
      <thead>
        <tr>
          <th>상품</th>
          <th>수량</th>
          <th>상태</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in stockItems" :key="item.name">
          <td>{{ item.name }}</td>
          <td>{{ item.quantity }}</td>
          <td :class="getStatusClass(item.quantity)">{{ getStatusText(item.quantity) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const stockItems = ref([
  { name: '상품 A', quantity: 50 },
  { name: '상품 B', quantity: 10 },
  { name: '상품 C', quantity: 0 }
]);

const getStatusText = (quantity) => {
  if (quantity === 0) return '품절';
  if (quantity < 20) return '부족';
  return '정상';
};

const getStatusClass = (quantity) => {
  if (quantity === 0) return 'out-of-stock';
  if (quantity < 20) return 'low-stock';
  return 'in-stock';
};
</script>

<style scoped>
.widget-title {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.stock-table {
  width: 100%;
  border-collapse: collapse;
}

.stock-table th,
.stock-table td {
  border: 1px solid #ddd;
  padding: 8px;
}

.out-of-stock {
  color: #F9B17A;  /* Secondary color */
}

.low-stock {
  color: #FCDDBD;  /* Secondary light */
}

.in-stock {
  color: #424769;  /* Primary color */
}
</style>
