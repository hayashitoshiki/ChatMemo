package com.example.chatmemo.model.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Comment
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.model.entity.Template

/**
 * DB定義
 */
@Database(
    entities = [Comment::class, Phrase::class, ChatRoom::class, Template::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun phraseDao(): PhraseDao
    abstract fun roomDao(): RoomDao
    abstract fun templateDao(): TemplateDao
}
