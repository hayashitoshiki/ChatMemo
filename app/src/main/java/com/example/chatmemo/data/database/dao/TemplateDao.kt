package com.example.chatmemo.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.chatmemo.data.database.entity.TemplateEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface TemplateDao {

    @Query("SELECT MAX(id) FROM template")
    suspend fun getNextId(): Long?

    @Insert
    suspend fun insert(template: TemplateEntity)

    @Update
    suspend fun update(template: TemplateEntity)

    @Delete
    suspend fun delete(template: TemplateEntity)

    @Query("delete from template WHERE id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("SELECT * FROM template")
    fun getAll(): LiveData<List<TemplateEntity>>

    @Query("SELECT * FROM template WHERE id = :id")
    suspend fun getTemplateById(id: Long): TemplateEntity

    @Query("SELECT * FROM template WHERE title = :title")
    suspend fun getAllByTitle(title: String): List<TemplateEntity>
}