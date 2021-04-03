package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId

class ChatUseCaseImp : ChatUseCase {
    override fun updateRoom(chatRoom: ChatRoom) {
        TODO("Not yet implemented")
    }

    override fun getRoomAll(): LiveData<List<ChatRoom>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        TODO("Not yet implemented")
    }

    override fun createRoom(chatRoom: ChatRoom): ChatRoom {
        TODO("Not yet implemented")
    }

    override fun getNextRoomId(): RoomId {
        TODO("Not yet implemented")
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