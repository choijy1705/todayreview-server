package com.junyoung.todayreview.auth.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.access-token-expiry}") private val accessTokenExpiry: Long,
    @Value("\${jwt.refresh-token-expiry}") private val refreshTokenExpiry: Long,
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun createAccessToken(userId: Long): String =
        createToken(userId, accessTokenExpiry)

    fun createRefreshToken(userId: Long): String =
        createToken(userId, refreshTokenExpiry)

    fun getRefreshTokenExpiry(): Long = refreshTokenExpiry

    fun getUserId(token: String): Long =
        parseClaims(token).subject.toLong()

    fun validate(token: String): Boolean =
        try {
            parseClaims(token)
            true
        } catch (e: Exception) {
            false
        }

    private fun createToken(userId: Long, expiryMs: Long): String {
        val now = Date()
        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(now)
            .expiration(Date(now.time + expiryMs))
            .signWith(key)
            .compact()
    }

    private fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}
