package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.repository.DataBaseRepository

/**
 * ルーム名変更ダイアログ_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class RoomTitleEditViewModel(
    private var chatRoom: ChatRoom, private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val newRoomTitle = MutableLiveData("")
    private val _oldRoomTitle = MutableLiveData<String>()
    val oldRoomTitle: LiveData<String> = _oldRoomTitle
    private val _isEnableSubmitButton = MediatorLiveData<Boolean>()
    val isEnableSubmitButton: LiveData<Boolean> = _isEnableSubmitButton

    // 初期化
    init {
        _oldRoomTitle.value = chatRoom.title
        _isEnableSubmitButton.addSource(newRoomTitle) { changeSubmitButton(it) }
    }

    // ルーム名変更
    suspend fun changeRoomName(roomName: String) {
        chatRoom.title = roomName
        dataBaseRepository.updateRoom(chatRoom)
    }

    // 変更ボタン活性・非活性制御
    private fun changeSubmitButton(title: String) {
        _isEnableSubmitButton.postValue(title.isNotEmpty() && title != oldRoomTitle.value!!)
    }
}