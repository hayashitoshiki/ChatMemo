package com.myapp.chatmemo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myapp.chatmemo.data.database.entity.ChatRoomEntity
import com.myapp.chatmemo.data.database.entity.CommentEntity
import com.myapp.chatmemo.data.database.entity.TemplateMessageEntity
import com.myapp.chatmemo.data.database.entity.TemplateTitleEntity

/**
 * DB定義
 */
@Database(
    entities = [CommentEntity::class, TemplateMessageEntity::class, ChatRoomEntity::class, TemplateTitleEntity::class],
    version = 2, exportSchema = false
)
@TypeConverters(DataBaseConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun commentDao(): com.myapp.chatmemo.data.database.dao.CommentDao
    abstract fun phraseDao(): com.myapp.chatmemo.data.database.dao.PhraseDao
    abstract fun roomDao(): com.myapp.chatmemo.data.database.dao.RoomDao
    abstract fun templateDao(): com.myapp.chatmemo.data.database.dao.TemplateDao
}
