package com.myapp.chatmemo.presentation.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemCommentBlackBinding
import com.xwray.groupie.databinding.BindableItem

/**
 * チャットリスト_自分のコメント
 */
class CommentBlackItem(
    private val item: Comment,
    private val context: Context,
) : BindableItem<ItemCommentBlackBinding>() {

    override fun getLayout(): Int = R.layout.item_comment_black

    override fun bind(
        binding: ItemCommentBlackBinding,
        position: Int
    ) {
        binding.comment = item
        binding.layoutCommentBlack.setOnLongClickListener {
            val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PhoneNumber", binding.txtCommentBlack.text)
            clipboard.setPrimaryClip(clip)
            true
        }
    }
}
