import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '^/api/': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        configure: (proxy) => {
          proxy.on('proxyReq', function (proxyReq) {
            proxyReq.removeHeader('referer')
            proxyReq.removeHeader('origin')
          })
        }
      }
    }
  }
})
