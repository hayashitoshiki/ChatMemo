package com.myapp.chatmemo.presentation.chat

import android.view.MotionEvent
import android.view.View
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemRoomBinding
import com.myapp.chatmemo.presentation.utils.expansion.OnRoomListItemClickListener
import com.xwray.groupie.databinding.BindableItem

/**
 * ルームリスト用アイテム
 */
class RoomListItem(
    private val item: ChatRoom,
    private val listenerRoomList: OnRoomListItemClickListener
) : BindableItem<ItemRoomBinding>() {

    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    override fun getLayout(): Int = R.layout.item_room

    override fun bind(
        binding: ItemRoomBinding,
        position: Int
    ) {
        val room = item
        var x = 0f
        viewBinderHelper.bind(binding.container, room.toString())

        // ルーム名
        binding.nameLabel.text = room.title
        // 最新コメント
        if (room.commentList.size != 0) {
            val comment = room.commentList.last()
            binding.txtComment.visibility = View.VISIBLE
            binding.txtComment.text = comment.message
            binding.txtDate.visibility = View.VISIBLE
            binding.txtDate.text = comment.time.toSectionDate()
        } else {
            binding.txtComment.visibility = View.GONE
            binding.txtDate.visibility = View.GONE
        }

        // カードタップ
        binding.containerMain.transitionName = room.roomId.toString()
        binding.containerMain.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                }
                MotionEvent.ACTION_UP -> {
                    if (-5 < x - event.x && x - event.x < 5) {
                        listenerRoomList.onItemClickListener(v, position, room)
                    }
                    x = 0f
                }
            }
            true
        }
        // 削除ボタン
        binding.btnDelete.setOnClickListener {
            if (binding.container.isOpened) {
                listenerRoomList.onItemClickListener(it, position, room)
            }
        }
    }

    override fun getId(): Long = item.roomId.value
}
