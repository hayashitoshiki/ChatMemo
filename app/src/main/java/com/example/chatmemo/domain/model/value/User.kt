package com.example.chatmemo.domain.model.value

enum class User {
    BLACK, WHITE;

    fun chageInt(): Int {
        return when (this) {
            BLACK -> 1
            WHITE -> 2
        }
    }

    companion object {
        fun getUser(index: Int): User {
            return when (index) {
                1 -> BLACK
                2 -> WHITE
                else -> BLACK
            }
        }
    }
}
