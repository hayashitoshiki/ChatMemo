package com.example.chatmemo.ui.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"))
}
