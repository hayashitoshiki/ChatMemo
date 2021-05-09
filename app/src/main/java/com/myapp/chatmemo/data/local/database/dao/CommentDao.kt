package com.myapp.chatmemo.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.chatmemo.data.local.database.entity.CommentEntity
import java.time.LocalDateTime

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

    @Query("SELECT  * from comments WHERE coment_time = :commentTim")
    suspend fun getCommentByDate(commentTim: LocalDateTime): CommentEntity
}
