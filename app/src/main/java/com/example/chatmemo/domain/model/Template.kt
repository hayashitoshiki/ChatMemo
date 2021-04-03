package com.example.chatmemo.domain.model

import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import java.io.Serializable

class Template(
    // テンプレートID
    val templateId: TemplateId,
    // タイトル
    var title: String,
    // テンプレートメッセージリスト
    var templateMessageList: List<TemplateMessage>
) : Serializable