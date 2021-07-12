package com.myapp.chatmemo.presentation.chat

import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemCommentHeaderBinding
import com.xwray.groupie.databinding.BindableItem
import java.time.ZoneOffset

/**
 * チャットリスト_日付ヘッダー
 */
class CommentHeaderItem(private val item: Comment) : BindableItem<ItemCommentHeaderBinding>() {

    override fun getLayout(): Int = R.layout.item_comment_header

    override fun bind(binding: ItemCommentHeaderBinding, position: Int) {
        binding.txtDate.text = item.time.toSectionDate()
    }
}
