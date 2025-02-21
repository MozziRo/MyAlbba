#!/bin/sh

# ✅ 환경 변수 적용된 env-config.js 생성
cat <<EOF > /usr/share/nginx/html/env-config.js
window.env = {
  VITE_APP_API_URL: "${VITE_APP_API_URL}",
  VITE_APP_WS_URL: "${VITE_APP_WS_URL}",
  VITE_APP_PURE_URL: "${VITE_APP_PURE_URL}"
};
EOF

# ✅ env-config.js 파일 확인 (디버깅용)
cat /usr/share/nginx/html/env-config.js

# ✅ Nginx 실행
nginx -g "daemon off;"


