package com.myapp.chatmemo.presentation.utils.expansion

import android.view.View
import com.myapp.chatmemo.domain.model.entity.ChatRoom

/**
 * ルームリスト用アイテムクリックリスナー
 */
interface OnRoomListItemClickListener {
    fun onItemClickListener(
        view: View,
        position: Int,
        item: ChatRoom
    )
}
