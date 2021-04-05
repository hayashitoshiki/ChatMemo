package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.model.repository.DataBaseRepository

class ChatUseCaseImp(private val dataBaseRepository: DataBaseRepository) : ChatUseCase {

    override suspend fun updateRoom(chatRoom: ChatRoom) {
        dataBaseRepository.updateRoom(chatRoom)
    }

    override fun getRoomAll(): LiveData<List<ChatRoom>> {
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
        return dataBaseRepository.getRoomById(roomId)
    }

    override suspend fun updateComment(commentList: List<Comment>, roomId: RoomId) {
        dataBaseRepository.updateComment(commentList, roomId)
    }

    override suspend fun addComment(comment: Comment, roomId: RoomId) {
        dataBaseRepository.addComment(comment, roomId)
    }

    override fun getCommentAll(roomId: RoomId): List<Comment> {
        TODO("Not yet implemented")
    }


}