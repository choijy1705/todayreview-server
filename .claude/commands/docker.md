---
description: Docker 빌드 및 로컬 환경 관리
---

# Docker

Docker 관련 작업을 수행합니다.

## 로컬 DB

```bash
# DB 시작
docker compose up -d

# DB 중지
docker compose down

# DB + 볼륨 삭제 (데이터 초기화)
docker compose down -v
```

## 애플리케이션 빌드

```bash
# 이미지 빌드
docker build -t todayreview-server .

# 컨테이너 실행
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/todayreview \
  -e DATABASE_USERNAME=todayreview \
  -e DATABASE_PASSWORD=todayreview \
  -e JWT_SECRET=your-secret \
  -e GOOGLE_CLIENT_ID=your-client-id \
  todayreview-server
```
