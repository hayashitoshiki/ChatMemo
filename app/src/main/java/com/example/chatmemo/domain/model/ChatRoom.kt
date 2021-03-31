package com.example.chatmemo.domain.model

import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.domain.value.TemplateMode

class ChatRoom(
    // 部屋のID
    val roomId: RoomId,
    // 部屋名
    val title: String,
    // テンプレート
    val template: Template?,
    // テンプレート表示モード
    val templateMode: TemplateMode,
    // コメントリスト
    val commentList: List<Comment>
)