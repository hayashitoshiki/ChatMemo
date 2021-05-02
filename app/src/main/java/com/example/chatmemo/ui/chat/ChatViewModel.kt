package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.value.Comment
import com.example.chatmemo.domain.model.value.RoomId
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.ui.utils.expansion.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * チャット画面_UIロジック
 *
 * @property chatUseCase チャットルームに関するビジネスロジック
 */
class ChatViewModel(
    id: RoomId, private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val chatRoom = chatUseCase.getChatRoomByRoomById(id)
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
        if (_commentList.value == null || _commentList.value!!.isEmpty()) {
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
                room.commentList.add(comment)

                delay(300)
                room.templateConfiguration?.let { templateConfiguration ->
                    val (configuretion, templateComment) = chatUseCase.addTemplateComment(templateConfiguration, roomId)
                    room.templateConfiguration = configuretion
                    room.commentList.add(templateComment)
                }

                chatUseCase.updateRoom(room)
            }
        }
        _commentList.postValue(chatRoom.value!!.commentList)
        commentText.postValue("")
    }

    // ルーム削除
    fun deleteRoom() {
        viewModelScope.launch {
            chatUseCase.deleteRoom(chatRoom.value!!.roomId)
        }
    }

    // 立場変更
    fun changeUser() {
        commentList.value?.let { comments ->
            viewModelScope.launch {
                val newCommentList = chatUseCase.reverseAllCommentUser(comments)
                _commentList.postValue(newCommentList)
            }
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
