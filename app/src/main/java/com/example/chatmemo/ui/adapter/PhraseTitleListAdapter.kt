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
import com.example.chatmemo.databinding.ItemPhraseTitleBinding
import com.example.chatmemo.model.entity.Template

/**
 * 定型文リスト画面用のリサイクルビューアダプター
 * @property items 定型文のタイトルリスト
 */
class PhraseTitleListAdapter(private var items: List<Template>) : RecyclerView.Adapter<PhraseTitleListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    // 参照するviewの定義
    open class ViewHolder(v: ItemPhraseTitleBinding) : RecyclerView.ViewHolder(v.root) {
        val textView: TextView = v.txtName
        val deleteButton: ImageButton = v.btnDelete
        val swipeRevealLayout: SwipeRevealLayout = v.container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = ItemPhraseTitleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
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
        holder.textView.transitionName = template.id.toString()
        holder.textView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                }
                MotionEvent.ACTION_UP -> {
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

    // インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, item: Template)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
