package com.myapp.chatmemo.domain.translator

import com.myapp.chatmemo.domain.dto.TemplateInputDto
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId

/**
 * テンプレート関連のDto→モデル変換Translator
 */
object TemplateTranslator {

    /**
     * テンプレート生成
     *
     * @param templateId テンプレートID
     * @param templateInputDto　テンプレートモデルを生成するためのDto
     * @return テンプレートモデル
     */
    fun createTemplateConvert(
        templateId: TemplateId,
        templateInputDto: TemplateInputDto
    ): Template {
        return Template(templateId, templateInputDto.templateTitle, templateInputDto.templateMessageList)
    }
}
