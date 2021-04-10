package com.example.chatmemo.domain.value

sealed class DataBaseResult<out R> {
    data class Success<out T>(val data: T) : DataBaseResult<T>()
    data class Error(val exception: Exception) : DataBaseResult<Nothing>()
}