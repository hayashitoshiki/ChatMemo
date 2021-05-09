package com.myapp.chatmemo.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.chatmemo.ui.utils.expansion.getDateTimeNow
import java.time.LocalDateTime

/**
 * 定型文テーブル
 * @property id プライマリーキー
 * @property text 定型文
 * @property templateId 定型文タイトルのId
 */
@Entity(tableName = "phrases")
data class TemplateMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var text: String,
    @ColumnInfo(name = "template_id") var templateId: Long,
    @ColumnInfo(name = "create_at") val createAt: LocalDateTime = getDateTimeNow(),
    @ColumnInfo(name = "update_at") var updateAt: LocalDateTime = getDateTimeNow()
)
