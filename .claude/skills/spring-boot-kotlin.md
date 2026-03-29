---
name: spring-boot-kotlin
description: Spring Boot + Kotlin development patterns for this project. Covers entity design, service patterns, security configuration, and JPA best practices.
---

# Spring Boot + Kotlin Patterns

## Entity Pattern

```kotlin
@Entity
@Table(name = "table_name")
class EntityName(
    @Column(nullable = false)
    val field: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: StatusEnum,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) : BaseTimeEntity()
```

## Service Pattern

```kotlin
@Service
@Transactional(readOnly = true)
class SomeService(
    private val repository: SomeRepository
) {
    fun findById(id: Long): SomeResponse {
        val entity = repository.findById(id)
            .orElseThrow { BusinessException(HttpStatus.NOT_FOUND, "Not found") }
        return SomeResponse.from(entity)
    }

    @Transactional
    fun create(userId: Long, request: CreateRequest): SomeResponse {
        val entity = SomeEntity(
            userId = userId,
            field = request.field
        )
        return SomeResponse.from(repository.save(entity))
    }
}
```

## Controller Pattern

```kotlin
@RestController
@RequestMapping("/api/some")
class SomeController(
    private val someService: SomeService
) {
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ApiResponse<SomeResponse> {
        val userId = SecurityContextHolder.getContext()
            .authentication.principal as Long
        return ApiResponse.success(someService.findById(userId, id))
    }

    @PostMapping
    fun create(@Valid @RequestBody request: CreateRequest): ApiResponse<SomeResponse> {
        val userId = SecurityContextHolder.getContext()
            .authentication.principal as Long
        return ApiResponse.success(someService.create(userId, request))
    }
}
```

## DTO Pattern

```kotlin
data class CreateRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val content: String,

    @field:NotNull
    val category: Category
)

data class SomeResponse(
    val id: Long,
    val content: String,
    val category: Category,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(entity: SomeEntity) = SomeResponse(
            id = entity.id,
            content = entity.content,
            category = entity.category,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
}
```

## Repository Pattern

```kotlin
interface SomeRepository : JpaRepository<SomeEntity, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): SomeEntity?
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<SomeEntity>

    @Query("SELECT e FROM SomeEntity e WHERE e.userId = :userId AND e.status = :status")
    fun findByUserIdAndStatus(userId: Long, status: Status): List<SomeEntity>
}
```

## Exception Handling

```kotlin
// 비즈니스 예외는 BusinessException 사용
throw BusinessException(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다")
throw BusinessException(HttpStatus.FORBIDDEN, "권한이 없습니다")
throw BusinessException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다")
```

## Flyway Migration

- 경로: `src/main/resources/db/migration/V{N}__{description}.sql`
- JPA ddl-auto는 `validate` - 스키마 변경은 반드시 Flyway로
- PostgreSQL 문법 사용 (BIGSERIAL, VARCHAR, TIMESTAMP 등)
