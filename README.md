# TodayReview Server

매일의 활동과 회고를 기록하고 추적하는 개인 일일 리뷰 서비스의 백엔드 서버입니다.

> **Built with [Claude Code](https://claude.ai/claude-code)** — 설계부터 구현, 배포 설정까지 AI와 협업하여 개발한 프로젝트입니다.

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

## AI-Assisted Development

이 프로젝트는 [Claude Code](https://claude.ai/claude-code) (Anthropic의 AI 코딩 에이전트)를 활용하여 개발되었습니다. 단순히 코드 생성에 그치지 않고, **개발 전 과정에서 AI를 체계적으로 활용**하는 워크플로우를 구축했습니다.

### AI 활용 방식

**1. 아키텍처 설계 단계**
- AI 에이전트(`planner`, `architect`)를 통해 도메인 분리 구조와 레이어별 구현 계획을 수립
- 기능 추가 시 영향도 분석과 리스크 평가를 자동화하여 설계 품질 확보

**2. 구현 단계**
- `api-designer` 에이전트로 Controller → Service → DTO → Repository 레이어를 프로젝트 컨벤션에 맞게 일관되게 생성
- `tdd-guide` 에이전트로 JUnit5 + MockK 기반 테스트 주도 개발 워크플로우 적용
- Flyway 마이그레이션, Spring Security 설정 등 보일러플레이트가 많은 영역에서 AI를 적극 활용

**3. 품질 관리 단계**
- `code-reviewer` 에이전트가 N+1 쿼리, 트랜잭션 범위, 입력 검증 누락 등을 자동 검토
- `security-reviewer` 에이전트로 JWT 설정, OAuth 흐름, OWASP Top 10 취약점을 코드 작성 직후 점검
- `build-error-resolver` 에이전트로 Kotlin 컴파일 에러 및 Gradle 빌드 실패를 빠르게 진단/수정

**4. 개발환경 자동화**
- 프로젝트 전용 커스텀 커맨드(`/build`, `/test`, `/migrate`, `/api`, `/docker`)로 반복 작업을 한 줄로 실행
- Git hooks를 통한 파괴적 명령 차단, println 사용 경고 등 실수 방지 자동화
- `CLAUDE.md`에 프로젝트 규칙을 명시하여 AI가 항상 동일한 컨벤션을 따르도록 유지

### 프로젝트 내 AI 설정 구조

```
.claude/
├── agents/                # 역할별 AI 에이전트 (7개)
│   ├── planner.md         # 기능 구현 계획 수립
│   ├── architect.md       # 아키텍처 설계 검토
│   ├── api-designer.md    # REST API 일관된 패턴 구현
│   ├── tdd-guide.md       # 테스트 주도 개발 가이드
│   ├── code-reviewer.md   # 코드 품질 리뷰
│   ├── security-reviewer.md # 보안 취약점 검토
│   └── build-error-resolver.md # 빌드 에러 자동 해결
├── commands/              # 커스텀 슬래시 커맨드 (5개)
│   ├── build.md           # /build - Gradle 빌드 및 에러 수정
│   ├── test.md            # /test - 테스트 실행 및 분석
│   ├── migrate.md         # /migrate - Flyway 마이그레이션 생성
│   ├── api.md             # /api - 새 API 엔드포인트 생성
│   └── docker.md          # /docker - Docker 환경 관리
├── skills/                # 프로젝트 지식 베이스 (2개)
│   ├── spring-boot-kotlin.md # Kotlin/Spring Boot 패턴 레퍼런스
│   └── api-testing.md     # curl 기반 API 테스트 가이드
└── settings.local.json    # 권한, hooks 설정
```

### AI 활용에서 중요하게 생각한 점

- **AI에게 맥락을 충분히 제공**: `CLAUDE.md`에 프로젝트 규칙, 컨벤션, 기술 스택을 명시하여 매번 설명할 필요 없이 일관된 코드를 생성하도록 함
- **역할 분리를 통한 전문성 확보**: 하나의 AI에게 모든 것을 맡기지 않고, 계획/구현/리뷰/보안 등 역할별 에이전트를 분리하여 각 단계의 품질을 높임
- **자동화와 가드레일**: hooks를 통해 실수 방지(파괴적 명령 차단, 안티패턴 경고)를 자동화하고, 커맨드를 통해 반복 작업의 일관성을 유지
- **AI 출력을 무조건 신뢰하지 않음**: `code-reviewer`, `security-reviewer`를 통해 AI가 생성한 코드도 반드시 검증하는 프로세스를 포함
