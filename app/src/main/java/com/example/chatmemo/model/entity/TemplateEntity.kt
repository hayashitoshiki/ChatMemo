package com.example.chatmemo.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 定型文タイトルテーブル
 * @property id プライマリーキー
 * @property title 定型文のタイトル
 */
@Entity(tableName = "template")
data class TemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?, var title: String
) : java.io.Serializable