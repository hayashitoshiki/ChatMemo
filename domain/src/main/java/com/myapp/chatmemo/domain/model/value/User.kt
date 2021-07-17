package com.myapp.chatmemo.domain.model.value

/**
 * チャットのユーザ定義
 */
enum class User {
    BLACK, WHITE;

    /**
     * ユーザの数値分類取得
     * @return 自分→1、相手→2
     */
    fun chageInt(): Int {
        return when (this) {
            BLACK -> 1
            WHITE -> 2
        }
    }

    companion object {
        /**
         * 数値からユーザ取得
         *
         * @param index 数値
         * @return 1→自分、2→相手
         */
        fun getUser(index: Int): User {
            return when (index) {
                1 -> BLACK
                2 -> WHITE
                else -> BLACK
            }
        }
    }
}
