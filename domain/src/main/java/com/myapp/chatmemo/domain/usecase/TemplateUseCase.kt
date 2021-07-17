package com.myapp.chatmemo.domain.usecase

import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import kotlinx.coroutines.flow.Flow

/**
 * テンプレートに関するビジネスロジック
 */
interface TemplateUseCase {

    /**
     * テンプレート作成
     * @param template 作成するテンプレート
     */
    suspend fun createTemplate(template: Template): Boolean

    /**
     * テンプレート削除
     * @param templateId 削除するテンプレートID
     */
    suspend fun deleteTemplate(templateId: TemplateId): Boolean

    /**
     * テンプレートアプデート
     * @param template 更新するテンプレート
     */
    suspend fun updateTemplate(template: Template): Boolean

    /**
     * 全てのテンプレート取得
     * @return 全てのテンプレート
     */
    fun getTemplateAll(): Flow<List<Template>>

    /**
     * 全てのテンプレート取得（選択肢スピナーに表示させる用）
     * @return 全てのテンプレート
     */
    fun getSpinnerTemplateAll(): Flow<List<Template>>

    /**
     * テンプレートIDに紐づくテンプレートコメント取得
     * @param templateId 取得するテンプレートのID
     */
    suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage>

    /**
     * 次の連番のテンプレートID取得
     * @return 次の登録する際のテンプレートID
     */
    suspend fun getNextTemplateId(): TemplateId
}
