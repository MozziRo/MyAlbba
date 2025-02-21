# 1단계: Vue 프로젝트 빌드
FROM node:18 AS builder
WORKDIR /app

# package.json과 package-lock.json 먼저 복사 (의존성 캐시 활용)
COPY ./Front/package.json ./Front/package-lock.json ./

# npm install 실행 (의존성 캐시 활용)
RUN npm install

# ✅ 전체 프로젝트 복사 (public 폴더 포함)
COPY ./Front ./

# Vue 프로젝트 빌드
RUN npm run build

# 2단계: Nginx 설정
FROM nginx:alpine AS runner
WORKDIR /usr/share/nginx/html

# ✅ Vue 빌드 결과물을 Nginx로 복사
COPY --from=builder /app/dist .

# ✅ Nginx 설정 파일 복사
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# ✅ 환경 변수 적용 스크립트 복사 및 실행 권한 추가
COPY ./nginx/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# 포트 개방
EXPOSE 80 443

# Nginx 실행
CMD ["/docker-entrypoint.sh"]

