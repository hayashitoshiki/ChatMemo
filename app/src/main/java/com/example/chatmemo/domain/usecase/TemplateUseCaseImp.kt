package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import com.example.chatmemo.model.repository.DataBaseRepository

class TemplateUseCaseImp(private val dataBaseRepository: DataBaseRepository) : TemplateUseCase {

    override suspend fun deleteTemplate(template: Template): Boolean {
        dataBaseRepository.deletePhraseByTitle(template.templateId)
        dataBaseRepository.deleteTemplateTitle(template)
        return true
    }

    override fun getTemplateAll(): LiveData<List<Template>> {
        return dataBaseRepository.getPhraseTitle()
    }

    override fun getSpinnerTemplateAll(): LiveData<List<Template>> {
        val templateList = MutableLiveData<List<Template>>()
        val templateLiveData = dataBaseRepository.getPhraseTitle()
        templateLiveData.observeForever {
            val list = arrayListOf(Template(TemplateId(0), "選択なし", listOf()))
            list.addAll(it)
            templateList.postValue(list)
        }
        return templateList
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