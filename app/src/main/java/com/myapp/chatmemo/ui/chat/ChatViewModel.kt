package com.myapp.chatmemo.ui.chat

import androidx.lifecycle.*
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.ui.utils.expansion.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * チャット画面_UIロジック
 *
 * @property chatUseCase チャットルームに関するビジネスロジック
 */
class ChatViewModel(
    id: RoomId,
    private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val chatRoom = chatUseCase.getChatRoomByRoomById(id)
        .asLiveData()
    val commentText = MutableLiveData("")
    private val _commentList = MediatorLiveData<List<Comment>>()
    val commentList: LiveData<List<Comment>> = _commentList
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        _commentList.addSource(chatRoom) { updateRoom(it) }
        _isEnableSubmitButton.addSource(commentText) { changeSubmitButton(it) }
    }

    // ルーム更新
    private fun updateRoom(chatRoom: ChatRoom) {
        if (commentList.value == null) {
            _commentList.value = chatRoom.commentList
        }
    }

    // 送信
    fun submit() {
        viewModelScope.launch {
            chatRoom.value?.also { room ->
                val roomId = room.roomId
                val message = commentText.value!!
                val comment = chatUseCase.addComment(message, roomId)
                val commentList: MutableList<Comment> = _commentList.value!!.toMutableList()
                commentList.add(comment)
                room.commentList = commentList
                room.templateConfiguration?.let { templateConfiguration ->
                    delay(300)
                    val (configuretion, templateComment) = chatUseCase.addTemplateComment(templateConfiguration, roomId)
                    room.templateConfiguration = configuretion
                    commentList.add(templateComment)
                    room.commentList = commentList
                }

                chatUseCase.updateRoom(room)
                _commentList.value = commentList
                commentText.value = ""
            }
        }
    }

    // ルーム削除
    fun deleteRoom() {
        viewModelScope.launch {
            chatUseCase.deleteRoom(chatRoom.value!!.roomId)
        }
    }

    // 立場変更
    fun changeUser() {
        if (!commentList.value.isNullOrEmpty()) {
            val newCommentList = chatUseCase.reverseAllCommentUser(commentList.value!!)
            _commentList.value = newCommentList
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
