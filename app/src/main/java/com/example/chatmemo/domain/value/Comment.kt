package com.example.chatmemo.domain.value

import java.util.*

data class Comment(
    // コメント
    val message: String,
    // ユーザ
    val user: User,
    // コメントした時間
    val time: Date
)