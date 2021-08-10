package com.myapp.chatmemo.presentation.template

import com.myapp.chatmemo.domain.model.value.TemplateMessage
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemTemplateMessageBinding
import com.myapp.chatmemo.presentation.utils.expansion.OnTemplateMessageListItemClickListener
import com.xwray.groupie.databinding.BindableItem

/**
 * テンプレート文言リスト用アイテム
 */
class TemplateMessageItem(
    private val item: TemplateMessage,
    private val listenerTemplateMessageList: OnTemplateMessageListItemClickListener
) : BindableItem<ItemTemplateMessageBinding>() {

    override fun getLayout(): Int = R.layout.item_template_message

    override fun bind(
        binding: ItemTemplateMessageBinding,
        position: Int
    ) {
        binding.container.close(true)
        // 値代入
        binding.txtIndex.text = (position + 1).toString() + ". "
        binding.txtPhrase.text = item.massage
        // 削除ボタン
        binding.btnDelete.setOnClickListener {
            if (binding.container.isOpened) {
                listenerTemplateMessageList.onItemClickListener(it, position, item)
            }
        }
    }
}
