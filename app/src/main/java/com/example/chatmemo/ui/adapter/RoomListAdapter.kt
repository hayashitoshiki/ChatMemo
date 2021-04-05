package com.example.chatmemo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.chatmemo.databinding.ItemRoomListBinding
import com.example.chatmemo.domain.model.ChatRoom


/**
 * ルームリスト用アダプター
 */
class RoomListAdapter(private var items: List<ChatRoom>) : RecyclerView.Adapter<RoomListAdapter.ViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = ItemRoomListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = items[position]
        var x = 0f
        viewBinderHelper.bind(holder.swipeRevealLayout, items[position].toString())

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
                MotionEvent.ACTION_UP   -> {
                    if (-5 < x - event.x && x - event.x < 5) {
                        listener.onItemClickListener(v, position, items[position])
                    }
                    x = 0f
                }
            }
            true
        }
        // 削除ボタン
        holder.deleteButton.setOnClickListener {
            if (holder.swipeRevealLayout.isOpened) {
                listener.onItemClickListener(it, position, items[position])
            }
        }
    }

    // データ更新
    fun setData(items: List<ChatRoom>) {
        this.items = items
    }

    //インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, item: ChatRoom)
    }

    // リスナー設定
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}