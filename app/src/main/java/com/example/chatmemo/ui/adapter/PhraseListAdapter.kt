package com.example.chatmemo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.example.chatmemo.databinding.ItemPhraseBinding
import com.example.chatmemo.model.entity.Phrase
import java.util.*

/**
 * 定型文作成画面用のリストビューアダプター
 * @property items 定型文リスト
 */
class PhraseListAdapter(private var items: ArrayList<Phrase>) : RecyclerView.Adapter<PhraseListAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    // 参照するviewの定義
    open class ViewHolder(v: ItemPhraseBinding) : RecyclerView.ViewHolder(v.root) {
        var indexText: TextView = v.txtIndex
        var phraseText: TextView = v.txtPhrase
        var deleteButton: ImageButton = v.btnDelete
        var swipeRevealLayout: SwipeRevealLayout = v.container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = ItemPhraseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.swipeRevealLayout.close(true)

        // 値代入
        holder.indexText.text = (position + 1).toString() + ". "
        holder.phraseText.text = item.text

        // 削除ボタン
        holder.deleteButton.setOnClickListener {
            if (holder.swipeRevealLayout.isOpened) {
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount)
                listener.onItemClickListener(it, position, items)
            }
        }
    }

    fun setData(items: ArrayList<Phrase>) {
        this.items = items
    }

    // インターフェースの作成
    interface OnItemClickListener {
        fun onItemClickListener(view: View, position: Int, items: ArrayList<Phrase>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
