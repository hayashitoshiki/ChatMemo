package com.myapp.chatmemo.domain.model.entity

import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration
import java.io.Serializable

/**
 * チャットルーム定義
 */
data class ChatRoom(
    /**
     * チャットルームのID
     */
    val roomId: RoomId,
    /**
     * チャットルーム名
     */
    var title: String,
    /**
     * テンプレート使用設定
     */
    var templateConfiguration: TemplateConfiguration?,
    /**
     * 過去のコメントリスト
     */
    var commentList: MutableList<Comment>
) : Serializable
