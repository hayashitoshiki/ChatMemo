package com.example.chatmemo.model.dao

import androidx.room.*
import com.example.chatmemo.model.entity.Template

/**
 * 定型文用クエリ管理
 */
@Dao
interface TemplateDao {

    @Insert
    suspend fun insert(template: Template)

    @Update
    suspend fun update(template: Template)

    @Delete
    suspend fun delete(template: Template)

    @Query("SELECT * FROM template")
    fun getAll(): List<Template>

    @Query("SELECT * FROM template WHERE id = :id")
    suspend fun getTemplateById(id: Long): Template

    @Query("SELECT * FROM template WHERE title = :title")
    suspend fun getAllByTitle(title: String): List<Template>
}
