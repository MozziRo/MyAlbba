import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import vuetify from 'vite-plugin-vuetify'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    vuetify(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  define: { global: 'window' },  // 글로벌 객체 설정 유지

  build: {
    outDir: 'dist', // ? 빌드된 파일이 저장될 폴더
    publicDir: 'public', // ? public 폴더 자동 복사
    rollupOptions: {
      input: fileURLToPath(new URL('index.html', import.meta.url)), // ? index.html 경로 수정
    },
  },
})

