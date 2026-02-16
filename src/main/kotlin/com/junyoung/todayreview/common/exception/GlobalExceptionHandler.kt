package com.junyoung.todayreview.common.exception

import com.junyoung.todayreview.common.dto.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = LoggerFactory.getLogger("GlobalExceptionHandler")

class BusinessException(
    val status: HttpStatus,
    override val message: String,
) : RuntimeException(message)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusiness(e: BusinessException): ResponseEntity<ApiResponse<Unit>> =
        ResponseEntity.status(e.status).body(ApiResponse.error(e.message))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val message = e.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.badRequest().body(ApiResponse.error(message))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Unexpected error", e)
        return ResponseEntity.internalServerError()
            .body(ApiResponse.error("서버 오류가 발생했습니다."))
    }
}
