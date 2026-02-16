package com.junyoung.todayreview.record.entity

enum class Category(val displayName: String, val emoji: String) {
    EXERCISE("운동", "\uD83D\uDCAA"),
    STUDY("공부", "\uD83D\uDCDA"),
    WORK("업무", "\uD83D\uDCBC"),
    HOBBY("취미", "\uD83C\uDFA8"),
    ETC("기타", "\u2728"),
}
