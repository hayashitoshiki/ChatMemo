package com.example.chatmemo.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatmemo.model.entity.ChatRoom

/**
 * 定型文用クエリ管理
 */
@Dao
interface RoomDao {

    @Insert
    suspend fun insert(chatRoom: ChatRoom)

    @Update
    suspend fun update(chatRoom: ChatRoom)

    @Query("SELECT * FROM room")
    fun getAll(): LiveData<List<ChatRoom>>

    @Query("SELECT * FROM room WHERE title = :title")
    suspend fun getRoomByTitle(title: String): List<ChatRoom>

    @Query("SELECT * FROM room WHERE template_id = :templateId")
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoom>

    @Query("SELECT * FROM room WHERE id = :id")
    fun getRoomById(id: Long): LiveData<ChatRoom>

    @Query("delete from room WHERE id = :id")
    suspend fun deleteById(id: Long)
}
