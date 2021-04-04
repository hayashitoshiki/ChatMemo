package com.example.chatmemo.domain.value

sealed class TemplateMode(val massage: String) {
    data class Order(var message: String = "", var position: Int = 0) : TemplateMode(message)
    data class Randam(
        var message: String = "", var position: MutableList<Int> = mutableListOf()
    ) : TemplateMode(message)

    fun getInt(): Int {
        return when (this) {
            is Order  -> 1
            is Randam -> 2
        }
    }
}