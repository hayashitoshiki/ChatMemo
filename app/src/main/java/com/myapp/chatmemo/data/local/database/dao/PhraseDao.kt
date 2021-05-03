package com.myapp.chatmemo.data.local.database.dao

import androidx.room.*
import com.myapp.chatmemo.data.local.database.entity.PhraseEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface PhraseDao {

    @Insert
    suspend fun insert(comment: PhraseEntity)

    @Update
    suspend fun update(comment: PhraseEntity)

    @Delete
    suspend fun delete(comment: PhraseEntity)

    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("SELECT * FROM phrases")
    suspend fun getAll(): Array<PhraseEntity>

    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<PhraseEntity>

    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteById(templateId: Long)
}
