package com.myapp.chatmemo.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.chatmemo.R
import com.myapp.chatmemo.databinding.ItemCommentBlackBinding
import com.myapp.chatmemo.databinding.ItemCommentWhiteBinding
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.User

/**
 * チャット画面のリサイクルビュー
 *
 * @property context コンテキスト
 * @property lifecycleOwner ライフサイクル
 */
class ChatRecyclerAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner) :
    ListAdapter<Comment, ChatRecyclerAdapter.ViewHolder>(DiffCallback) {

    // Viewのタイプ定数
    companion object {

        // 自分のコメント
        private const val VIEW_TYPE_BLACK = 2

        // 相手のコメント
        private const val VIEW_TYPE_WHITE = 3
    }

    // ViewType取得
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).user) {
            User.BLACK -> VIEW_TYPE_BLACK
            User.WHITE -> VIEW_TYPE_WHITE
        }
    }

    // 表示するレイアウト
    private fun getLayoutRes(viewType: Int): Int {
        return when (viewType) {
            VIEW_TYPE_BLACK -> R.layout.item_comment_black
            VIEW_TYPE_WHITE -> R.layout.item_comment_white
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    // Binding
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ViewDataBinding = DataBindingUtil.bind(v)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(getLayoutRes(viewType), parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        val beforComment = if (position != 0) getItem(position - 1) else null
        val state = ChatRecyclerItemState(beforComment, comment)
        when (holder.binding) {
            is ItemCommentBlackBinding -> {
                holder.binding.txtDate.visibility = if (state.isHeader1) View.VISIBLE else View.GONE
                holder.binding.txtDate.text = comment.time.toSectionDate()
                holder.binding.txtDateBlack.text = comment.time.toMessageDate()
                holder.binding.txtCommentBlack.text = comment.message
                holder.binding.layoutCommentBlack.setOnLongClickListener {
                    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("PhoneNumber", holder.binding.txtCommentBlack.text)
                    clipboard.setPrimaryClip(clip)
                    true
                }
            }
            is ItemCommentWhiteBinding -> {
                holder.binding.txtDate.visibility = if (state.isHeader1) View.VISIBLE else View.GONE
                holder.binding.txtDate.text = comment.time.toSectionDate()
                holder.binding.txtDateWhite.text = comment.time.toMessageDate()
                holder.binding.txtCommentWhite.text = comment.message
                holder.binding.layoutCommentWhite.setOnLongClickListener {
                    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("PhoneNumber", holder.binding.txtCommentWhite.text)
                    clipboard.setPrimaryClip(clip)
                    true
                }
            }
        }
    }
}

private object DiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.time == newItem.time
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}