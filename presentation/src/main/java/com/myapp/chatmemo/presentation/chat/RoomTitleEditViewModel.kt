package com.myapp.chatmemo.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.presentation.utils.expansion.BaseViewModel

/**
 * ルーム名変更ダイアログ_ロジック
 * @property chatRoomEntity chatRoom
 * @property chatRoomUseCase Chatに関するUseCase
 */
class RoomTitleEditViewModel(
    private var chatRoomEntity: ChatRoom,
    private val chatRoomUseCase: ChatUseCase
) : BaseViewModel() {

    val newRoomTitle = MutableLiveData("")
    private val _oldRoomTitle = MutableLiveData("")
    val oldRoomTitle: LiveData<String> = _oldRoomTitle
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    // 初期化
    init {
        _oldRoomTitle.value = chatRoomEntity.title
        _isEnableSubmitButton.addSource(newRoomTitle) { changeSubmitButton(it) }
    }

    // ルーム名変更
    suspend fun changeRoomName(roomName: String) {
        chatRoomEntity.title = roomName
        chatRoomUseCase.updateRoom(chatRoomEntity)
    }

    // 変更ボタン活性・非活性制御
    private fun changeSubmitButton(title: String) {
        _isEnableSubmitButton.postValue(title.isNotEmpty() && title != _oldRoomTitle.value!!)
    }
}
