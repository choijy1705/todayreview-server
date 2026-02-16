package com.junyoung.todayreview.common.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(success = true, data = data)

        fun success(): ApiResponse<Unit> =
            ApiResponse(success = true)

        fun <T> error(message: String): ApiResponse<T> =
            ApiResponse(success = false, error = message)
    }
}
