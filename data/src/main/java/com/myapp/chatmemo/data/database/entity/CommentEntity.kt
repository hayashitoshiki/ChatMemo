package com.myapp.chatmemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.chatmemo.data.getDateTimeNow
import java.time.LocalDateTime

/**
 * コメントテーブル
 * @property id プライマリーキー
 * @property text コメント
 * @property user ユーザー
 * @property roomId 紐づいているルームID
 */
@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var text: String,
    var user: Int,
    @ColumnInfo(name = "coment_time") val commentTime: LocalDateTime,
    @ColumnInfo(name = "room_id") val roomId: Long,
    @ColumnInfo(name = "create_at") val createAt: LocalDateTime = getDateTimeNow(),
    @ColumnInfo(name = "update_at") var updateAt: LocalDateTime = getDateTimeNow()
) : java.io.Serializable {
    fun update(commentEntity: CommentEntity) {
        text = commentEntity.text
        user = commentEntity.user
        updateAt = getDateTimeNow()
    }
}
