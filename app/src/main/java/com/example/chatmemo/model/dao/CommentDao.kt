package com.example.chatmemo.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.chatmemo.model.entity.CommentEntity

/**
 * コメント用クエリ管理
 */
@Dao
interface CommentDao {

    @Insert
    fun insert(comment: CommentEntity)

    @Update
    fun update(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE room_id = :roomId")
    fun getAllCommentByRoom(roomId: Long): List<CommentEntity>

    @Query("delete from comments WHERE room_id = :roomId")
    fun deleteById(roomId: Long)

}
