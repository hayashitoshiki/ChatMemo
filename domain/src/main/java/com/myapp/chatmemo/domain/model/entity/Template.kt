package com.myapp.chatmemo.domain.model.entity

import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import java.io.Serializable

data class Template(
    // テンプレートID
    val templateId: TemplateId,
    // タイトル
    var title: String,
    // テンプレートメッセージリスト
    var templateMessageList: List<TemplateMessage>
) : Serializable
