package com.example.chatmemo.data.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatmemo.data.database.entity.ChatRoomEntity
import com.example.chatmemo.data.database.entity.CommentEntity
import com.example.chatmemo.data.database.entity.PhraseEntity
import com.example.chatmemo.data.database.entity.TemplateEntity

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
