package com.myapp.chatmemo.domain.model.value

/**
 * テンプレートの表示形式定義
 */
sealed class TemplateMode(var massage: String) {
    /**
     * 登録順
     */
    data class Order(
        var message: String = "順番",
        var position: Int = 0
    ) : TemplateMode(message)

    /**
     * ランダム
     */
    data class Randam(
        var message: String = "ランダム",
        var position: MutableList<Int> = mutableListOf()
    ) : TemplateMode(message)

    /**
     * テンプレート表示モードの数値取得
     * @return 登録順番→1、ランダム→2
     */
    fun getInt(): Int {
        return when (this) {
            is Order -> 1
            is Randam -> 2
        }
    }

    companion object {
        /**
         * 数値からテンプレートモード取得
         * @param index 数値
         * @return 1→登録順、2→ランダム
         */
        fun toStatus(index: Int): TemplateMode {
            return when (index) {
                1 -> Order()
                2 -> Randam()
                else -> Order()
            }
        }
    }
}
