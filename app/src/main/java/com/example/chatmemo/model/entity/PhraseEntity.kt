package com.example.chatmemo.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 定型文テーブル
 * @property id プライマリーキー
 * @property text 定型文
 * @property templateId 定型文タイトルのId
 */
@Entity(tableName = "phrases")
data class PhraseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    var text: String,
    @ColumnInfo(name = "template_id") var templateId: Long
)