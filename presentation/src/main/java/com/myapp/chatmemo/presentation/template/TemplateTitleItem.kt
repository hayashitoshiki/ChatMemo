package com.myapp.chatmemo.presentation.template

import android.view.MotionEvent
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.presentation.R
import com.myapp.chatmemo.presentation.databinding.ItemTemplateTitleBinding
import com.myapp.chatmemo.presentation.utils.expansion.OnTemplateTitleListItemClickListener
import com.xwray.groupie.databinding.BindableItem

/**
 * テンプレートタイトルリスト用アイテム
 */
class TemplateTitleItem(
    private val item: Template,
    private val listenerTemplateTitleList: OnTemplateTitleListItemClickListener
) : BindableItem<ItemTemplateTitleBinding>() {

    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    override fun getLayout(): Int = R.layout.item_template_title

    override fun bind(binding: ItemTemplateTitleBinding, position: Int) {
        var x = 0f
        viewBinderHelper.bind(binding.container, item.toString())

        // 値代入
        binding.txtName.text = item.title
        binding.txtName.transitionName = item.templateId.toString()
        binding.txtName.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x = event.x
                }
                MotionEvent.ACTION_UP -> {
                    if (-5 < x - event.x && x - event.x < 5) {
                        listenerTemplateTitleList.onItemClickListener(binding.txtName, position, item)
                    }
                    x = 0f
                }
            }
            true
        }
        // 削除ボタン
        binding.btnDelete.setOnClickListener {
            if (binding.container.isOpened) {
                listenerTemplateTitleList.onItemClickListener(it, position, item)
            }
        }
    }

    override fun getId(): Long = item.templateId.value
}
