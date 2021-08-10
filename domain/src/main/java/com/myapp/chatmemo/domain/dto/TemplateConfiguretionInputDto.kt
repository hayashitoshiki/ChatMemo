package com.myapp.chatmemo.domain.dto

import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import com.myapp.chatmemo.domain.model.value.TemplateMode
import java.io.Serializable

/**
 * 未モデル状態のテンプレート詳細設定のInputDto
 *
 * @property templateId テンプレートID
 * @property templateTitle テンプレートタイトル
 * @property templateMessageList テンプレートメッセージリスト
 * @property templateMode テンプレートモード
 */
data class TemplateConfiguretionInputDto(
    val templateId: TemplateId,
    val templateTitle: String,
    val templateMessageList: List<TemplateMessage>,
    val templateMode: TemplateMode
) : Serializable
