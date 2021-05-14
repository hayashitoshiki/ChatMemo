package com.myapp.chatmemo.data.local.database.dao

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DataBaseConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
