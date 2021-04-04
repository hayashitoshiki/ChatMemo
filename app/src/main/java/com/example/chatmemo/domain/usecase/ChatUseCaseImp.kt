package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.repository.DataBaseRepository

class ChatUseCaseImp(private val dataBaseRepository: DataBaseRepository) : ChatUseCase {
    override fun updateRoom(chatRoom: ChatRoom) {
        TODO("Not yet implemented")
    }

    override fun getRoomAll(): LiveData<List<ChatRoomEntity>> {
        return dataBaseRepository.getRoomAll()
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        TODO("Not yet implemented")
    }

    override suspend fun createRoom(chatRoom: ChatRoom) {
        dataBaseRepository.createRoom(chatRoom)
    }

    override suspend fun getNextRoomId(): RoomId {
        return dataBaseRepository.getNextId()
    }

    override fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom> {
        TODO("Not yet implemented")
    }

    override fun updateComment(CommentList: List<Comment>) {
        TODO("Not yet implemented")
    }

    override fun getCommentAll(roomId: RoomId): List<Comment> {
        TODO("Not yet implemented")
    }


}