package com.junyoung.todayreview.auth.service

import com.junyoung.todayreview.auth.dto.TokenResponse
import com.junyoung.todayreview.auth.entity.RefreshToken
import com.junyoung.todayreview.auth.jwt.JwtProvider
import com.junyoung.todayreview.auth.repository.RefreshTokenRepository
import com.junyoung.todayreview.common.exception.BusinessException
import com.junyoung.todayreview.user.entity.SocialProvider
import com.junyoung.todayreview.user.entity.User
import com.junyoung.todayreview.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtProvider: JwtProvider,
    private val kakaoOAuthService: KakaoOAuthService,
    private val googleOAuthService: GoogleOAuthService,
) {

    @Transactional
    fun loginWithKakao(accessToken: String): TokenResponse {
        val kakaoUser = kakaoOAuthService.getUserInfo(accessToken)
        val user = findOrCreateUser(
            socialId = kakaoUser.socialId,
            provider = SocialProvider.KAKAO,
            nickname = kakaoUser.nickname,
            email = kakaoUser.email,
        )
        return issueTokens(user)
    }

    @Transactional
    fun loginWithGoogle(idToken: String): TokenResponse {
        val googleUser = googleOAuthService.getUserInfo(idToken)
        val user = findOrCreateUser(
            socialId = googleUser.socialId,
            provider = SocialProvider.GOOGLE,
            nickname = googleUser.nickname,
            email = googleUser.email,
        )
        return issueTokens(user)
    }

    @Transactional
    fun refresh(refreshToken: String): TokenResponse {
        if (!jwtProvider.validate(refreshToken)) {
            throw BusinessException(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다.")
        }

        val stored = refreshTokenRepository.findByToken(refreshToken)
            ?: throw BusinessException(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다.")

        if (stored.expiresAt.isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(stored)
            throw BusinessException(HttpStatus.UNAUTHORIZED, "만료된 리프레시 토큰입니다.")
        }

        val userId = jwtProvider.getUserId(refreshToken)
        val user = userRepository.findById(userId)
            .orElseThrow { BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.") }

        // Rotate refresh token
        refreshTokenRepository.delete(stored)
        return issueTokens(user)
    }

    @Transactional
    fun logout(userId: Long) {
        refreshTokenRepository.deleteByUserId(userId)
    }

    private fun findOrCreateUser(
        socialId: String,
        provider: SocialProvider,
        nickname: String?,
        email: String?,
    ): User {
        val existing = userRepository.findBySocialIdAndProvider(socialId, provider)
        if (existing != null) {
            existing.nickname = nickname ?: existing.nickname
            existing.email = email ?: existing.email
            return existing
        }
        return userRepository.save(
            User(
                socialId = socialId,
                provider = provider,
                nickname = nickname,
                email = email,
            )
        )
    }

    private fun issueTokens(user: User): TokenResponse {
        val accessToken = jwtProvider.createAccessToken(user.id)
        val refreshToken = jwtProvider.createRefreshToken(user.id)

        val expiresAt = LocalDateTime.now()
            .plusSeconds(jwtProvider.getRefreshTokenExpiry() / 1000)

        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id,
                token = refreshToken,
                expiresAt = expiresAt,
            )
        )

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            nickname = user.nickname,
        )
    }
}
