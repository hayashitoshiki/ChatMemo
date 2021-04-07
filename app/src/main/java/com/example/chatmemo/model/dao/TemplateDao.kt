package com.example.chatmemo.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.chatmemo.model.entity.TemplateEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface TemplateDao {

    @Insert
    suspend fun insert(template: TemplateEntity)

    @Update
    suspend fun update(template: TemplateEntity)

    @Delete
    suspend fun delete(template: TemplateEntity)

    @Query("SELECT * FROM template")
    fun getAll(): LiveData<List<TemplateEntity>>

    @Query("SELECT * FROM template WHERE id = :id")
    suspend fun getTemplateById(id: Long): TemplateEntity

    @Query("SELECT * FROM template WHERE title = :title")
    suspend fun getAllByTitle(title: String): List<TemplateEntity>
}
