package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage

interface TemplateUseCase {

    suspend fun deleteTemplate(template: Template): Boolean

    fun getTemplateAll(): LiveData<List<Template>>

    fun getSpinnerTemplateAll(): LiveData<List<Template>>

    suspend fun getPhraseByTemplateId(templateId: TemplateId): List<TemplateMessage>

    suspend fun createTemplate(template: Template): Boolean

    suspend fun updateTemplate(template: Template): Boolean

    suspend fun getNextTemplateId(): TemplateId
}