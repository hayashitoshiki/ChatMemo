package com.example.chatmemo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatmemo.data.database.entity.CommentEntity

/**
 * コメント用クエリ管理
 */
@Dao
interface CommentDao {

    @Insert
    suspend fun insert(comment: CommentEntity)

    @Update
    suspend fun update(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE room_id = :roomId")
    suspend fun getAllCommentByRoom(roomId: Long): List<CommentEntity>

    @Query("delete from comments WHERE room_id = :roomId")
    suspend fun deleteById(roomId: Long)

    @Query("update comments SET user = :user WHERE created_at = :createAt")
    suspend fun updateUserBy(user: Int, createAt: String)
}
