package com.example.chatmemo.model.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.CommentEntity
import com.example.chatmemo.model.entity.PhraseEntity
import com.example.chatmemo.model.entity.TemplateEntity

/**
 * DB定義
 */
@Database(
    entities = [CommentEntity::class, PhraseEntity::class, ChatRoomEntity::class, TemplateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun phraseDao(): PhraseDao
    abstract fun roomDao(): RoomDao
    abstract fun templateDao(): TemplateDao
}
