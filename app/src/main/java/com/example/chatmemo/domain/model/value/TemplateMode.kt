package com.example.chatmemo.domain.model.value

sealed class TemplateMode(val massage: String) {
    data class Order(var message: String = "順番", var position: Int = 0) : TemplateMode(message)
    data class Randam(
        var message: String = "ランダム", var position: MutableList<Int> = mutableListOf()
    ) : TemplateMode(message)

    fun getInt(): Int {
        return when (this) {
            is Order  -> 1
            is Randam -> 2
        }
    }

    companion object {
        fun toStatus(index: Int): TemplateMode {
            return when (index) {
                1    -> Order()
                2    -> Randam()
                else -> Order()
            }
        }
    }

}