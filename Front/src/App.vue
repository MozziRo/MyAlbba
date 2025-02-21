<template>
  <v-app>
    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>

<style>
:root {
  width: 100%;
  min-height: 100vh;
  font-family: 'Pretendard';

  /* Primary Colors */
  --primary-dark: #2D3250;
  --primary: #424769;
  --primary-light: #676F9D;

  /* Secondary Colors */
  --accent: #F9B17A;
  --accent-light: #FCDDBD;

  /* Neutral Colors */
  --neutral-light: #F5F5F5;
  --neutral: #E0E0E0;
  --neutral-dark: #9E9E9E;
}
</style>
