package com.example.chatmemo.model.repository

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage

class TemplateDataBaseRepositoryImp : TemplateDataBaseRepository {
    override suspend fun getNextTemplateId(): TemplateId {
        TODO("Not yet implemented")
    }

    override suspend fun createTemplate(template: Template): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateTemplate(template: Template): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTemplateAll(): LiveData<List<Template>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        TODO("Not yet implemented")
    }

}