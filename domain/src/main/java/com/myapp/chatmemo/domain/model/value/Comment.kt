package com.myapp.chatmemo.domain.model.value

import java.io.Serializable

/**
 * チャットのコメント定義
 */
data class Comment(
    /**
     * コメント
     */
    val message: String,
    /**
     * コメントしたユーザ
     */
    val user: User,
    /**
     * コメントした時間
     */
    val time: CommentDateTime
) : Serializable
