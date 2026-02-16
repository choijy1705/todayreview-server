package com.junyoung.todayreview.record.dto

import com.junyoung.todayreview.record.entity.Category
import com.junyoung.todayreview.record.entity.DailyRecord
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateRecordRequest(
    @field:NotNull(message = "날짜는 필수입니다.")
    val date: LocalDate,

    @field:NotBlank(message = "내용은 필수입니다.")
    @field:Size(max = 100, message = "내용은 100자 이내여야 합니다.")
    val content: String,

    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: Category,
)

data class UpdateRecordRequest(
    @field:NotBlank(message = "내용은 필수입니다.")
    @field:Size(max = 100, message = "내용은 100자 이내여야 합니다.")
    val content: String,

    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: Category,
)

data class RecordResponse(
    val id: Long,
    val date: LocalDate,
    val content: String,
    val category: Category,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun from(record: DailyRecord): RecordResponse = RecordResponse(
            id = record.id,
            date = record.date,
            content = record.content,
            category = record.category,
            createdAt = record.createdAt,
            updatedAt = record.updatedAt,
        )
    }
}

data class RecordDatesResponse(
    val dates: List<LocalDate>,
)

data class StreakResponse(
    val streakDays: Int,
)
