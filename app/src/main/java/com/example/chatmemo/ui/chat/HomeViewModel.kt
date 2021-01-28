package com.example.chatmemo.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.repository.DataBaseRepository
import kotlinx.coroutines.launch

/**
 * ホーム画面_ロジック
 * @property dataBaseRepository DB取得リポジトリ
 */
class HomeViewModel(private val dataBaseRepository: DataBaseRepository) : ViewModel() {

    val chatRoomList: LiveData<List<ChatRoom>> = dataBaseRepository.getRoomAll()

    // ルーム削除
    fun deleteRoom(roomId: Long) {
        viewModelScope.launch {
            dataBaseRepository.deleteRoom(roomId)
            dataBaseRepository.deleteCommentByRoomId(roomId)
        }
    }
}