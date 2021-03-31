package com.example.chatmemo.domain.value

data class Comment(
    // コメント
    val message: String,
    // ユーザ
    val user: User
)