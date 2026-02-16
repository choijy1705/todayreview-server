package com.junyoung.todayreview.auth.service

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.junyoung.todayreview.common.exception.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

data class GoogleUserInfo(
    val socialId: String,
    val nickname: String?,
    val email: String?,
)

@Service
class GoogleOAuthService(
    @Value("\${oauth.google.client-id}") private val clientId: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val verifier = GoogleIdTokenVerifier.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance()
    )
        .setAudience(listOf(clientId))
        .build()

    fun getUserInfo(idToken: String): GoogleUserInfo {
        val googleIdToken = try {
            verifier.verify(idToken)
        } catch (e: Exception) {
            log.error("Failed to verify Google ID token", e)
            throw BusinessException(HttpStatus.UNAUTHORIZED, "구글 인증에 실패했습니다.")
        } ?: throw BusinessException(HttpStatus.UNAUTHORIZED, "유효하지 않은 구글 토큰입니다.")

        val payload = googleIdToken.payload

        return GoogleUserInfo(
            socialId = payload.subject,
            nickname = payload["name"]?.toString(),
            email = payload.email,
        )
    }
}
