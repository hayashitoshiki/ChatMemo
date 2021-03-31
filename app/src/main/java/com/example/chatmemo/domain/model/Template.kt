package com.example.chatmemo.domain.model

import com.example.chatmemo.domain.value.TempalateMessage
import com.example.chatmemo.domain.value.TemplateId

class Template(
    // テンプレートID
    val templateId: TemplateId,
    // タイトル
    val title: String,
    // テンプレートメッセージリスト
    val templateMessageList: List<TempalateMessage>
)