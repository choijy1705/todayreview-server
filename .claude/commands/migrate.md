---
description: Flyway DB 마이그레이션 파일 생성
---

# Migrate

새로운 Flyway 마이그레이션 파일을 생성합니다.

1. 기존 마이그레이션 확인: `src/main/resources/db/migration/` 디렉토리의 파일 목록 조회
2. 다음 버전 번호 결정 (V{N}__ 형식)
3. 사용자가 요청한 DDL 변경사항으로 SQL 파일 작성
4. 파일명 규칙: `V{N}__{description}.sql` (언더스코어 2개)

주의사항:
- JPA 엔티티와 마이그레이션 SQL이 일치하는지 확인
- 기존 데이터에 영향을 주는 변경은 사용자에게 경고
- PostgreSQL 문법 사용
