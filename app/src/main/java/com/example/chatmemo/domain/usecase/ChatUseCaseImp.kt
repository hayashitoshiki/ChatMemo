package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.data.repository.ChatDataBaseRepository
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.value.*
import java.time.LocalDateTime

class ChatUseCaseImp(private val chatDataBaseRepository: ChatDataBaseRepository) : ChatUseCase {

    override suspend fun updateRoom(chatRoom: ChatRoom) {
        chatDataBaseRepository.updateRoom(chatRoom)
    }

    override fun getRoomAll(): LiveData<List<ChatRoom>> {
        return chatDataBaseRepository.getRoomAll()
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        chatDataBaseRepository.deleteRoom(roomId)
    }

    override suspend fun createRoom(chatRoom: ChatRoom) {
        chatDataBaseRepository.createRoom(chatRoom)
    }

    override suspend fun getNextRoomId(): RoomId {
        return chatDataBaseRepository.getNextRoomId()
    }

    override fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom> {
        return chatDataBaseRepository.getRoomById(roomId)
    }

    override suspend fun reverseAllCommentUser(commentList: List<Comment>): List<Comment> {
        val reverseCommentList = commentList.map {
            when (it.user) {
                User.WHITE -> Comment(it.message, User.BLACK, it.time)
                User.BLACK -> Comment(it.message, User.WHITE, it.time)
            }
        }
        chatDataBaseRepository.updateComments(reverseCommentList)
        return reverseCommentList
    }

    override suspend fun addTemplateComment(
        templateConfiguration: TemplateConfiguration, roomId: RoomId
    ): Pair<TemplateConfiguration, Comment> {
        when (val templateMode = templateConfiguration.templateMode) {
            is TemplateMode.Order -> {
                val templateMessage = templateConfiguration.template.templateMessageList[templateMode.position].massage
                val templateMessageDate = CommentDateTime(LocalDateTime.now())
                if (templateMode.position == templateConfiguration.template.templateMessageList.size - 1) {
                    templateMode.position = 0
                } else {
                    templateMode.position++
                }
                val comment = Comment(templateMessage, User.WHITE, templateMessageDate)
                chatDataBaseRepository.addComment(comment, roomId)
                return Pair(templateConfiguration, comment)
            }
            is TemplateMode.Randam -> {
                val randomList = templateConfiguration.template.templateMessageList.filterIndexed { idx, _ ->
                    !templateMode.position.contains(idx)
                }
                val templateMessage = randomList.random().massage
                val templateMessageDate = CommentDateTime(LocalDateTime.now())
                if (randomList.size == 1) {
                    templateMode.position.clear()
                } else {
                    val position = templateConfiguration.template.templateMessageList.indexOf(TemplateMessage(templateMessage))
                    templateMode.position.add(position)
                }
                val comment = Comment(templateMessage, User.WHITE, templateMessageDate)
                chatDataBaseRepository.addComment(comment, roomId)
                return Pair(templateConfiguration, comment)
            }
        }
    }

    override suspend fun addComment(message: String, roomId: RoomId): Comment {
        val date = CommentDateTime(LocalDateTime.now())
        val comment = Comment(message, User.BLACK, date)
        chatDataBaseRepository.addComment(comment, roomId)
        return comment
    }
}
