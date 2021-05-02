package com.example.chatmemo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.data.local.database.entity.PhraseEntity
import com.example.chatmemo.data.local.database.entity.TemplateEntity
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage
import com.example.chatmemo.ui.MyApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalTemplateRepositoryImp(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    LocalTemplateRepository {

    private val templateDao = MyApplication.database.templateDao()
    private val phraseDao = MyApplication.database.phraseDao()

    override suspend fun getNextTemplateId(): TemplateId {
        return withContext(ioDispatcher) {
            val id = templateDao.getNextId() ?: 0
            return@withContext TemplateId(id.toInt() + 1)
        }
    }

    override suspend fun createTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateId = template.templateId.value.toLong()
            val templateTitle = template.title
            val templateEntity = TemplateEntity(null, templateTitle)
            templateDao.insert(templateEntity)
            template.templateMessageList.forEach {
                val phraseTitle = it.massage
                val phrase = PhraseEntity(null, phraseTitle, templateId)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    override suspend fun updateTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateId = template.templateId.value.toLong()
            val templateTitle = template.title
            val templateEntity = TemplateEntity(templateId, templateTitle)
            templateDao.update(templateEntity)
            phraseDao.deleteByTemplateId(templateId)
            template.templateMessageList.forEach {
                val phraseTitle = it.massage
                val phrase = PhraseEntity(null, phraseTitle, templateId)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        return withContext(ioDispatcher) {
            val temId = templateId.value.toLong()
            templateDao.deleteByTemplateId(temId)
            phraseDao.deleteByTemplateId(temId)

            return@withContext true
        }
    }

    override fun getTemplateAll(): LiveData<List<Template>> {
        val templateListLiveData = templateDao.getAll()
        val templateList = MutableLiveData<List<Template>>()
        templateListLiveData.observeForever {
            val list = it.map { templateEntity ->
                Template(TemplateId(templateEntity.id!!.toInt()), templateEntity.title, listOf())
            }
            templateList.postValue(list)
        }
        return templateList
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return withContext(ioDispatcher) {
            val id = templateId.value.toLong()
            return@withContext phraseDao.getAllByTitle(id).map {
                val message = it.text
                TemplateMessage(message)
            }
        }
    }
}
