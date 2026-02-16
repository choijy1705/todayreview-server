package com.junyoung.todayreview.user.entity

import com.junyoung.todayreview.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["social_id", "provider"])]
)
class User(
    @Column(name = "social_id", nullable = false, length = 255)
    val socialId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val provider: SocialProvider,

    @Column(length = 100)
    var nickname: String? = null,

    @Column(length = 255)
    var email: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) : BaseTimeEntity()
