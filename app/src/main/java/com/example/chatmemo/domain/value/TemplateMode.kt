package com.example.chatmemo.domain.value

sealed class TemplateMode(val massage: String) {
    data class None(var message: String = "") : TemplateMode(message)
    data class Order(var message: String = "", var position: Int = 0) : TemplateMode(message)
    data class Randam(
        var message: String = "",
        var position: MutableList<Int> = mutableListOf()
    ) : TemplateMode(message)
}