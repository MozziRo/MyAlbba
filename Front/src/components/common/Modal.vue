<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-container">
      <div class="modal-content">
        <div class="modal-header">
          <slot name="header"></slot>
        </div>
      
        <div class="modal-body">
          <slot name="body"></slot>
        </div>
      
        <div class="modal-footer">
          <slot name="footer"></slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['close'])
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.modal-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(66, 71, 105, 0.1);
  width: 1000px;
  max-width: 95vw;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.modal-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-body {
  flex: 1;
  padding: 20px;
  overflow: auto;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>