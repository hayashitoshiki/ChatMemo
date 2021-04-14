package com.example.chatmemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * コメントテーブル
 * @property id プライマリーキー
 * @property text コメント
 * @property user ユーザー
 * @property createdAt 生成時間 yyyy/MM/dd HH:mm
 * @property roomId 紐づいているルームID
 */
@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var text: String,
    var user: Int,
    @ColumnInfo(name = "created_at") var createdAt: String,
    @ColumnInfo(name = "room_id") var roomId: Long
)
