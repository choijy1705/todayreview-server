---
description: 테스트 실행 및 결과 분석
---

# Test

테스트 실행 및 실패 분석:

1. 전체 테스트: `./gradlew test`
2. 특정 테스트: `./gradlew test --tests "패턴"`

3. 실패 시:
   - 테스트 리포트 확인: `build/reports/tests/test/index.html`
   - 실패 원인 분석
   - 테스트 또는 구현 코드 수정
   - 재실행하여 검증

4. 결과 요약:
   - 통과/실패/스킵 건수
   - 실패한 테스트 목록과 원인
