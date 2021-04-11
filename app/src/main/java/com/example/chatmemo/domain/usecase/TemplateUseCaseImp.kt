package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.data.repository.ChatDataBaseRepository
import com.example.chatmemo.data.repository.TemplateDataBaseRepository
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage

class TemplateUseCaseImp(
    private val chatDataBaseRepository: ChatDataBaseRepository,
    private val templateDataBaseRepository: TemplateDataBaseRepository
) : TemplateUseCase {

    override suspend fun createTemplate(template: Template): Boolean {
        return templateDataBaseRepository.createTemplate(template)
    }

    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        val roomList = chatDataBaseRepository.getRoomByTemplateId(templateId)
        return if (roomList.isNotEmpty()) {
            false
        } else {
            templateDataBaseRepository.deleteTemplate(templateId)
        }
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