package com.myapp.chatmemo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.chatmemo.data.database.entity.ChatRoomEntity
import kotlinx.coroutines.flow.Flow

/**
 * チャットルーム用クエリ管理
 */
@Dao
interface RoomDao {

    /**
     * チャットルーム追加
     *
     * @param chatRoomEntity 追加するチャットルーム
     * @return
     */
    @Insert
    suspend fun insert(chatRoomEntity: ChatRoomEntity): Long

    /**
     * チャットルーム更新
     *
     * @param chatRoomEntity 更新するチャットルーム
     */
    @Update
    suspend fun update(chatRoomEntity: ChatRoomEntity)

    /**
     * 新規で登録する際のチャットルームID取得
     *
     * @return 現在格納されているチャットルームID + 1
     */
    @Query("SELECT MAX(id) FROM room")
    suspend fun getNextId(): Long?

    /**
     * 全てのチャットルーム取得
     *
     * @return 格納されている全チャットルームリスト
     */
    @Query("SELECT * FROM room")
    fun getAll(): Flow<List<ChatRoomEntity>>

    /**
     * 定型文IDに紐づくチャットルームリスト取得
     *
     * @param templateId 取得する定型文ID
     * @return 定型文IDに紐づいたチャットルームリスト
     */
    @Query("SELECT * FROM room WHERE template_id = :templateId")
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity>

    /**
     * チャットルームIDに紐づいたチャットルーム取得
     *
     * @param id 取得するチャットルームID
     * @return チャットルームIDに紐づいたチャットルーム
     */
    @Query("SELECT * FROM room WHERE id = :id")
    fun getRoomById(id: Long): Flow<ChatRoomEntity>

    /**
     * チャットルームIDに紐づいたチャットルーム削除
     *
     * @param id 削除するチャットルームID
     */
    @Query("delete from room WHERE id = :id")
    suspend fun deleteById(id: Long)
}
