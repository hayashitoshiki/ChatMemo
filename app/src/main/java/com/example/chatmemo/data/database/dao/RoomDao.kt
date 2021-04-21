package com.example.chatmemo.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatmemo.data.database.entity.ChatRoomEntity

/**
 * 定型文用クエリ管理
 */
@Dao
interface RoomDao {

    @Insert
    suspend fun insert(chatRoomEntity: ChatRoomEntity): Long

    @Update
    suspend fun update(chatRoomEntity: ChatRoomEntity)

    @Query("SELECT MAX(id) FROM room")
    suspend fun getNextId(): Long?

    @Query("SELECT * FROM room")
    fun getAll(): LiveData<List<ChatRoomEntity>>

    @Query("SELECT * FROM room WHERE template_id = :templateId")
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity>

    @Query("SELECT * FROM room WHERE id = :id")
    fun getRoomById(id: Long): LiveData<ChatRoomEntity>

    @Query("delete from room WHERE id = :id")
    suspend fun deleteById(id: Long)
}
