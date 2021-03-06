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
import com.example.chatmemo.model.entity.ChatRoom
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
        room.commentLast?.also {
            holder.commentTextView.text = it
            holder.commentTextView.visibility = View.VISIBLE
        } ?: run {
            holder.commentTextView.visibility = View.GONE
        }
        // 最新コメント時間
        room.commentTime?.also { commentTime ->
            commentTime.substring(0, 10).let {
                holder.timeTextView.visibility = View.VISIBLE
                holder.timeTextView.text = if (it == getDataNow()) {
                    commentTime.substring(11, 16)
                } else {
                    it
                }
            }
        } ?: run {
            holder.timeTextView.visibility = View.GONE
        }
        // カードタップ
        holder.container.transitionName = room.id.toString()
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

    // 日付取得
    @SuppressLint("SimpleDateFormat")
    private fun getDataNow(): String {
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
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