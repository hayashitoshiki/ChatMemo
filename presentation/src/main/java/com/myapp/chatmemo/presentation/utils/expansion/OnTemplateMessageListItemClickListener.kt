package com.myapp.chatmemo.presentation.utils.expansion

import android.view.View
import com.myapp.chatmemo.domain.model.value.TemplateMessage

/**
 * テンプレート文言リスト用アイテムクリックリスナー
 */
interface OnTemplateMessageListItemClickListener {
    fun onItemClickListener(
        view: View,
        position: Int,
        item: TemplateMessage
    )
}
