package com.junyoung.todayreview.user.repository

import com.junyoung.todayreview.user.entity.SocialProvider
import com.junyoung.todayreview.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findBySocialIdAndProvider(socialId: String, provider: SocialProvider): User?
}
