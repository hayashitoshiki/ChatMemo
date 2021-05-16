package com.myapp.chatmemo.presentation.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.presentation.databinding.ItemRoomListBinding

/**
 * ルームリスト用アダプター
 */
class RoomListAdapter : ListAdapter<ChatRoom, RoomListAdapter.ViewHolder>(ChatRoomDiffCallback) {

    private lateinit var listener: OnItemClickListener
    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    // 参照するviewの定義
    open class ViewHolder(v: ItemRoomListBinding) : RecyclerView.ViewHolder(v.root) {
        val swipeRevealLayout: SwipeRevealLayout = v.container
        val container: ConstraintLayout = v.containerMain
        val roomNameTextView: TextView = v.nameLabel
        val commentTextView: TextView = v.txtComment
        val timeTextView: TextView = v.txtDate
        val deleteButton: ImageButton = v.btnDelete
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = ItemRoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val room = getItem(position)
        var x = 0f
        viewBinderHelper.bind(holder.swipeRevealLayout, room.toString())

        // ルーム名
        holder.roomNameTextView.text = room.title
        // 最新コメント
        if (room.commentList.size != 0) {
            val comment = room.commentList.last()
            holder.commentTextView.visibility = View.VISIBLE
            holder.commentTextView.text = comment.message
            holder.timeTextView.visibility = View.VISIBLE
            holder.timeTextView.text = comment.time.toSectionDate()
        } else {
            holder.commentTextView.visibility = View.GONE
            holder.timeTextView.visibility = View.GONE
        }

        // カードタップ
        holder.container.transitionName = room.roomId.toString()
        holder.container.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                }
                MotionEvent.ACTION_UP -> {
                    if (-5 < x - event.x && x - event.x < 5) {
                        listener.onItemClickListener(v, position, room)
                    }
                    x = 0f
                }
            }
            true
        }
        // 削除ボタン
        holder.deleteButton.setOnClickListener {
            if (holder.swipeRevealLayout.isOpened) {
                listener.onItemClickListener(it, position, room)
            }
        }
    }

    // インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(
            view: View,
            position: Int,
            item: ChatRoom
        )
    }

    // リスナー設定
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

private object ChatRoomDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
    override fun areItemsTheSame(
        oldItem: ChatRoom,
        newItem: ChatRoom
    ): Boolean {
        return oldItem.roomId == newItem.roomId
    }

    override fun areContentsTheSame(
        oldItem: ChatRoom,
        newItem: ChatRoom
    ): Boolean {
        return oldItem == newItem
    }
}
