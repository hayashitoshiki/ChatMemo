package com.example.chatmemo.domain.usecase

import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import com.example.chatmemo.model.repository.DataBaseRepository

class TemplateUseCaseImp(private val dataBaseRepository: DataBaseRepository) : TemplateUseCase {
    override suspend fun deleteTemplate(template: Template): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getTemplateAll(): List<Template> {
        return dataBaseRepository.getPhraseTitle()
    }

    override suspend fun getPhraseByTemplateId(templateId: TemplateId): List<TemplateMessage> {
        TODO("Not yet implemented")
    }

    override suspend fun createTemplate(
        template: Template, templateMessages: List<TemplateMessage>
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateTemplate(
        template: Template, templateMessages: List<TemplateMessage>
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTemplateId(): TemplateId {
        TODO("Not yet implemented")
    }
}