package com.myapp.chatmemo.data.local.database.dao

import androidx.room.*
import com.myapp.chatmemo.data.local.database.entity.TemplateMessageEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface PhraseDao {

    @Insert
    suspend fun insert(comment: TemplateMessageEntity)

    @Update
    suspend fun update(comment: TemplateMessageEntity)

    @Delete
    suspend fun delete(comment: TemplateMessageEntity)

    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("SELECT * FROM phrases")
    suspend fun getAll(): Array<TemplateMessageEntity>

    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<TemplateMessageEntity>

    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteById(templateId: Long)
}
