# TodayReview Server

매일의 활동과 회고를 기록하고 추적하는 개인 일일 리뷰 서비스의 백엔드 서버입니다.

## Tech Stack

- **Language:** Kotlin
- **Framework:** Spring Boot 3.3.5
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA + Hibernate
- **Migration:** Flyway
- **Auth:** JWT (jjwt 0.12.6) + OAuth (Google, Kakao)
- **Build:** Gradle (Kotlin DSL)
- **Runtime:** Java 17
- **Container:** Docker (multi-stage build)

## Features

### 일일 기록 관리
- 카테고리별 일일 활동 기록 (운동, 공부, 업무, 취미, 기타)
- 월별 기록 조회 및 날짜별 기록 확인
- 연속 기록 스트릭(streak) 계산

### 소셜 로그인 인증
- Google OAuth / Kakao OAuth 지원
- JWT 기반 Stateless 인증
- Refresh Token Rotation 방식의 토큰 갱신

## Project Structure

```
src/main/kotlin/com/junyoung/todayreview/
├── auth/                  # 인증 모듈 (OAuth, JWT)
│   ├── controller/        # 로그인, 로그아웃, 토큰 갱신 API
│   ├── dto/               # 요청/응답 DTO
│   ├── entity/            # RefreshToken 엔티티
│   ├── jwt/               # JwtProvider, JwtAuthenticationFilter
│   ├── repository/        # RefreshToken 레포지토리
│   └── service/           # AuthService, KakaoOAuth, GoogleOAuth
├── record/                # 일일 기록 모듈
│   ├── controller/        # 기록 CRUD API
│   ├── dto/               # 요청/응답 DTO
│   ├── entity/            # DailyRecord, Category 엔티티
│   ├── repository/        # DailyRecord 레포지토리
│   └── service/           # RecordService (월별 조회, 스트릭 등)
├── user/                  # 사용자 모듈
│   ├── entity/            # User 엔티티
│   └── repository/        # User 레포지토리
├── common/                # 공통 모듈
│   ├── dto/               # ApiResponse 래퍼
│   ├── entity/            # BaseTimeEntity (audit)
│   └── exception/         # BusinessException, GlobalExceptionHandler
└── config/                # SecurityConfig, WebConfig, JacksonConfig
```

## API Endpoints

### Auth (`/api/auth`)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/auth/kakao` | - | Kakao 소셜 로그인 |
| POST | `/api/auth/google` | - | Google 소셜 로그인 |
| POST | `/api/auth/refresh` | - | Access Token 갱신 |
| POST | `/api/auth/logout` | Required | 로그아웃 |

### Records (`/api/records`)

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/records?yearMonth=2024-03` | Required | 월별 기록 조회 |
| GET | `/api/records/{id}` | Required | 단건 기록 조회 |
| GET | `/api/records/dates?yearMonth=2024-03` | Required | 기록이 있는 날짜 목록 |
| GET | `/api/records/streak` | Required | 연속 기록 일수 |
| POST | `/api/records` | Required | 기록 생성 |
| PUT | `/api/records/{id}` | Required | 기록 수정 |
| DELETE | `/api/records/{id}` | Required | 기록 삭제 |

## Entity Relationships

```
User (1) ──── (N) DailyRecord
     (1) ──── (N) RefreshToken
```

- **User:** socialId + provider 조합으로 고유 식별
- **DailyRecord:** 날짜별 복수 기록 가능, 카테고리 분류
- **RefreshToken:** 토큰 로테이션, 로그아웃 시 전체 삭제

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 16 (or Docker)

### Local Development

1. **데이터베이스 실행**
   ```bash
   docker compose up -d
   ```

2. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

   서버가 `http://localhost:8080`에서 시작됩니다.

### Production (Docker)

```bash
docker build -t todayreview-server .
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host:5432/todayreview \
  -e DATABASE_USERNAME=user \
  -e DATABASE_PASSWORD=pass \
  -e JWT_SECRET=your-256-bit-secret \
  -e GOOGLE_CLIENT_ID=your-client-id \
  todayreview-server
```

### Environment Variables (Production)

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | JDBC PostgreSQL connection string |
| `DATABASE_USERNAME` | DB username |
| `DATABASE_PASSWORD` | DB password |
| `JWT_SECRET` | JWT signing secret (256-bit 이상) |
| `GOOGLE_CLIENT_ID` | Google OAuth Client ID |
| `PORT` | Server port (default: 8080) |
