package com.myapp.chatmemo.domain.model.entity

import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration
import java.io.Serializable


/**
 * チャットルーム定義
 *
 * @property roomId チャットルームのID
 * @property title チャットルーム名
 * @property templateConfiguration テンプレート使用設定
 * @property commentList 過去のコメントリスト
 */
data class ChatRoom(
    val roomId: RoomId,
    var title: String,
    var templateConfiguration: TemplateConfiguration?,
    var commentList: MutableList<Comment>
) : Serializable
