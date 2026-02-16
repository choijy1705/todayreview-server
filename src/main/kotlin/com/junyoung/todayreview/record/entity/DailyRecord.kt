package com.junyoung.todayreview.record.entity

import com.junyoung.todayreview.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "daily_records")
class DailyRecord(
    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val date: LocalDate,

    @Column(nullable = false, length = 100)
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var category: Category,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
) : BaseTimeEntity() {

    fun update(content: String, category: Category) {
        this.content = content
        this.category = category
    }
}
