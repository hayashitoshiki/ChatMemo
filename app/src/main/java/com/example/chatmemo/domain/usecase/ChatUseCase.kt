package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId

interface ChatUseCase {
    fun updateRoom(chatRoom: ChatRoom)

    fun getRoomAll(): LiveData<List<ChatRoom>>

    suspend fun deleteRoom(roomId: RoomId)

    fun createRoom(chatRoom: ChatRoom): ChatRoom

    fun getNextRoomId(): RoomId

    fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom>

    fun updateComment(CommentList: List<Comment>)

    fun getCommentAll(roomId: RoomId): List<Comment>
}