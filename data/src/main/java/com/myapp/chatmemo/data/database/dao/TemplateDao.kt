package com.myapp.chatmemo.data.database.dao

import androidx.room.*
import com.myapp.chatmemo.data.database.entity.TemplateTitleEntity
import kotlinx.coroutines.flow.Flow

/**
 * 定型文用クエリ管理
 */
@Dao
interface TemplateDao {

    @Query("SELECT MAX(id) FROM template")
    suspend fun getNextId(): Long?

    @Insert
    suspend fun insert(templateTitle: TemplateTitleEntity)

    @Update
    suspend fun update(templateTitle: TemplateTitleEntity)

    @Delete
    suspend fun delete(templateTitle: TemplateTitleEntity)

    @Query("delete from template WHERE id = :templateId")
    suspend fun deleteByTemplateId(templateId: Long)

    @Query("SELECT * FROM template")
    fun getAll(): Flow<List<TemplateTitleEntity>>

    @Query("SELECT * FROM template WHERE id = :id")
    suspend fun getTemplateById(id: Long): TemplateTitleEntity

    @Query("SELECT * FROM template WHERE title = :title")
    suspend fun getAllByTitle(title: String): List<TemplateTitleEntity>
}
