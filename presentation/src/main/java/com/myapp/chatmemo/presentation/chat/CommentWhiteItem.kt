package com.myapp.chatmemo.presentation.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemCommentWhiteBinding
import com.xwray.groupie.databinding.BindableItem

/**
 * チャットリスト_相手のコメント
 */
class CommentWhiteItem(
    private val item: Comment,
    private val context: Context,
) : BindableItem<ItemCommentWhiteBinding>() {

    override fun getLayout(): Int = R.layout.item_comment_white

    override fun bind(
        binding: ItemCommentWhiteBinding,
        position: Int
    ) {
        binding.comment = item
        binding.layoutCommentWhite.setOnLongClickListener {
            val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("PhoneNumber", binding.txtCommentWhite.text)
            clipboard.setPrimaryClip(clip)
            true
        }
    }
}
