package com.myapp.chatmemo.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.myapp.chatmemo.data.database.entity.CommentEntity
import java.time.LocalDateTime

/**
 * コメント用クエリ管理
 */
@Dao
interface CommentDao {

    /**
     * コメント追加Dao
     *
     * @param comment 追加するコメント
     */
    @Insert
    suspend fun insert(comment: CommentEntity)

    /**
     * コメントアップデート
     *
     * @param comment 更新するコメント
     */
    @Update
    suspend fun update(comment: CommentEntity)

    /**
     * 部屋に紐づくコメント取得
     *
     * @param roomId ルームID
     * @return ルームIDに紐づくコメントリスト
     */
    @Query("SELECT * FROM comments WHERE room_id = :roomId")
    suspend fun getAllCommentByRoom(roomId: Long): List<CommentEntity>

    /**
     * ルームIDに紐づく全コメント削除
     *
     * @param roomId 削除するルームID
     */
    @Query("delete from comments WHERE room_id = :roomId")
    suspend fun deleteByRoomId(roomId: Long)

    /**
     * 送信日時に紐づくコメント取得
     *
     * @param commentTim 取得するコメントの送信日時
     * @return 送信日時に紐づくコメント
     */
    @Query("SELECT  * from comments WHERE coment_time = :commentTim")
    suspend fun getCommentByDate(commentTim: LocalDateTime): CommentEntity
}
