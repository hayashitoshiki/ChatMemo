package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage

/**
 * テンプレートに関するビジネスロジック
 */
interface TemplateUseCase {

    /**
     * テンプレート作成
     */
    suspend fun createTemplate(template: Template): Boolean

    /**
     * テンプレート削除
     */
    suspend fun deleteTemplate(templateId: TemplateId): Boolean

    /**
     * テンプレートアプデート
     */
    suspend fun updateTemplate(template: Template): Boolean

    /**
     * 全てのテンプレート取得
     */
    fun getTemplateAll(): LiveData<List<Template>>

    /**
     * 全てのテンプレート取得（選択肢スピナーに表示させる用）
     */
    fun getSpinnerTemplateAll(): LiveData<List<Template>>

    /**
     * テンプレートIDに紐づくテンプレートコメント取得
     */
    suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage>

    /**
     * 次の連番のテンプレートID取得
     */
    suspend fun getNextTemplateId(): TemplateId
}
