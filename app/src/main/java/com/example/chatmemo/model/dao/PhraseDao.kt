package com.example.chatmemo.model.dao

import androidx.room.*
import com.example.chatmemo.model.entity.PhraseEntity

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

    @Query("delete from comments")
    fun deleteAll()

    @Query("SELECT * FROM phrases")
    fun getAll(): Array<PhraseEntity>

    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<PhraseEntity>

    @Query("delete from phrases WHERE template_id = :templateId")
    fun deleteById(templateId: Long)
}
