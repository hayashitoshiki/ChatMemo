package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.value.*
import com.example.chatmemo.ui.utils.BaseViewModel
import com.example.chatmemo.ui.utils.ViewModelLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * チャット画面_UIロジック
 *
 * @property chatUseCase Chatに関するUseCase
 */
class ChatViewModel(
    id: RoomId, private val chatUseCase: ChatUseCase
) : BaseViewModel() {

    val chatRoom: LiveData<ChatRoom> = chatUseCase.getChatRoomByRoomById(id)
    val commentText = MutableLiveData("")
    val commentList: ViewModelLiveData<List<Comment>> = ViewModelLiveData<List<Comment>>()
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    init {
        _isEnableSubmitButton.addSource(commentText) { changeSubmitButton(it) }
    }

    // ルーム更新
    fun updateRoom(chatRoom: ChatRoom) {
        commentList.postValue(chatRoom.commentList)
    }

    // 送信
    fun submit() {
        viewModelScope.launch {
            chatRoom.value?.also { room ->
                val message = commentText.value!!
                val date = CommentDateTime(LocalDateTime.now())
                val comment = Comment(message, User.BLACK, date)
                room.commentList.add(comment)
                chatUseCase.addComment(comment, room.roomId)

                // 定型文がある場合は定型文も送信
                if (room.templateConfiguration != null) {
                    val templateComment = when (val mode = room.templateConfiguration!!.templateMode) {
                        is TemplateMode.Order  -> {
                            val templateMessage = room.templateConfiguration!!.template.templateMessageList[mode.position].massage
                            val templateMessageDate = CommentDateTime(LocalDateTime.now())
                            if (mode.position + 1 >= room.templateConfiguration!!.template.templateMessageList.size) {
                                mode.position = 0
                            } else {
                                mode.position++
                            }
                            Comment(templateMessage, User.WHITE, templateMessageDate)
                        }
                        is TemplateMode.Randam -> {
                            val randomList = room.templateConfiguration!!.template.templateMessageList.filterIndexed { idx, _ ->
                                !mode.position.contains(idx)
                            }
                            val templateMessage = randomList.random().massage
                            val templateMessageDate = CommentDateTime(LocalDateTime.now())
                            if (randomList.size <= 1) {
                                mode.position.clear()
                            } else {
                                val position = room.templateConfiguration!!.template.templateMessageList.indexOf(
                                    message
                                )
                                mode.position.add(position)
                            }
                            Comment(templateMessage, User.WHITE, templateMessageDate)
                        }
                    }
                    room.commentList.add(templateComment)
                    chatUseCase.addComment(templateComment, room.roomId)
                }
                chatUseCase.updateRoom(room)
            }
        }
        commentList.postValue(chatRoom.value!!.commentList)
        commentText.postValue("")
    }

    // ルーム削除
    fun deleteRoom() {
        GlobalScope.launch {
            chatUseCase.deleteRoom(chatRoom.value!!.roomId)
        }
    }

    // 立場変更
    fun changeUser() {
        chatRoom.value?.commentList?.let { comments ->
            comments.forEachIndexed { index, comment ->
                comments[index] = when (comment.user) {
                    User.WHITE -> Comment(comment.message, User.BLACK, comment.time)
                    User.BLACK -> Comment(comment.message, User.WHITE, comment.time)
                }
            }
            GlobalScope.launch {
                chatUseCase.updateComment(comments, chatRoom.value!!.roomId)
                commentList.postValue(comments)
            }
        }
    }

    // 送信ボタンの活性非活性制御
    private fun changeSubmitButton(message: String) {
        _isEnableSubmitButton.postValue(message.isNotEmpty())
    }
}
