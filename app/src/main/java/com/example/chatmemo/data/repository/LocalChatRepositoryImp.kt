package com.example.chatmemo.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.data.local.database.entity.ChatRoomEntity
import com.example.chatmemo.data.local.database.entity.CommentEntity
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.*
import com.example.chatmemo.ui.MyApplication
import com.example.chatmemo.ui.utils.toLocalDateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class LocalChatRepositoryImp(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : LocalChatRepository {

    private val roomDao = MyApplication.database.roomDao()
    private val commentDao = MyApplication.database.commentDao()
    private val templateDao = MyApplication.database.templateDao()
    private val phraseDao = MyApplication.database.phraseDao()

    override suspend fun getNextRoomId(): RoomId {
        return withContext(ioDispatcher) {
            val id = roomDao.getNextId() ?: 0
            return@withContext RoomId(id.toInt() + 1)
        }
    }

    override suspend fun createRoom(chatRoom: ChatRoom) {
        withContext(ioDispatcher) {
            val id = chatRoom.roomId.value.toLong()
            val title = chatRoom.title
            val templateId: Long?
            val point: String?
            val mode: Int?
            val templateConfiguration = chatRoom.templateConfiguration
            if (templateConfiguration != null) {
                templateId = templateConfiguration.template.templateId.value.toLong()
                point = when (val templateMode = templateConfiguration.templateMode) {
                    is TemplateMode.Order -> {
                        templateMode.position.toString()
                    }
                    is TemplateMode.Randam -> {
                        templateMode.position.joinToString()
                    }
                }
                mode = templateConfiguration.templateMode.getInt()
            } else {
                templateId = null
                point = null
                mode = null
            }
            val chatRoomEntity = ChatRoomEntity(id, title, templateId, mode, point, null, null)

            roomDao.insert(chatRoomEntity)
        }
    }

    override suspend fun deleteRoom(roomId: RoomId) {
        withContext(ioDispatcher) {
            val id = roomId.value.toLong()
            roomDao.deleteById(id)
            commentDao.deleteById(id)
        }
    }

    override suspend fun updateRoom(chatRoom: ChatRoom) {
        return withContext(ioDispatcher) {
            val id = chatRoom.roomId.value.toLong()
            val title = chatRoom.title
            val templateId: Long?
            val point: String?
            val mode: Int?
            val templateConfiguration = chatRoom.templateConfiguration
            if (templateConfiguration != null) {
                templateId = templateConfiguration.template.templateId.value.toLong()
                point = when (val templateMode = templateConfiguration.templateMode) {
                    is TemplateMode.Order -> {
                        templateMode.position.toString()
                    }
                    is TemplateMode.Randam -> {
                        templateMode.position.joinToString()
                    }
                }
                mode = templateConfiguration.templateMode.getInt()
            } else {
                templateId = null
                point = null
                mode = null
            }
            val commentLast: String?
            val commentLastTime: String?
            if (chatRoom.commentList.size != 0) {
                commentLast = chatRoom.commentList.last().message
                commentLastTime = chatRoom.commentList.last().time.toDataBaseDate()
            } else {
                commentLast = null
                commentLastTime = null
            }

            val chatRoomEntity = ChatRoomEntity(id, title, templateId, mode, point, commentLast, commentLastTime)
            return@withContext roomDao.update(chatRoomEntity)
        }
    }

    override fun getRoomAll(): LiveData<List<ChatRoom>> {
        val chatRoomEntityList = roomDao.getAll()
        val chatRoom = MutableLiveData<List<ChatRoom>>()
        chatRoomEntityList.observeForever {
            val list = it.map { chatRoomEntity ->
                val roomId = RoomId(chatRoomEntity.id!!.toInt())
                val title = chatRoomEntity.title
                val commentList = mutableListOf<Comment>()
                chatRoomEntity.commentTime?.let { date ->
                    val message = chatRoomEntity.commentLast ?: ""
                    val commentDate = CommentDateTime(date.toLocalDateTime())
                    val comment = Comment(message, User.BLACK, commentDate)
                    commentList.add(comment)
                }
                ChatRoom(roomId, title, null, commentList)
            }
            chatRoom.postValue(list)
        }
        return chatRoom
    }

    override fun getRoomById(roomId: RoomId): LiveData<ChatRoom> {
        val chatRoomEntity = roomDao.getRoomById(roomId.value.toLong())
        val chatRoom = MutableLiveData<ChatRoom>()
        chatRoomEntity.observeForever {
            it?.let { chatroomEntity ->
                val roomTitle = chatroomEntity.title
                val commentList: MutableList<Comment> = runBlocking {
                    commentDao.getAllCommentByRoom(chatroomEntity.id!!)
                }.map { commentEntity ->
                    val message = commentEntity.text
                    val user = User.getUser(commentEntity.user)
                    val commentDate = CommentDateTime(commentEntity.createdAt.toLocalDateTime())
                    return@map Comment(message, user, commentDate)
                }.toMutableList()
                val templateConfiguration = if (chatroomEntity.templateId != null) {
                    val templateHeader = runBlocking { templateDao.getTemplateById(it.templateId!!) }
                    val templatePhrase = runBlocking {
                        phraseDao.getAllByTitle(templateHeader.id!!).map { TemplateMessage(it.text) }
                    }
                    val template = Template(TemplateId(templateHeader.id!!.toInt()), templateHeader.title, templatePhrase)
                    val templateMode = TemplateMode.toStatus(it.templateMode!!)
                    TemplateConfiguration(template, templateMode)
                } else {
                    null
                }
                chatRoom.postValue(ChatRoom(roomId, roomTitle, templateConfiguration, commentList))
            }
        }
        return chatRoom
    }

    override suspend fun getRoomByTemplateId(templateId: TemplateId): List<ChatRoom> {
        return withContext(ioDispatcher) {
            return@withContext roomDao.getRoomByTemplateId(templateId.value.toLong()).map { chatRoomEntity ->
                val roomId = RoomId(chatRoomEntity.id!!.toInt())
                val title = chatRoomEntity.title
                val commentList = mutableListOf<Comment>()
                chatRoomEntity.commentTime?.let { date ->
                    val message = chatRoomEntity.commentLast ?: ""
                    val commentDate = CommentDateTime(date.toLocalDateTime())
                    val comment = Comment(message, User.BLACK, commentDate)
                    commentList.add(comment)
                }
                ChatRoom(roomId, title, null, commentList)
            }
        }
    }

    override suspend fun addComment(comment: Comment, roomId: RoomId) {
        return withContext(ioDispatcher) {
            val message = comment.message
            val user = comment.user.chageInt()
            val date = comment.time.toDataBaseDate()
            val roomIdLong = roomId.value.toLong()
            val commentEntity = CommentEntity(null, message, user, date, roomIdLong)

            return@withContext commentDao.insert(commentEntity)
        }
    }

    override suspend fun updateComments(commentList: List<Comment>) {
        withContext(ioDispatcher) {
            commentList.forEach {
                val user = it.user.chageInt()
                val commentDate = it.time.toDataBaseDate()
                commentDao.updateUserBy(user, commentDate)
            }
        }
    }
}
