package com.example.chatmemo.ui.chat

import androidx.lifecycle.*
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.domain.value.TemplateMode
import com.example.chatmemo.domain.value.User
import kotlinx.coroutines.launch
import java.util.*

/**
 * チャット画面_UIロジック
 *
 * @property chatUseCase Chatに関するUseCase
 */
class ChatViewModel(
    id: RoomId, private val chatUseCase: ChatUseCase
) : ViewModel() {

    val chatRoomEntity: LiveData<ChatRoom> = chatUseCase.getChatRoomByRoomById(id)
    val commentText = MutableLiveData("")
    private val _commentList = MutableLiveData<List<Comment>>(listOf())
    val commentList: LiveData<List<Comment>> = _commentList
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    private var isFirst = true

    init {
        _commentList.postValue(chatRoomEntity.value!!.commentList)
        _isEnableSubmitButton.addSource(commentText) { changeSubmitButton(it) }
    }

    // ルーム更新
    fun updateRoom(chatRoomEntity: ChatRoom) {
        viewModelScope.launch {
            if (isFirst) {
                _commentList.postValue(chatUseCase.getCommentAll(chatRoomEntity.roomId))
                isFirst = false
            }
        }
    }

    // 送信
    fun submit() {
        viewModelScope.launch {
            chatRoomEntity.value?.also { room ->
                val comment = Comment(commentText.value!!, User.BLACK, getDataNow())
                room.commentList.add(comment)


                // 定型文がある場合は定型文も送信
                if (room.template != null) {
                    when (val mode = room.templateMode) {
                        is TemplateMode.Order  -> {
                            val message = room.template!!.templateMessageList[mode.position].massage
                            val templateComment = Comment(message, User.WHITE, getDataNow())
                            room.commentList.add(templateComment)
                            if (mode.position >= room.template!!.templateMessageList.size) {
                                mode.position = 0
                            } else {
                                mode.position++
                            }
                        }
                        is TemplateMode.Randam -> {
                            val randomList = room.template!!.templateMessageList.filterIndexed { idx, _ ->
                                !mode.position.contains(idx)
                            }
                            val message = randomList.random()
                            val templateComment = Comment(message.massage, User.WHITE, getDataNow())
                            room.commentList.add(templateComment)
                            if (randomList.size <= 1) {
                                mode.position.clear()
                            } else {
                                val position = room.template!!.templateMessageList.indexOf(message)
                                mode.position.add(position)
                            }
                        }
                    }
                }
                chatUseCase.updateRoom(room)
            }
        }
        _commentList.postValue(chatRoomEntity.value!!.commentList)
        commentText.postValue("")

    }

    // 日付取得
    private fun getDataNow(): Date {
        return Date(System.currentTimeMillis())
    }

    // ルーム削除
    fun deleteRoom() {
        viewModelScope.launch {
            chatUseCase.deleteRoom(chatRoomEntity.value!!.roomId)
        }
    }

    // 立場変更
    fun changeUser() {
        chatRoomEntity.value?.commentList?.let { commentList ->
            commentList.forEachIndexed { index, comment ->
                commentList[index] = when (comment.user) {
                    User.WHITE -> Comment(comment.message, User.BLACK, comment.time)
                    User.BLACK -> Comment(comment.message, User.WHITE, comment.time)
                }
            }
            viewModelScope.launch {
                chatUseCase.updateComment(commentList)
                _commentList.postValue(commentList)
            }
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
