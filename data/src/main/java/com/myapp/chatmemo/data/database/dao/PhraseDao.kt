package com.myapp.chatmemo.data.database.dao

import androidx.room.*
import com.myapp.chatmemo.data.database.entity.TemplateMessageEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface PhraseDao {

    /**
     * 定型文追加
     *
     * @param templateMessage 追加する定型文
     */
    @Insert
    suspend fun insert(templateMessage: TemplateMessageEntity)

    /**
     * 定型文更新
     *
     * @param templateMessage 更新する定型文
     */
    @Update
    suspend fun update(templateMessage: TemplateMessageEntity)

    /**
     * 定型文削除
     *
     * @param templateMessage 削除する定型文
     */
    @Delete
    suspend fun delete(templateMessage: TemplateMessageEntity)

    /**
     * 定型文リストIDに紐づく定型文削除
     *
     * @param templateId 削除する定型文ID
     */
    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    /**
     * 全ての定型文取得
     *
     * @return
     */
    @Query("SELECT * FROM phrases")
    suspend fun getAll(): Array<TemplateMessageEntity>

    /**
     * 定型文リストIDに紐づく定型文リスト取得
     *
     * @param templateId 定型文リストID
     * @return 定型文リストIDに紐づく定型文リスト
     */
    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<TemplateMessageEntity>

    /**
     * 定型文リストIDに紐づく定型文削除
     *
     * @param templateId 削除する定型文リストID
     */
    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteById(templateId: Long)
}
