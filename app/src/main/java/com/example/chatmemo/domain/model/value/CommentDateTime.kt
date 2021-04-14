package com.example.chatmemo.domain.model.value

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

inline class CommentDateTime(val date: LocalDateTime) {
    fun toSectionDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        return df.format(date).substring(0, 10)
    }

    fun toMessageDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        return df.format(date).substring(11, 16)
    }

    fun toDataBaseDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.SSS")
        return df.format(date)
    }
}
