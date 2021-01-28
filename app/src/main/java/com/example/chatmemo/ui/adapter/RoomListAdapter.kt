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
import com.example.chatmemo.R
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
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val swipeRevealLayout: SwipeRevealLayout = v.findViewById(R.id.container)
        val container: ConstraintLayout = v.findViewById(R.id.container_main)
        val roomNameTextView: TextView = v.findViewById(R.id.name_label)
        val commentTextView: TextView = v.findViewById(R.id.txt_comment)
        val timeTextView: TextView = v.findViewById(R.id.txt_date)
        val deleteButton: ImageButton = v.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.item_room_list, parent, false
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