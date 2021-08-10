package com.myapp.chatmemo.domain.translator

import com.myapp.chatmemo.domain.dto.ChatRoomInputDto
import com.myapp.chatmemo.domain.dto.TemplateConfiguretionInputDto
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.TemplateConfiguration

/**
 * チャットルーム関連のDto→モデル変換Translator
 */
object ChatRoomTranslator {

    /**
     * チャットルーム生成
     *
     * @param roomId チャットルームID
     * @param chatRoomInputDto チャットルームを生成するためのDto
     * @return チャットルームモデル
     */
    fun createChatRoomConvert(
        roomId: RoomId,
        chatRoomInputDto: ChatRoomInputDto
    ): ChatRoom {
        val templateConfiguration = chatRoomInputDto.templateConfiguretionInputDto?.let { templateDto ->
            templateConfigurationConvert(templateDto)
        }
        return ChatRoom(roomId, chatRoomInputDto.chatRoomTitle, templateConfiguration, mutableListOf())
    }

    /**
     * テンプレート詳細設定生成
     *
     * @param templateConfiguretionDto テンプレート詳細設定InputData
     * @return テンプレート詳細設定モデル
     */
    private fun templateConfigurationConvert(templateConfiguretionDto: TemplateConfiguretionInputDto): TemplateConfiguration {
        val template = Template(
            templateConfiguretionDto.templateId, templateConfiguretionDto.templateTitle,
            templateConfiguretionDto.templateMessageList
        )
        return TemplateConfiguration(template, templateConfiguretionDto.templateMode)
    }
}
