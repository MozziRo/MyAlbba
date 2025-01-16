<template>
    <div class="header">
      <div>화상면접</div>
      <div class="profile">
        <span>사용자</span>
        <img src="https://via.placeholder.com/50" alt="Profile" />
      </div>
    </div>
  </template>
  
  <script lang="ts" setup></script>
  
  <style scoped>
  .header {
    background-color: #ffeeba;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .profile {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .profile img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
  }
  </style>
  