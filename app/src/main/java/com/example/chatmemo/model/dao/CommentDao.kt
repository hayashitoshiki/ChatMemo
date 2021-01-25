package com.example.chatmemo.model.dao

import androidx.room.*
import com.example.chatmemo.model.entity.Comment

/**
 * コメント用クエリ管理
 */
@Dao
interface CommentDao {

    @Insert
    fun insert(comment: Comment)

    @Update
    fun update(comment: Comment)

    @Query("SELECT * FROM comments WHERE room_id = :roomId")
    fun getAllCommentByRoom(roomId: Long): List<Comment>

    @Query("delete from comments WHERE room_id = :roomId")
    fun deleteById(roomId: Long)

}
