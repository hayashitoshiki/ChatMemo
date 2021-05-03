package com.myapp.chatmemo.domain.model.value

import java.io.Serializable

data class Comment(
    // コメント
    val message: String,
    // ユーザ
    val user: User,
    // コメントした時間
    val time: CommentDateTime
) : Serializable
