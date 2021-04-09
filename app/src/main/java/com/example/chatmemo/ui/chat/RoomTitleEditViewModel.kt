package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.ui.utils.BaseViewModel
import com.example.chatmemo.ui.utils.ViewModelLiveData

/**
 * ルーム名変更ダイアログ_ロジック
 * @property chatRoomEntity chatRoom
 * @property chatRoomUseCase Chatに関するUseCase
 */
class RoomTitleEditViewModel(
    private var chatRoomEntity: ChatRoom, private val chatRoomUseCase: ChatUseCase
) : BaseViewModel() {

    val newRoomTitle = MutableLiveData("")
    val oldRoomTitle: ViewModelLiveData<String> = ViewModelLiveData<String>()
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    // 初期化
    init {
        oldRoomTitle.setValue(chatRoomEntity.title)
        _isEnableSubmitButton.addSource(newRoomTitle) { changeSubmitButton(it) }
    }

    // ルーム名変更
    suspend fun changeRoomName(roomName: String) {
        chatRoomEntity.title = roomName
        chatRoomUseCase.updateRoom(chatRoomEntity)
    }

    // 変更ボタン活性・非活性制御
    private fun changeSubmitButton(title: String) {
        if (oldRoomTitle.value == null) return
        _isEnableSubmitButton.postValue(title.isNotEmpty() && title != oldRoomTitle.value!!)
    }
}