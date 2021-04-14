package com.example.chatmemo.domain.model.value

data class Comment(
    // コメント
    val message: String,
    // ユーザ
    val user: User,
    // コメントした時間
    val time: CommentDateTime
)
