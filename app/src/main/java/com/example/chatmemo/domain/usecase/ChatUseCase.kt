package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.model.entity.ChatRoomEntity

interface ChatUseCase {
    fun updateRoom(chatRoom: ChatRoom)

    fun getRoomAll(): LiveData<List<ChatRoomEntity>>

    suspend fun deleteRoom(roomId: RoomId)

    suspend fun createRoom(chatRoom: ChatRoom)

    suspend fun getNextRoomId(): RoomId

    fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom>

    fun updateComment(CommentList: List<Comment>)

    fun getCommentAll(roomId: RoomId): List<Comment>
}