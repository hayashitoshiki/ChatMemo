package com.myapp.chatmemo.presentation.utils.expansion

import android.view.View
import com.myapp.chatmemo.domain.model.entity.Template

/**
 * テンプレートタイトルリスト用アイテムクリックリスナー
 */
interface OnTemplateTitleListItemClickListener {
    fun onItemClickListener(
        view: View,
        position: Int,
        item: Template
    )
}
