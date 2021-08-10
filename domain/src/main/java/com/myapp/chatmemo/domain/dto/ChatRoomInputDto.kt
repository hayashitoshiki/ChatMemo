package com.myapp.chatmemo.domain.dto

import java.io.Serializable


/**
 * 未モデル状態のチャットルームのInputDto
 *
 * @property chatRoomTitle チャットルーム名
 * @property templateConfiguretionInputDto 使用するテンプレート
 */
data class ChatRoomInputDto(
    val chatRoomTitle: String,
    val templateConfiguretionInputDto: TemplateConfiguretionInputDto?
) : Serializable
