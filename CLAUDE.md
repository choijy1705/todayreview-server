# TodayReview Server - Claude Code 프로젝트 가이드

## 프로젝트 개요

Kotlin + Spring Boot 3.3.5 기반의 일일 리뷰 서비스 백엔드.
PostgreSQL 16 + Flyway 마이그레이션, JWT 인증, Google/Kakao OAuth 지원.

## 빌드 & 실행

```bash
# 로컬 DB 실행
docker compose up -d

# 빌드
./gradlew build

# 테스트
./gradlew test

# 실행
./gradlew bootRun

# 빌드만 (테스트 제외)
./gradlew build -x test
```

## 프로젝트 구조

```
src/main/kotlin/com/junyoung/todayreview/
├── auth/          # 인증 (OAuth, JWT, RefreshToken)
├── record/        # 일일 기록 CRUD, 스트릭
├── user/          # 사용자 엔티티/레포지토리
├── common/        # ApiResponse, BaseTimeEntity, 예외처리
└── config/        # Security, Web, Jackson, JPA 설정
```

## 핵심 규칙

- **Kotlin** - Java가 아닌 Kotlin으로 작성
- **Immutable** - data class 사용, var 대신 val 선호
- **DB 마이그레이션** - DDL 변경은 반드시 `src/main/resources/db/migration/` 에 Flyway 파일 추가
- **엔티티 직접 노출 금지** - 반드시 DTO로 변환하여 응답
- **API 응답 래퍼** - 모든 응답은 `ApiResponse<T>` 형태로 반환
- **인증** - `/api/auth/**` 외 모든 엔드포인트는 JWT 인증 필수
- **예외 처리** - `BusinessException`으로 통일, GlobalExceptionHandler에서 처리

## 컨벤션

- 패키지: 도메인별 분리 (auth, record, user, common, config)
- 엔티티: `BaseTimeEntity` 상속으로 createdAt/updatedAt 자동 관리
- 레포지토리: Spring Data JPA 메서드 네이밍 규칙 준수
- 테스트: `src/test/kotlin/` 하위에 동일 패키지 구조

## 환경 설정

- `application.yml` - 로컬 개발 (localhost:5432)
- `application-prod.yml` - 프로덕션 (환경변수 참조)
- Docker Compose - 로컬 PostgreSQL 16
