package com.myapp.chatmemo.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.chatmemo.domain.getDateTimeNow
import java.time.LocalDateTime

/**
 * 定型文タイトルテーブル
 * @property id プライマリーキー
 * @property title 定型文のタイトル
 */
@Entity(tableName = "template")
data class TemplateTitleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var title: String,
    @ColumnInfo(name = "create_at") val createAt: LocalDateTime = getDateTimeNow(),
    @ColumnInfo(name = "update_at") var updateAt: LocalDateTime = getDateTimeNow()
) : java.io.Serializable {
    fun update(templateTitleEntity: TemplateTitleEntity) {
        title = templateTitleEntity.title
        updateAt = getDateTimeNow()
    }
}
