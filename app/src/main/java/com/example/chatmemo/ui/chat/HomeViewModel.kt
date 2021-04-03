package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.model.entity.ChatRoomEntity
import kotlinx.coroutines.launch

/**
 * ホーム画面_ロジック
 * @property chatUseCase Chatに関するUseCase
 */
class HomeViewModel(
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    val chatRoomEntityList: LiveData<List<ChatRoomEntity>> = chatUseCase.getRoomAll()

    // ルーム削除
    fun deleteRoom(roomId: RoomId) {
        viewModelScope.launch {
            chatUseCase.deleteRoom(roomId)
        }
    }
}