---
name: api-testing
description: API endpoint testing patterns with curl commands for local development and debugging.
---

# API Testing

로컬 서버 (localhost:8080) 기준 API 테스트 가이드.

## 인증 플로우

```bash
# Kakao 로그인
curl -X POST http://localhost:8080/api/auth/kakao \
  -H "Content-Type: application/json" \
  -d '{"accessToken": "KAKAO_ACCESS_TOKEN"}'

# Google 로그인
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"idToken": "GOOGLE_ID_TOKEN"}'

# 토큰 갱신
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "REFRESH_TOKEN"}'

# 로그아웃
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer ACCESS_TOKEN"
```

## 기록 API

```bash
# 월별 기록 조회
curl http://localhost:8080/api/records?yearMonth=2024-03 \
  -H "Authorization: Bearer ACCESS_TOKEN"

# 기록 생성
curl -X POST http://localhost:8080/api/records \
  -H "Authorization: Bearer ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"date": "2024-03-29", "content": "운동 1시간", "category": "EXERCISE"}'

# 기록 수정
curl -X PUT http://localhost:8080/api/records/1 \
  -H "Authorization: Bearer ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"content": "운동 2시간", "category": "EXERCISE"}'

# 기록 삭제
curl -X DELETE http://localhost:8080/api/records/1 \
  -H "Authorization: Bearer ACCESS_TOKEN"

# 기록 날짜 목록
curl http://localhost:8080/api/records/dates?yearMonth=2024-03 \
  -H "Authorization: Bearer ACCESS_TOKEN"

# 스트릭 조회
curl http://localhost:8080/api/records/streak \
  -H "Authorization: Bearer ACCESS_TOKEN"
```

## 카테고리 값

- `EXERCISE` - 운동
- `STUDY` - 공부
- `WORK` - 업무
- `HOBBY` - 취미
- `ETC` - 기타
