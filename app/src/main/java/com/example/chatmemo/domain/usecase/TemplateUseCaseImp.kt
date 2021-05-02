package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.data.repository.LocalChatRepository
import com.example.chatmemo.data.repository.LocalTemplateRepository
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage

class TemplateUseCaseImp(
    private val localChatRepository: LocalChatRepository,
    private val localTemplateRepository: LocalTemplateRepository
) : TemplateUseCase {

    override suspend fun createTemplate(template: Template): Boolean {
        return localTemplateRepository.createTemplate(template)
    }

    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        val roomList = localChatRepository.getRoomByTemplateId(templateId)
        return if (roomList.isNotEmpty()) {
            false
        } else {
            localTemplateRepository.deleteTemplate(templateId)
        }
    }

    override suspend fun updateTemplate(template: Template): Boolean {
        return localTemplateRepository.updateTemplate(template)
    }

    override fun getTemplateAll(): LiveData<List<Template>> {
        return localTemplateRepository.getTemplateAll()
    }

    override fun getSpinnerTemplateAll(): LiveData<List<Template>> {
        val templateList = MutableLiveData<List<Template>>()
        val templateLiveData = localTemplateRepository.getTemplateAll()
        templateLiveData.observeForever {
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            list.addAll(it)
            templateList.postValue(list)
        }
        return templateList
    }

    override suspend fun getNextTemplateId(): TemplateId {
        return localTemplateRepository.getNextTemplateId()
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return localTemplateRepository.getTemplateMessageById(templateId)
    }
}
