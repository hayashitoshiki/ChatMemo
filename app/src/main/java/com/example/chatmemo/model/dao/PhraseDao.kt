package com.example.chatmemo.model.dao

import androidx.room.*
import com.example.chatmemo.model.entity.Phrase

/**
 * 定型文用クエリ管理
 */
@Dao
interface PhraseDao {

    @Insert
    suspend fun insert(comment: Phrase)

    @Update
    suspend fun update(comment: Phrase)

    @Delete
    fun delete(comment: Phrase)

    @Query("delete from comments")
    fun deleteAll()

    @Query("SELECT * FROM phrases")
    fun getAll(): Array<Phrase>

    @Query("SELECT * FROM phrases WHERE template_id = :templateId")
    suspend fun getAllByTitle(templateId: Long): List<Phrase>

    @Query("delete from phrases WHERE template_id = :templateId")
    fun deleteById(templateId: Long)
}
