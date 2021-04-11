package com.example.chatmemo.data.database.dao

import androidx.room.*
import com.example.chatmemo.data.database.entity.PhraseEntity

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
    fun delete(comment: PhraseEntity)

    @Query("delete from phrases")
    fun deleteAll()

    @Query("delete from phrases WHERE template_id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("SELECT * FROM phrases")
    fun getAll(): Array<PhraseEntity>

    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<PhraseEntity>

    @Query("delete from phrases WHERE template_id = :templateId")
    fun deleteById(templateId: Long)
}
