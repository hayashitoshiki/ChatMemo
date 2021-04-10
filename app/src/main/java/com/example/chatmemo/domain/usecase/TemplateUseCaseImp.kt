package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import com.example.chatmemo.model.repository.TemplateDataBaseRepository

class TemplateUseCaseImp(private val templateDataBaseRepository: TemplateDataBaseRepository) : TemplateUseCase {

    override suspend fun createTemplate(template: Template): Boolean {
        return templateDataBaseRepository.createTemplate(template)
    }

    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        return templateDataBaseRepository.deleteTemplate(templateId)
    }

    override suspend fun updateTemplate(template: Template): Boolean {
        return templateDataBaseRepository.updateTemplate(template)
    }

    override fun getTemplateAll(): LiveData<List<Template>> {
        return templateDataBaseRepository.getTemplateAll()
    }

    override fun getSpinnerTemplateAll(): LiveData<List<Template>> {
        val templateList = MutableLiveData<List<Template>>()
        val templateLiveData = templateDataBaseRepository.getTemplateAll()
        templateLiveData.observeForever {
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            list.addAll(it)
            templateList.postValue(list)
        }
        return templateList
    }

    override suspend fun getNextTemplateId(): TemplateId {
        return templateDataBaseRepository.getNextTemplateId()
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return templateDataBaseRepository.getTemplateMessageById(templateId)
    }
}