package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId

interface ChatUseCase {

    suspend fun updateRoom(chatRoom: ChatRoom)

    fun getRoomAll(): LiveData<List<ChatRoom>>

    suspend fun deleteRoom(roomId: RoomId)

    suspend fun createRoom(chatRoom: ChatRoom)

    suspend fun getNextRoomId(): RoomId

    fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom>

    suspend fun updateComment(commentList: List<Comment>, roomId: RoomId)

    fun getCommentAll(roomId: RoomId): List<Comment>

    suspend fun addComment(comment: Comment, roomId: RoomId)
}