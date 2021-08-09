package com.myapp.chatmemo.presentation.chat

import androidx.lifecycle.*
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ホーム画面_ロジック
 * @property chatUseCase Chatに関するUseCase
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    val chatRoomEntityList: LiveData<List<ChatRoom>> = chatUseCase.getRoomAll()
        .asLiveData()
    private val _isNoDataText: MediatorLiveData<Boolean> = MediatorLiveData()
    val isNoDataText: LiveData<Boolean> = _isNoDataText

    init {
        _isNoDataText.addSource(chatRoomEntityList) { changeNoDataText(it) }
    }

    // チャットルームなし文言テキスト表示制御
    private fun changeNoDataText(chatRoomList: List<ChatRoom>) {
        _isNoDataText.value = chatRoomList.isEmpty()
    }

    /**
     * チャットルーム削除
     *
     * @param roomId 削除するチャットルームのID
     */
    fun deleteRoom(roomId: RoomId) {
        viewModelScope.launch {
            chatUseCase.deleteRoom(roomId)
        }
    }
}
