package com.example.chatmemo.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.*
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.CommentEntity
import com.example.chatmemo.model.entity.PhraseEntity
import com.example.chatmemo.model.entity.TemplateEntity
import com.example.chatmemo.ui.MyApplication
import com.example.chatmemo.ui.utils.toLocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DataBaseRepositoryImp : DataBaseRepository {

    @Suppress("JAVA_CLASS_ON_COMPANION")
    companion object {
        const val TAG = "DataBaseRepositoryImp"
    }

    private val commentDao = MyApplication.database.commentDao()
    private val phraseDao = MyApplication.database.phraseDao()
    private val roomDao = MyApplication.database.roomDao()
    private val templateDao = MyApplication.database.templateDao()

    // region コメント

    // コメント登録
    override suspend fun addComment(comment: Comment, roomId: RoomId) {
        return withContext(Dispatchers.IO) {
            val message = comment.message
            val user = comment.user.chageInt()
            val date = comment.time.toDataBaseDate()
            val roomIdLong = roomId.value.toLong()
            val commentEntity = CommentEntity(null, message, user, date, roomIdLong)
            return@withContext commentDao.insert(commentEntity)
        }
    }

    // コメント削除
    override suspend fun deleteCommentByRoomId(roomId: RoomId) {
        withContext(Dispatchers.IO) {
            val id = roomId.value.toLong()
            commentDao.deleteById(id)
        }
    }

    // コメント更新
    override suspend fun updateComment(comments: List<Comment>, roomId: RoomId) {
        withContext(Dispatchers.IO) {
            comments.forEach {
                val user = it.user.chageInt()
                val commentDate = it.time.toDataBaseDate()
                commentDao.updateUserBy(user, commentDate)
            }
        }
    }

    // endregion

    // region 定型文

    override suspend fun getNextTemplateId(): TemplateId {
        val id = templateDao.getNextId() ?: 0
        return TemplateId(id.toInt() + 1)
    }

    // テンプレート登録
    override suspend fun createTemplate(template: Template): Boolean {
        return withContext(Dispatchers.IO) {
            val templateId = template.templateId.value.toLong()
            val templateTitle = template.title
            val templateEntity = TemplateEntity(null, templateTitle)
            templateDao.insert(templateEntity)
            template.templateMessageList.forEach {
                val phraseTitle = it.massage
                val phrase = PhraseEntity(null, phraseTitle, templateId)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    // テンプレート更新
    override suspend fun updateTemplate(template: Template): Boolean {
        return withContext(Dispatchers.IO) {
            val templateId = template.templateId.value.toLong()
            val templateTitle = template.title
            val templateEntity = TemplateEntity(templateId, templateTitle)
            templateDao.update(templateEntity)
            phraseDao.deleteAllByTemplateId(templateId)
            template.templateMessageList.forEach {
                val phraseTitle = it.massage
                val phrase = PhraseEntity(null, phraseTitle, templateId)
                phraseDao.insert(phrase)
            }
            return@withContext true
        }
    }

    // テンプレート削除
    override suspend fun deleteTemplateTitle(template: Template) {
        withContext(Dispatchers.IO) {
            val title = template.title
            val id = template.templateId.value.toLong()
            val templateEntity = TemplateEntity(id, title)
            templateDao.delete(templateEntity)
        }
    }

    // タイトルに紐づくテンプレートタイトル取得
    override suspend fun getTemplateByTitle(title: String): TemplateEntity {
        return withContext(Dispatchers.IO) {
            return@withContext templateDao.getAllByTitle(title)[0]
        }
    }

    // タイトルに紐づくテンプレートタイトル取得
    override suspend fun getTemplateById(templateId: TemplateId): TemplateEntity {
        return withContext(Dispatchers.IO) {
            val id = templateId.value.toLong()
            return@withContext templateDao.getTemplateById(id)
        }
    }

    // テンプレートタイトル全取得
    override fun getPhraseTitle(): LiveData<List<Template>> {
        val templateListLiveData = templateDao.getAll()
        val templateList = MutableLiveData<List<Template>>()
        templateListLiveData.observeForever {
            val list = it.map { templateEntity ->
                Template(TemplateId(templateEntity.id!!.toInt()), templateEntity.title, listOf())
            }
            templateList.postValue(list)
        }
        return templateList
    }

    override suspend fun deletePhraseByTitle(templateId: TemplateId): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                phraseDao.deleteById(templateId.value.toLong())
            } catch (e: Exception) {
                Log.e(TAG, "Failure :$e")
                return@withContext false
            }
            return@withContext true
        }
    }

    // タイトルに紐づいた定型文取得
    override suspend fun getPhraseByTitle(templateId: TemplateId): List<TemplateMessage> {
        return withContext(Dispatchers.IO) {
            val id = templateId.value.toLong()
            return@withContext phraseDao.getAllByTitle(id).map {
                val message = it.text
                TemplateMessage(message)
            }
        }
    }

    // endregion

    // region ルーム

    // 次の連番を返す
    override suspend fun getNextRoomId(): RoomId {
        val id = roomDao.getNextId() ?: 0
        return RoomId(id.toInt() + 1)
    }

    // ルーム作成
    override suspend fun createRoom(chatRoom: ChatRoom) {
        val id = chatRoom.roomId.value.toLong()
        val title = chatRoom.title
        val templateId: Long?
        val point: String?
        val mode: Int?
        val templateConfiguration = chatRoom.templateConfiguration
        if (templateConfiguration != null) {
            templateId = templateConfiguration.template.templateId.value.toLong()
            point = when (val templateMode = templateConfiguration.templateMode) {
                is TemplateMode.Order  -> {
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
        withContext(Dispatchers.IO) {
            roomDao.insert(chatRoomEntity)
        }
    }

    // ルーム更新
    override suspend fun updateRoom(chatRoom: ChatRoom) {
        return withContext(Dispatchers.IO) {
            val id = chatRoom.roomId.value.toLong()
            val title = chatRoom.title
            val templateId: Long?
            val point: String?
            val mode: Int?
            val templateConfiguration = chatRoom.templateConfiguration
            if (templateConfiguration != null) {
                templateId = templateConfiguration.template.templateId.value.toLong()
                point = when (val templateMode = templateConfiguration.templateMode) {
                    is TemplateMode.Order  -> {
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

            val chatRoomEntity = ChatRoomEntity(
                id, title, templateId, mode, point, commentLast, commentLastTime
            )
            return@withContext roomDao.update(chatRoomEntity)
        }
    }

    // ルーム削除
    override suspend fun deleteRoom(roomId: RoomId) {
        withContext(Dispatchers.IO) {
            val id = roomId.value.toLong()
            roomDao.deleteById(id)
        }
    }

    // ルーム取得
    override suspend fun getRoomByTitle(title: String): ChatRoomEntity {
        return withContext(Dispatchers.IO) {
            val rooms = roomDao.getRoomByTitle(title)
            return@withContext rooms[0]
        }
    }

    // Idに紐づくルーム取得
    override fun getRoomById(roomId: RoomId): LiveData<ChatRoom> {
        val chatRoomEntity = roomDao.getRoomById(roomId.value.toLong())
        val chatRoom = MutableLiveData<ChatRoom>()
        chatRoomEntity.observeForever {
            val roomTitle = it.title
            val commentList: MutableList<Comment> = runBlocking { commentDao.getAllCommentByRoom(it.id!!) }.map { commentEntity ->
                val message = commentEntity.text
                val user = User.getUser(commentEntity.user)
                val commentDate = CommentDateTime(commentEntity.createdAt.toLocalDateTime())
                return@map Comment(message, user, commentDate)
            }.toMutableList()
            val templateConfiguration = if (it.templateId != null) {
                val templateHeader = runBlocking { templateDao.getTemplateById(it.templateId!!) }
                val templatePhrase = runBlocking {
                    phraseDao.getAllByTitle(templateHeader.id!!).map { TemplateMessage(it.text) }
                }
                val template = Template(
                    TemplateId(templateHeader.id!!.toInt()), templateHeader.title, templatePhrase
                )
                val templateMode = TemplateMode.toStatus(it.templateMode!!)
                TemplateConfiguration(template, templateMode)
            } else {
                null
            }
            chatRoom.postValue(ChatRoom(roomId, roomTitle, templateConfiguration, commentList))
        }
        return chatRoom
    }

    // ルーム全取得
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

    // 指定したテンプレートが使用されているルーム取得
    override suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext roomDao.getRoomByTemplateId(templateId)
        }
    }

    // endregion

}
