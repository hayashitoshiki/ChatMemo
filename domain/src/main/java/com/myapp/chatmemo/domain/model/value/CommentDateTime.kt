package com.myapp.chatmemo.domain.model.value

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * コメントした日時
 *
 * @property date 日時データ
 */
data class CommentDateTime(val date: LocalDateTime) {
    /**
     * セクション用の日時文字列を返す
     * @return yyyy/MM/dd
     */
    fun toSectionDate(): String {
        val systemDateTime = LocalDateTime.ofInstant(date.toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        val sectionData = df.format(systemDateTime)
            .substring(0, 10)
        return getDataString(sectionData)
    }

    /**
     * メッセージリスト用の日時文字列を返す
     * @return hh:mm
     */
    fun toMessageDate(): String {
        val systemDateTime = LocalDateTime.ofInstant(date.toInstant(ZoneOffset.UTC), ZoneId.systemDefault())
        val df = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss")
        return df.format(systemDateTime)
            .substring(11, 16)
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
