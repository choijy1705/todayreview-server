package com.junyoung.todayreview.record.service

import com.junyoung.todayreview.common.exception.BusinessException
import com.junyoung.todayreview.record.dto.*
import com.junyoung.todayreview.record.entity.DailyRecord
import com.junyoung.todayreview.record.repository.DailyRecordRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.YearMonth

@Service
@Transactional(readOnly = true)
class RecordService(
    private val dailyRecordRepository: DailyRecordRepository,
) {

    fun getMonthlyRecords(userId: Long, yearMonth: YearMonth): List<RecordResponse> {
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()
        return dailyRecordRepository
            .findByUserIdAndDateBetweenOrderByDateAscCreatedAtAsc(userId, start, end)
            .map(RecordResponse::from)
    }

    fun getRecord(userId: Long, id: Long): RecordResponse {
        val record = dailyRecordRepository.findByIdAndUserId(id, userId)
            ?: throw BusinessException(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다.")
        return RecordResponse.from(record)
    }

    @Transactional
    fun createRecord(userId: Long, request: CreateRecordRequest): RecordResponse {
        val record = dailyRecordRepository.save(
            DailyRecord(
                userId = userId,
                date = request.date,
                content = request.content,
                category = request.category,
            )
        )
        return RecordResponse.from(record)
    }

    @Transactional
    fun updateRecord(userId: Long, id: Long, request: UpdateRecordRequest): RecordResponse {
        val record = dailyRecordRepository.findByIdAndUserId(id, userId)
            ?: throw BusinessException(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다.")

        record.update(request.content, request.category)
        return RecordResponse.from(record)
    }

    @Transactional
    fun deleteRecord(userId: Long, id: Long) {
        val record = dailyRecordRepository.findByIdAndUserId(id, userId)
            ?: throw BusinessException(HttpStatus.NOT_FOUND, "기록을 찾을 수 없습니다.")
        dailyRecordRepository.delete(record)
    }

    fun getRecordDates(userId: Long, yearMonth: YearMonth): RecordDatesResponse {
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()
        val dates = dailyRecordRepository.findDatesByUserIdAndDateBetween(userId, start, end)
        return RecordDatesResponse(dates)
    }

    fun getStreak(userId: Long): StreakResponse {
        val today = LocalDate.now()
        val recentDates = dailyRecordRepository.findRecentDatesByUserId(userId, today)

        if (recentDates.isEmpty()) return StreakResponse(0)

        var streak = 0
        var expectedDate = today

        if (recentDates.first() != today) {
            expectedDate = today.minusDays(1)
            if (recentDates.first() != expectedDate) {
                return StreakResponse(0)
            }
        }

        for (date in recentDates) {
            if (date == expectedDate) {
                streak++
                expectedDate = expectedDate.minusDays(1)
            } else {
                break
            }
        }

        return StreakResponse(streak)
    }
}
