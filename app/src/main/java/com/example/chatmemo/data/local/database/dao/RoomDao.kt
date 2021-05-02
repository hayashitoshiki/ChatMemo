package com.example.chatmemo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatmemo.data.local.database.entity.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

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
    fun getAll(): Flow<List<ChatRoomEntity>>

    @Query("SELECT * FROM room WHERE template_id = :templateId")
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity>

    @Query("SELECT * FROM room WHERE id = :id")
    fun getRoomById(id: Long): Flow<ChatRoomEntity>

    @Query("delete from room WHERE id = :id")
    suspend fun deleteById(id: Long)
}
