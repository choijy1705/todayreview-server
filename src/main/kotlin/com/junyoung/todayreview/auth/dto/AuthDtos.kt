package com.junyoung.todayreview.auth.dto

import jakarta.validation.constraints.NotBlank

data class KakaoLoginRequest(
    @field:NotBlank(message = "accessToken은 필수입니다.")
    val accessToken: String,
)

data class GoogleLoginRequest(
    @field:NotBlank(message = "idToken은 필수입니다.")
    val idToken: String,
)

data class RefreshRequest(
    @field:NotBlank(message = "refreshToken은 필수입니다.")
    val refreshToken: String,
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String?,
)
