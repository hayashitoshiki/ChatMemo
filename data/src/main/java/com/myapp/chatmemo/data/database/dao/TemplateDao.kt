package com.myapp.chatmemo.data.database.dao

import androidx.room.*
import com.myapp.chatmemo.data.database.entity.TemplateTitleEntity
import kotlinx.coroutines.flow.Flow

/**
 * 定型文タイトル用クエリ管理
 */
@Dao
interface TemplateDao {

    /**
     * TODO
     *
     * @return
     */
    @Query("SELECT MAX(id) FROM template")
    suspend fun getNextId(): Long?

    /**
     * 定型文タイトル登録
     *
     * @param templateTitle 登録する定型文タイトル
     */
    @Insert
    suspend fun insert(templateTitle: TemplateTitleEntity)

    /**
     * 定型文タイトル更新
     *
     * @param templateTitle 更新する定型文タイトル
     */
    @Update
    suspend fun update(templateTitle: TemplateTitleEntity)

    /**
     * 定型文タイトル削除
     *
     * @param templateTitle 削除する定型文タイトル
     */
    @Delete
    suspend fun delete(templateTitle: TemplateTitleEntity)

    /**
     * 定型文リストIDに紐づく定型文タイトル削除
     *
     * @param templateId 削除する定型文リストID
     */
    @Query("delete from template WHERE id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    /**
     * 全定型文タイトル取得
     *
     * @return 全ての定型文タイトルリスト
     */
    @Query("SELECT * FROM template")
    fun getAll(): Flow<List<TemplateTitleEntity>>

    /**
     * 定型文リストIDに紐づく定型文タイトル取得
     *
     * @param id 取得する定型文リストID
     * @return 定型文リストIDに紐づく定型文タイトル
     */
    @Query("SELECT * FROM template WHERE id = :id")
    suspend fun getTemplateById(id: Long): TemplateTitleEntity
}
