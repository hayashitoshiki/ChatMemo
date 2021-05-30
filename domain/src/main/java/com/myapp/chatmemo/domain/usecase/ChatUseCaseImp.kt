package com.myapp.chatmemo.domain.usecase

import com.myapp.chatmemo.common.getDateTimeNow
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.*
import com.myapp.chatmemo.domain.repository.LocalChatRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class ChatUseCaseImp @Inject constructor(
    private val localChatRepository: LocalChatRepository,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ChatUseCase {

    override suspend fun updateRoom(chatRoom: ChatRoom) {
        localChatRepository.updateRoom(chatRoom)
    }

    override fun getRoomAll(): Flow<List<ChatRoom>> {
        return localChatRepository.getRoomAll()
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        localChatRepository.deleteRoom(roomId)
    }

    override suspend fun createRoom(chatRoom: ChatRoom) {
        localChatRepository.createRoom(chatRoom)
    }

    override suspend fun getNextRoomId(): RoomId {
        return localChatRepository.getNextRoomId()
    }

    override fun getChatRoomByRoomById(roomId: RoomId): Flow<ChatRoom> {
        return localChatRepository.getRoomById(roomId)
    }

    override fun reverseAllCommentUser(commentList: List<Comment>): List<Comment> {
        val reverseCommentList = commentList.map {
            when (it.user) {
                User.WHITE -> Comment(it.message, User.BLACK, it.time)
                User.BLACK -> Comment(it.message, User.WHITE, it.time)
            }
        }
        externalScope.launch(defaultDispatcher) {
            localChatRepository.updateComments(reverseCommentList)
        }
        return reverseCommentList
    }

    override fun addTemplateComment(
        templateConfiguration: TemplateConfiguration,
        roomId: RoomId
    ): Pair<TemplateConfiguration, Comment> {
        when (val templateMode = templateConfiguration.templateMode) {
            is TemplateMode.Order -> {
                val templateMessage = templateConfiguration.template.templateMessageList[templateMode.position].massage
                val templateMessageDate = CommentDateTime(LocalDateTime.now())
                if (templateMode.position == templateConfiguration.template.templateMessageList.size - 1) {
                    (templateConfiguration.templateMode as TemplateMode.Order).position = 0
                } else {
                    (templateConfiguration.templateMode as TemplateMode.Order).position++
                }
                val comment = Comment(templateMessage, User.WHITE, templateMessageDate)
                externalScope.launch(defaultDispatcher) {
                    localChatRepository.addComment(comment, roomId)
                }
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
                externalScope.launch(defaultDispatcher) {
                    localChatRepository.addComment(comment, roomId)
                }
                return Pair(templateConfiguration, comment)
            }
        }
    }

    override fun addComment(
        message: String,
        roomId: RoomId
    ): Comment {
        val date = CommentDateTime(getDateTimeNow())
        val comment = Comment(message, User.BLACK, date)
        externalScope.launch(defaultDispatcher) {
            localChatRepository.addComment(comment, roomId)
        }
        return comment
    }
}
