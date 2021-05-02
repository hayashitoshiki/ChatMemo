package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.value.RoomId
import com.example.chatmemo.domain.usecase.ChatUseCase
import kotlinx.coroutines.launch

/**
 * ホーム画面_ロジック
 * @property chatUseCase Chatに関するUseCase
 */
class HomeViewModel(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    val chatRoomEntityList: LiveData<List<ChatRoom>> = chatUseCase.getRoomAll().asLiveData()

    // ルーム削除
    fun deleteRoom(roomId: RoomId) {
        viewModelScope.launch {
            chatUseCase.deleteRoom(roomId)
        }
    }
}
