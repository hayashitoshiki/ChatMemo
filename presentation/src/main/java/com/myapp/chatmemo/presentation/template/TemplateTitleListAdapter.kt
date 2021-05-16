package com.myapp.chatmemo.presentation.template

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.presentation.databinding.ItemPhraseTitleBinding

/**
 * 定型文リスト画面用のリサイクルビューアダプター
 */
class PhraseTitleListAdapter : ListAdapter<Template, PhraseTitleListAdapter.ViewHolder>(TemplateDiffCallback) {

    private lateinit var listener: OnItemClickListener
    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    // 参照するviewの定義
    open class ViewHolder(v: ItemPhraseTitleBinding) : RecyclerView.ViewHolder(v.root) {
        val textView: TextView = v.txtName
        val deleteButton: ImageButton = v.btnDelete
        val swipeRevealLayout: SwipeRevealLayout = v.container
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = ItemPhraseTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        var x = 0f
        val template = getItem(position)
        viewBinderHelper.bind(holder.swipeRevealLayout, template.toString())

        // 値代入
        holder.textView.text = template.title
        holder.textView.transitionName = template.templateId.toString()
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

    // インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(
            view: View,
            position: Int,
            item: Template
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}

private object TemplateDiffCallback : DiffUtil.ItemCallback<Template>() {
    override fun areItemsTheSame(
        oldItem: Template,
        newItem: Template
    ): Boolean {
        return oldItem.templateId == newItem.templateId
    }

    override fun areContentsTheSame(
        oldItem: Template,
        newItem: Template
    ): Boolean {
        return oldItem == newItem
    }
}
