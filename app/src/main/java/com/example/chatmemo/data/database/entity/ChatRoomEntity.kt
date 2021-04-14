package com.example.chatmemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @ColumnInfo(name = "comment_time") var commentTime: String?
) : java.io.Serializable
