package com.junyoung.todayreview.auth.controller

import com.junyoung.todayreview.auth.dto.GoogleLoginRequest
import com.junyoung.todayreview.auth.dto.KakaoLoginRequest
import com.junyoung.todayreview.auth.dto.RefreshRequest
import com.junyoung.todayreview.auth.dto.TokenResponse
import com.junyoung.todayreview.auth.service.AuthService
import com.junyoung.todayreview.common.dto.ApiResponse
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/kakao")
    fun loginWithKakao(@Valid @RequestBody request: KakaoLoginRequest): ApiResponse<TokenResponse> {
        val token = authService.loginWithKakao(request.accessToken)
        return ApiResponse.success(token)
    }

    @PostMapping("/google")
    fun loginWithGoogle(@Valid @RequestBody request: GoogleLoginRequest): ApiResponse<TokenResponse> {
        val token = authService.loginWithGoogle(request.idToken)
        return ApiResponse.success(token)
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshRequest): ApiResponse<TokenResponse> {
        val token = authService.refresh(request.refreshToken)
        return ApiResponse.success(token)
    }

    @PostMapping("/logout")
    fun logout(): ApiResponse<Unit> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        authService.logout(userId)
        return ApiResponse.success()
    }
}
