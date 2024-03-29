package com.myapp.chatmemo.domain.repository

import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMessage
import kotlinx.coroutines.flow.Flow

/**
 * テンプレートに関するローカルデータソースCRUD用Repository
 */
interface LocalTemplateRepository {

    // region 定型文タイトル

    /**
     * 次のID連番の値を返す
     * @return テンプレートタイトルの次の連番
     */
    suspend fun getNextTemplateId(): TemplateId

    /**
     * 定型文登録
     * @param template 追加する定型文リスト
     * @return 正常登録できたか
     */
    suspend fun createTemplate(template: Template): Boolean

    /**
     * 定型文更新
     * @param template 追加する定型文リスト
     * @return 正常登録できたか
     */
    suspend fun updateTemplate(template: Template): Boolean

    /**
     * 指定の定型文削除
     * @param templateId 削除するテンプレートのID
     */
    suspend fun deleteTemplate(templateId: TemplateId): Boolean

    /**
     * 定型文のタイトル一覧取得
     * @return 定型文のタイトル一覧
     */
    fun getTemplateAll(): Flow<List<Template>>

    // endregion

    // region 定型文

    /**
     * タイトルに紐づいた定型文リスト取得
     * @param templateId テンプレートID
     */
    suspend fun getTemplateMessageById(templateId: TemplateId): List<TemplateMessage>

    // endregion
}
