package com.junyoung.todayreview.auth.service

import com.junyoung.todayreview.common.exception.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

data class KakaoUserInfo(
    val socialId: String,
    val nickname: String?,
    val email: String?,
)

@Service
class KakaoOAuthService(
    @Value("\${oauth.kakao.user-info-url}") private val userInfoUrl: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val restTemplate = RestTemplate()

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        val headers = HttpHeaders().apply {
            setBearerAuth(accessToken)
        }
        val request = HttpEntity<Void>(headers)

        val response = try {
            restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, Map::class.java)
        } catch (e: Exception) {
            log.error("Failed to get Kakao user info", e)
            throw BusinessException(HttpStatus.UNAUTHORIZED, "카카오 인증에 실패했습니다.")
        }

        val body = response.body
            ?: throw BusinessException(HttpStatus.UNAUTHORIZED, "카카오 사용자 정보를 가져올 수 없습니다.")

        val id = body["id"]?.toString()
            ?: throw BusinessException(HttpStatus.UNAUTHORIZED, "카카오 사용자 ID를 가져올 수 없습니다.")

        @Suppress("UNCHECKED_CAST")
        val kakaoAccount = body["kakao_account"] as? Map<String, Any>

        @Suppress("UNCHECKED_CAST")
        val profile = kakaoAccount?.get("profile") as? Map<String, Any>

        return KakaoUserInfo(
            socialId = id,
            nickname = profile?.get("nickname")?.toString(),
            email = kakaoAccount?.get("email")?.toString(),
        )
    }
}
