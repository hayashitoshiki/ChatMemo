package com.example.chatmemo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.chatmemo.R
import com.example.chatmemo.model.entity.Phrase
import java.util.*

/**
 * 定型文作成画面用のリストビューアダプター
 * @property items 定型文リスト
 */
class PhraseListAdapter(private var items: ArrayList<Phrase>) :
    RecyclerView.Adapter<PhraseListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private var viewBinderHelper: ViewBinderHelper = ViewBinderHelper()

    // 参照するviewの定義
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView: TextView = v.findViewById(R.id.txt_phrase)
        var deleteButton: ImageButton = v.findViewById(R.id.btn_delete)
        var swipeRevealLayout: SwipeRevealLayout = v.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.item_phrase,
            parent,
            false
        )
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =  items[position]
        viewBinderHelper.bind(holder.swipeRevealLayout, items[position].toString())

        // 値代入
        holder.textView.text = item.text
        // 削除ボタン
        holder.deleteButton.setOnClickListener {
            if (holder.swipeRevealLayout.isOpened) {
                listener.onItemClickListener(it, position)
            }
        }
    }

    fun setData(items: ArrayList<Phrase>) {
        this.items = items
    }

    //インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}