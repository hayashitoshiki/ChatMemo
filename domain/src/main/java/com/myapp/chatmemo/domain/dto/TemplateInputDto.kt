package com.myapp.chatmemo.domain.dto

import com.myapp.chatmemo.domain.model.value.TemplateMessage
import java.io.Serializable

/**
 * 未モデル状態のテンプレートのInputDto
 *
 * @property templateTitle テンプレートタイトル
 * @property templateMessageList テンプレートメッセージリスト
 */
data class TemplateInputDto(
    val templateTitle: String,
    val templateMessageList: List<TemplateMessage>
) : Serializable
