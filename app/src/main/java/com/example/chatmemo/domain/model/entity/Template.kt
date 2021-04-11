package com.example.chatmemo.domain.model.entity

import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage
import java.io.Serializable

class Template(
    // テンプレートID
    val templateId: TemplateId,
    // タイトル
    var title: String,
    // テンプレートメッセージリスト
    var templateMessageList: List<TemplateMessage>
) : Serializable