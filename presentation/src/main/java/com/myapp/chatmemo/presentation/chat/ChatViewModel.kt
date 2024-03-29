package com.myapp.chatmemo.presentation.chat

import androidx.lifecycle.*
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * チャット画面_UIロジック
 *
 * @property chatUseCase チャットルームに関するビジネスロジック
 */
class ChatViewModel @AssistedInject constructor(
    private val chatUseCase: ChatUseCase,
    @Assisted("id") private val id: RoomId
) : ViewModel() {

    @AssistedFactory
    interface ViewModelAssistedFactory {
        fun create(@Assisted("id") id: RoomId): ChatViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: ViewModelAssistedFactory,
            id: RoomId
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(id) as T
            }
        }
    }

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
                val message = commentText.value ?: return@also
                val comment = chatUseCase.addComment(message, roomId)
                val commentList: MutableList<Comment> = _commentList.value?.toMutableList() ?: mutableListOf()
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
            chatUseCase.deleteRoom(chatRoom.value?.roomId ?: return@launch)
        }
    }

    // 立場変更
    fun changeUser() {
        commentList.value?.let {
            val newCommentList = chatUseCase.reverseAllCommentUser(it)
            _commentList.value = newCommentList
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
