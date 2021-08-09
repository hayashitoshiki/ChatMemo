package com.myapp.chatmemo.domain.model.entity

import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import java.io.Serializable

/**
 * テンプレート定義
 *
 * @property templateId テンプレートID
 * @property title テンプレートタイトル
 * @property templateMessageList テンプレートメッセージリスト
 */
data class Template(
    val templateId: TemplateId,
    var title: String,
    var templateMessageList: List<TemplateMessage>
) : Serializable
