package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.local.database.dao.PhraseDao
import com.myapp.chatmemo.data.local.database.dao.TemplateDao
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalTemplateRepositoryImp(
    private val templateDao: TemplateDao,
    private val phraseDao: PhraseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalTemplateRepository {

    // テンプレートテーブルの次の連番取得
    override suspend fun getNextTemplateId(): TemplateId {
        return withContext(ioDispatcher) {
            val id = templateDao.getNextId() ?: 0
            return@withContext TemplateId(id.toInt() + 1)
        }
    }

    // テンプレート登録
    override suspend fun createTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateEntity = Converter.templateEntityFromTemplate(template)
            templateDao.insert(templateEntity)
            template.templateMessageList.forEach {
                val phrase = Converter.praseEntityFromTemplateAndMessage(template, it)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    // テンプレート更新
    override suspend fun updateTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateId = template.templateId.value.toLong()
            val templateEntity = Converter.templateEntityFromTemplate(template)
            templateDao.update(templateEntity)
            phraseDao.deleteByTemplateId(templateId)
            template.templateMessageList.forEach {
                val phrase = Converter.praseEntityFromTemplateAndMessage(template, it)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    // テンプレート削除
    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        return withContext(ioDispatcher) {
            val temId = templateId.value.toLong()
            templateDao.deleteByTemplateId(temId)
            phraseDao.deleteByTemplateId(temId)

            return@withContext true
        }
    }

    override fun getTemplateAll(): Flow<List<Template>> {
        return templateDao.getAll().map { templateEntityList ->
            templateEntityList.map { templateEntity ->
                Template(TemplateId(templateEntity.id!!.toInt()), templateEntity.title, listOf())
            }
        }
    }

    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return withContext(ioDispatcher) {
            val id = templateId.value.toLong()
            return@withContext phraseDao.getAllByTitle(id).map {
                Converter.templateMessageFromPharaseEntity(it)
            }
        }
    }
}
