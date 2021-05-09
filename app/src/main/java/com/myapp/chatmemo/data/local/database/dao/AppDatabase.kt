package com.myapp.chatmemo.data.local.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.chatmemo.data.local.database.entity.ChatRoomEntity
import com.myapp.chatmemo.data.local.database.entity.CommentEntity
import com.myapp.chatmemo.data.local.database.entity.TemplateMessageEntity
import com.myapp.chatmemo.data.local.database.entity.TemplateTitleEntity

/**
 * DB定義
 */
@Database(entities = [CommentEntity::class, TemplateMessageEntity::class, ChatRoomEntity::class, TemplateTitleEntity::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun phraseDao(): PhraseDao
    abstract fun roomDao(): RoomDao
    abstract fun templateDao(): TemplateDao
}
