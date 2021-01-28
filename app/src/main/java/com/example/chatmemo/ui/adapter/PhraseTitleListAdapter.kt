package com.example.chatmemo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.chatmemo.R
import com.example.chatmemo.model.entity.Template

/**
 * 定型文リスト画面用のリサイクルビューアダプター
 * @property items 定型文のタイトルリスト
 */
class PhraseTitleListAdapter(private var items: List<Template>) : RecyclerView.Adapter<PhraseTitleListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    // 参照するviewの定義
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.txt_name)
        val deleteButton: ImageButton = v.findViewById(R.id.btn_delete)
        val swipeRevealLayout: SwipeRevealLayout = v.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.item_phrase_title, parent, false
        )
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var x = 0f
        val template = items[position]
        viewBinderHelper.bind(holder.swipeRevealLayout, items[position].toString())

        // 値代入
        holder.textView.text = template.title
        holder.textView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                }
                MotionEvent.ACTION_UP   -> {
                    if (-5 < x - event.x && x - event.x < 5) {
                        listener.onItemClickListener(holder.textView, position, template)
                    }
                    x = 0f
                }
            }
            true
        }
        // 削除ボタン
        holder.deleteButton.setOnClickListener {
            if (holder.swipeRevealLayout.isOpened) {
                listener.onItemClickListener(it, position, template)
            }
        }
    }

    fun setData(items: List<Template>) {
        this.items = items
    }

    //インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, item: Template)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}