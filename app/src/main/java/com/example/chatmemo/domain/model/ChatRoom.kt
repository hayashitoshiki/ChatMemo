package com.example.chatmemo.domain.model

import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.domain.value.TemplateMode
import java.io.Serializable

class ChatRoom(
    // 部屋のID
    val roomId: RoomId,
    // 部屋名
    var title: String,
    // テンプレート
    var template: Template?,
    // テンプレート表示モード
    var templateMode: TemplateMode?,
    // コメントリスト
    var commentList: MutableList<Comment>
) : Serializable