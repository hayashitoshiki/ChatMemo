package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.model.Const
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Comment
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * チャット画面_UIロジック
 *
 * @property dataBaseRepository DBアクセス用repository
 */
class ChatViewModel(
    id: Long, private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val chatRoom: LiveData<ChatRoom> = dataBaseRepository.getRoomById(id)
    val commentText = MutableLiveData("")
    private val _commentList = MutableLiveData<List<Comment>>(listOf())
    val commentList: LiveData<List<Comment>> = _commentList
    private val _isEnableSubmitButton = MutableLiveData(false)
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    private var phraseList = listOf<Phrase>()
    private var isFirst = true

    // ルーム更新
    fun updateRoom(chatRoom: ChatRoom) {
        viewModelScope.launch {
            if (isFirst) {
                _commentList.postValue(dataBaseRepository.getCommentAll(chatRoom.id!!))
                isFirst = false
            }
            // 定型文設定あり
            chatRoom.templateId?.also {
                phraseList = dataBaseRepository.getPhraseByTitle(it)
            }
        }
    }

    // 送信
    fun submit() {
        viewModelScope.launch {
            chatRoom.value?.also { room ->
                val comment = Comment(
                    null, commentText.value!!, Const.BLACK, getDataNow(), room.id!!
                )
                dataBaseRepository.addComment(comment)
                room.commentLast = comment.text
                room.commentTime = comment.createdAt
                dataBaseRepository.updateRoom(room)

                // 定型文がある場合は定型文も送信
                if (room.templateId != null) {
                    when (room.templateMode) {
                        // 順番出力
                        Const.ORDER  -> {
                            val index = if (room.phrasePoint != null && phraseList.size - 1 != room.phrasePoint!!.toInt()) {
                                room.phrasePoint!!.toInt() + 1
                            } else {
                                0
                            }
                            val comment2 = Comment(
                                null, phraseList[index].text, Const.WHITE, getDataNow(), room.id
                            )
                            dataBaseRepository.addComment(comment2)
                            room.commentLast = comment2.text
                            room.commentTime = comment2.createdAt
                            room.phrasePoint = index.toString()
                            dataBaseRepository.updateRoom(room)
                        }
                        // ランダム出力
                        Const.RANDOM -> {
                            var phraseList2 = arrayOf<Phrase>()
                            if (room.phrasePoint != null) {
                                val list = room.phrasePoint!!.split(",")
                                phraseList.forEachIndexed { index, phrase ->
                                    if (!list.contains(index.toString())) {
                                        phraseList2 += phrase
                                    }
                                }
                            }
                            if (phraseList2.isEmpty()) {
                                phraseList2 += phraseList
                                room.phrasePoint = null
                            }
                            val whiteComment = phraseList2.random()
                            val comment2 = Comment(
                                null, whiteComment.text, Const.WHITE, getDataNow(), room.id
                            )
                            dataBaseRepository.addComment(comment2)
                            if (room.phrasePoint == null) {
                                room.phrasePoint = phraseList.indexOf(whiteComment).toString() + ","
                            } else {
                                room.phrasePoint += phraseList.indexOf(whiteComment)
                                    .toString() + ","
                            }
                            room.commentLast = comment2.text
                            room.commentTime = comment2.createdAt
                            dataBaseRepository.updateRoom(room)
                        }
                    }

                }

                _commentList.postValue(dataBaseRepository.getCommentAll(room.id))
                commentText.postValue("")
            }
        }
    }

    // 日付取得
    @Suppress("SimpleDateFormat")
    private fun getDataNow(): String {
        val df: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm")
        val date = Date(System.currentTimeMillis())
        return df.format(date)
    }

    // ルーム削除
    fun deleteRoom() {
        viewModelScope.launch {
            dataBaseRepository.deleteRoom(chatRoom.value!!.id!!)
            dataBaseRepository.deleteCommentByRoomId(chatRoom.value!!.id!!)
        }
    }

    // 立場変更
    fun changeUser() {
        commentList.value?.let {
            it.forEach { comments ->
                when (comments.user) {
                    Const.WHITE -> comments.user = Const.BLACK
                    Const.BLACK -> comments.user = Const.WHITE
                }
            }
            viewModelScope.launch {
                dataBaseRepository.updateComment(it)
                _commentList.postValue(dataBaseRepository.getCommentAll(chatRoom.value!!.id!!))
            }
        }
    }

    // 送信ボタンの活性非活性制御
    fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
