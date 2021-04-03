package com.example.chatmemo.domain.usecase

import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage

interface TemplateUseCase {

    suspend fun deleteTemplate(template: Template): Boolean

    suspend fun getTemplateAll(): List<Template>

    suspend fun getPhraseByTemplateId(templateId: TemplateId): List<TemplateMessage>

    suspend fun createTemplate(template: Template, templateMessages: List<TemplateMessage>): Boolean

    suspend fun updateTemplate(template: Template, templateMessages: List<TemplateMessage>): Boolean

    fun getTemplateId(): TemplateId
}