package com.junyoung.todayreview.record.repository

import com.junyoung.todayreview.record.entity.DailyRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface DailyRecordRepository : JpaRepository<DailyRecord, Long> {

    fun findByUserIdAndDateOrderByCreatedAtDesc(userId: Long, date: LocalDate): List<DailyRecord>

    fun findByIdAndUserId(id: Long, userId: Long): DailyRecord?

    fun findByUserIdAndDateBetweenOrderByDateAscCreatedAtAsc(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<DailyRecord>

    @Query("SELECT DISTINCT r.date FROM DailyRecord r WHERE r.userId = :userId AND r.date BETWEEN :startDate AND :endDate ORDER BY r.date ASC")
    fun findDatesByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<LocalDate>

    @Query("SELECT DISTINCT r.date FROM DailyRecord r WHERE r.userId = :userId AND r.date <= :today ORDER BY r.date DESC")
    fun findRecentDatesByUserId(userId: Long, today: LocalDate): List<LocalDate>
}
