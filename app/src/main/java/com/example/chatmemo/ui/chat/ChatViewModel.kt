package com.example.chatmemo.ui.chat

import androidx.lifecycle.*
import com.example.chatmemo.model.Const
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.CommentEntity
import com.example.chatmemo.model.entity.PhraseEntity
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

    val chatRoomEntity: LiveData<ChatRoomEntity> = dataBaseRepository.getRoomById(id)
    val commentText = MutableLiveData("")
    private val _commentList = MutableLiveData<List<CommentEntity>>(listOf())
    val commentList: LiveData<List<CommentEntity>> = _commentList
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    private var phraseList = listOf<PhraseEntity>()
    private var isFirst = true

    init {
        _isEnableSubmitButton.addSource(commentText) { changeSubmitButton(it) }
    }

    // ルーム更新
    fun updateRoom(chatRoomEntity: ChatRoomEntity) {
        viewModelScope.launch {
            if (isFirst) {
                _commentList.postValue(dataBaseRepository.getCommentAll(chatRoomEntity.id!!))
                isFirst = false
            }
            // 定型文設定あり
            chatRoomEntity.templateId?.also {
                phraseList = dataBaseRepository.getPhraseByTitle(it)
            }
        }
    }

    // 送信
    fun submit() {
        viewModelScope.launch {
            chatRoomEntity.value?.also { room ->
                val comment = CommentEntity(
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
                            val comment2 = CommentEntity(
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
                            var phraseList2 = arrayOf<PhraseEntity>()
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
                            val comment2 = CommentEntity(
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
            dataBaseRepository.deleteRoom(chatRoomEntity.value!!.id!!)
            dataBaseRepository.deleteCommentByRoomId(chatRoomEntity.value!!.id!!)
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
                _commentList.postValue(dataBaseRepository.getCommentAll(chatRoomEntity.value!!.id!!))
            }
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
