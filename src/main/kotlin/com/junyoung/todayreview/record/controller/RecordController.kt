package com.junyoung.todayreview.record.controller

import com.junyoung.todayreview.common.dto.ApiResponse
import com.junyoung.todayreview.record.dto.*
import com.junyoung.todayreview.record.service.RecordService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/api/records")
class RecordController(
    private val recordService: RecordService,
) {

    @GetMapping
    fun getMonthlyRecords(@RequestParam yearMonth: YearMonth): ApiResponse<List<RecordResponse>> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.getMonthlyRecords(userId, yearMonth))
    }

    @GetMapping("/{id}")
    fun getRecord(@PathVariable id: Long): ApiResponse<RecordResponse> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.getRecord(userId, id))
    }

    @PostMapping
    fun createRecord(@Valid @RequestBody request: CreateRecordRequest): ApiResponse<RecordResponse> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.createRecord(userId, request))
    }

    @PutMapping("/{id}")
    fun updateRecord(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateRecordRequest,
    ): ApiResponse<RecordResponse> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.updateRecord(userId, id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteRecord(@PathVariable id: Long): ApiResponse<Unit> {
        val userId = currentUserId()
        recordService.deleteRecord(userId, id)
        return ApiResponse.success()
    }

    @GetMapping("/dates")
    fun getRecordDates(@RequestParam yearMonth: YearMonth): ApiResponse<RecordDatesResponse> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.getRecordDates(userId, yearMonth))
    }

    @GetMapping("/streak")
    fun getStreak(): ApiResponse<StreakResponse> {
        val userId = currentUserId()
        return ApiResponse.success(recordService.getStreak(userId))
    }

    private fun currentUserId(): Long =
        SecurityContextHolder.getContext().authentication.principal as Long
}
