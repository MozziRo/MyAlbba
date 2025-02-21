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
  define: { global: 'window' },  // �۷ι� ��ü ���� ����

  build: {
    outDir: 'dist', // ? ����� ������ ����� ����
    publicDir: 'public', // ? public ���� �ڵ� ����
    rollupOptions: {
      input: fileURLToPath(new URL('index.html', import.meta.url)), // ? index.html ��� ����
    },
  },
})

