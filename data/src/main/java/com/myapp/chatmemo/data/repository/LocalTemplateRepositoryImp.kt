package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import com.myapp.chatmemo.domain.repository.LocalTemplateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalTemplateRepositoryImp(
    private val templateDao: com.myapp.chatmemo.data.database.dao.TemplateDao,
    private val phraseDao: com.myapp.chatmemo.data.database.dao.PhraseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalTemplateRepository {

    // テンプレートテーブルの次の連番取得
    override suspend fun getNextTemplateId(): TemplateId {
        return withContext(ioDispatcher) {
            val id = templateDao.getNextId() ?: 0
            return@withContext TemplateId(id + 1)
        }
    }

    // テンプレート登録
    override suspend fun createTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateEntity = Converter.templateEntityFromTemplate(template)
            templateDao.insert(templateEntity)
            template.templateMessageList.forEach {
                val templateMessageEntity = Converter.templateMessageEntityFromTemplateAndMessage(template, it)
                phraseDao.insert(templateMessageEntity)
            }
            return@withContext true
        }
    }

    // テンプレート更新
    override suspend fun updateTemplate(template: Template): Boolean {
        return withContext(ioDispatcher) {
            val templateId = template.templateId.value
            val oldTemplateEntity = templateDao.getTemplateById(templateId)
            val newTemplateEntity = Converter.templateEntityFromTemplate(template)
            oldTemplateEntity.update(newTemplateEntity)
            templateDao.update(oldTemplateEntity)
            phraseDao.deleteByTemplateId(templateId)
            template.templateMessageList.forEach {
                val templateMessageEntity = Converter.templateMessageEntityFromTemplateAndMessage(template, it)
                phraseDao.insert(templateMessageEntity)
            }
            return@withContext true
        }
    }

    // テンプレート削除
    override suspend fun deleteTemplate(templateId: TemplateId): Boolean {
        return withContext(ioDispatcher) {
            val temId = templateId.value
            templateDao.deleteByTemplateId(temId)
            phraseDao.deleteByTemplateId(temId)

            return@withContext true
        }
    }

    // 全テンプレート取得
    override fun getTemplateAll(): Flow<List<Template>> {
        return templateDao.getAll()
            .map { templateEntityList ->
                templateEntityList.sortedByDescending { it.updateAt }
                    .filter { it.id != null }
                    .map { templateEntity ->
                        Template(TemplateId(templateEntity.id!!), templateEntity.title, listOf())
                    }
            }
    }

    // テンプレートIDに紐づくテンプレートメッセージ取得
    override suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage> {
        return withContext(ioDispatcher) {
            val id = templateId.value
            return@withContext phraseDao.getAllByTitle(id)
                .map {
                    Converter.templateMessageFromPharaseEntity(it)
                }
        }
    }
}
