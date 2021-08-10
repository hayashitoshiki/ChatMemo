package com.myapp.chatmemo.domain.model.value

import java.io.Serializable

/**
 * チャットのコメント定義
 *
 * @property message コメント
 * @property user コメントしたユーザ
 * @property time コメントした時間
 */
data class Comment(
    val message: String,
    val user: User,
    val time: CommentDateTime
) : Serializable
