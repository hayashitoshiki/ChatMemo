package com.example.chatmemo.domain.model.entity

import com.example.chatmemo.domain.model.value.Comment
import com.example.chatmemo.domain.model.value.RoomId
import com.example.chatmemo.domain.model.value.TemplateConfiguration
import java.io.Serializable

data class ChatRoom(
    // 部屋のID
    val roomId: RoomId,
    // 部屋名
    var title: String,
    // テンプレート
    var templateConfiguration: TemplateConfiguration?,
    // コメントリスト
    var commentList: MutableList<Comment>
) : Serializable
