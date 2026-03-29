---
description: 새로운 API 엔드포인트 생성 (Controller + Service + DTO)
---

# API

새로운 API 엔드포인트를 생성합니다. 이 프로젝트의 패턴을 따라 일관성 있게 작성합니다.

## 생성 순서

1. **DTO 작성** - Request/Response data class (validation 포함)
2. **Service 작성** - 비즈니스 로직 (기존 서비스에 추가 또는 새 서비스 생성)
3. **Controller 작성** - REST 엔드포인트 (기존 컨트롤러에 추가 또는 새 컨트롤러 생성)
4. **테스트 작성** - 서비스 및 컨트롤러 테스트

## 패턴 준수사항

- 응답은 반드시 `ApiResponse.success(data)` 래퍼 사용
- 인증이 필요한 엔드포인트는 `SecurityContextHolder`에서 userId 추출
- 입력 검증: `@Valid` + Jakarta Validation 어노테이션
- 에러: `BusinessException(HttpStatus, message)` 사용
- 엔티티를 직접 반환하지 않고 DTO로 변환
