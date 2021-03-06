package com.example.chatmemo.ui.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatmemo.R
import com.example.chatmemo.model.Const
import com.example.chatmemo.model.entity.Comment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * チャット画面のリサイクルビュー
 *
 * @property context コンテキスト
 * @property commentList 取得したコメント情報
 */
class ChatRecyclerAdapter(
    private val context: Context, private var commentList: List<Comment>
) : RecyclerView.Adapter<ChatRecyclerAdapter.ViewHolder>() {

    // 参照するviewの定義
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val commentBlackLayout: LinearLayout = v.findViewById(R.id.layout_comment_black)
        val commentWhiteLayout: LinearLayout = v.findViewById(R.id.layout_comment_white)
        val commentBlackTextView: TextView = v.findViewById(R.id.txt_comment_black)
        val commentWhiteTextView: TextView = v.findViewById(R.id.txt_comment_white)
        val dateBlackTextView: TextView = v.findViewById(R.id.txt_date_black)
        val dateWhiteTextView: TextView = v.findViewById(R.id.txt_date_white)
        val dataTextView: TextView = v.findViewById(R.id.txt_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = commentList[position]
        if (position == 0 || (position > 0 && commentList[position - 1].createdAt.substring(
                0, 10
            ) != commentList[position].createdAt.substring(0, 10))
        ) {
            comment.createdAt.substring(0, 10).let {
                holder.dataTextView.visibility = View.VISIBLE
                holder.dataTextView.text = if (it == getDataNow()) {
                    "今日"
                } else {
                    it
                }
            }
        } else {
            holder.dataTextView.visibility = View.GONE
        }
        when (comment.user) {
            Const.BLACK -> {
                holder.commentBlackLayout.visibility = View.VISIBLE
                holder.commentWhiteLayout.visibility = View.GONE
                holder.dateBlackTextView.text = comment.createdAt.substring(11, 16)
                holder.commentBlackTextView.text = comment.text
                holder.commentBlackTextView.setOnLongClickListener {
                    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        "PhoneNumber", holder.commentBlackTextView.text
                    )
                    clipboard.setPrimaryClip(clip)
                    true
                }
            }
            Const.WHITE -> {
                holder.commentBlackLayout.visibility = View.GONE
                holder.commentWhiteLayout.visibility = View.VISIBLE
                holder.dateWhiteTextView.text = comment.createdAt.substring(11, 16)
                holder.commentWhiteTextView.text = comment.text
                holder.commentWhiteTextView.setOnLongClickListener {
                    val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText(
                        "PhoneNumber", holder.commentBlackTextView.text
                    )
                    clipboard.setPrimaryClip(clip)
                    true
                }
            }
        }
    }

    // 日付取得
    @SuppressLint("SimpleDateFormat")
    private fun getDataNow(): String {
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }

    fun setData(items: List<Comment>) {
        this.commentList = items
    }
}
