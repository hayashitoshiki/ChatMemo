package com.myapp.chatmemo.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.chatmemo.ui.utils.expansion.getDateTimeNow
import java.time.LocalDateTime

/**
 * トークルーム
 * @property id プライマリーキー
 * @property title 部屋名
 * @property templateId 定型文タイトル：設定していない場合はnull
 * @property templateMode 定型文の表示モード:定型文を設定していない場合はnull
 * @property phrasePoint 定型文の表示位置メモ：定型文を設定していない場合はnull
 * @property commentLast 最新コメント：一言もコメントしていない場合はnull
 * @property commentTime 最新コメント時間：一言もコメントしていない場合はnull
 */
@Entity(tableName = "room")
data class ChatRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var title: String,
    @ColumnInfo(name = "template_id") var templateId: Long?,
    @ColumnInfo(name = "template_mode") var templateMode: Int?,
    @ColumnInfo(name = "phrase_point") var phrasePoint: String?,
    @ColumnInfo(name = "comment_last") var commentLast: String?,
    @ColumnInfo(name = "comment_time") var commentTime: LocalDateTime?,
    @ColumnInfo(name = "create_at") val createAt: LocalDateTime = getDateTimeNow(),
    @ColumnInfo(name = "update_at") var updateAt: LocalDateTime = getDateTimeNow()
) : java.io.Serializable {
    fun update(chatEoomEntity: ChatRoomEntity) {
        templateId = chatEoomEntity.templateId
        templateMode = chatEoomEntity.templateMode
        phrasePoint = chatEoomEntity.phrasePoint
        commentLast = chatEoomEntity.commentLast
        commentTime = chatEoomEntity.commentTime
        updateAt = getDateTimeNow()
    }
}
