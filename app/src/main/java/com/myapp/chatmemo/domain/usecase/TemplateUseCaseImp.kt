package com.myapp.chatmemo.domain.usecase

import com.myapp.chatmemo.data.repository.LocalChatRepository
import com.myapp.chatmemo.data.repository.LocalTemplateRepository
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class TemplateUseCaseImp(
    private val localChatRepository: LocalChatRepository, private val localTemplateRepository: LocalTemplateRepository
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

    override fun getTemplateAll(): Flow<List<Template>> {
        return localTemplateRepository.getTemplateAll()
    }

    override fun getSpinnerTemplateAll(): Flow<List<Template>> {
        return flow<List<Template>> {
            localTemplateRepository.getTemplateAll().collect {
                val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
                list.addAll(it)
                emit(list)
            }
        }
    }

    override suspend fun getNextTemplateId(): TemplateId {
        return localTemplateRepository.getNextTemplateId()
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return localTemplateRepository.getTemplateMessageById(templateId)
    }
}
