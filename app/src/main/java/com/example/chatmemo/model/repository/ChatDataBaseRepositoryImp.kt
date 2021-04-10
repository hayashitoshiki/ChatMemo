package com.example.chatmemo.model.repository

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId

class ChatDataBaseRepositoryImp : ChatDataBaseRepository {
    override suspend fun getNextRoomId(): RoomId {
        TODO("Not yet implemented")
    }

    override suspend fun createRoom(chatRoom: ChatRoom) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRoom(chatRoom: ChatRoom) {
        TODO("Not yet implemented")
    }

    override fun getRoomAll(): LiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override fun getRoomById(roomId: RoomId): LiveData<ChatRoom> {
        TODO("Not yet implemented")
    }

    override suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoom> {
        TODO("Not yet implemented")
    }

    override suspend fun addComment(comment: Comment, roomId: RoomId) {
        TODO("Not yet implemented")
    }

    override suspend fun updateComments(commentList: List<Comment>) {
        TODO("Not yet implemented")
    }
}