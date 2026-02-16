package com.junyoung.todayreview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodayReviewServerApplication

fun main(args: Array<String>) {
    runApplication<TodayReviewServerApplication>(*args)
}
