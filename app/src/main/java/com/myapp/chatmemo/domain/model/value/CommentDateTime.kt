package com.myapp.chatmemo.domain.model.value

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * コメントした日時
 */
inline class CommentDateTime(val date: LocalDateTime) {
    /**
     * セクション用の日時文字列を返す
     * yyyy/MM/dd
     */
    fun toSectionDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        val sectionData = df.format(date).substring(0, 10)
        return getDataString(sectionData)
    }

    /**
     * メッセージリスト用の日時文字列を返す
     * hh:mm
     */
    fun toMessageDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        return df.format(date).substring(11, 16)
    }

    /**
     * データベース保存用の日時文字列を返す
     * yyyy/MM/dd hh:mm:ss.SSS
     */
    fun toDataBaseDate(): String {
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss.SSS")
        return df.format(date)
    }

    // 日時変換
    private fun getDataString(dateString: String): String {
        return if (dateString == getDataNow()) {
            "今日"
        } else {
            dateString
        }
    }

    // 日付取得
    @SuppressLint("SimpleDateFormat")
    private fun getDataNow(): String {
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }
}
