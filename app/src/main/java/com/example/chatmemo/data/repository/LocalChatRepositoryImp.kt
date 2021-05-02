package com.example.chatmemo.data.repository

import com.example.chatmemo.data.local.database.dao.CommentDao
import com.example.chatmemo.data.local.database.dao.PhraseDao
import com.example.chatmemo.data.local.database.dao.RoomDao
import com.example.chatmemo.data.local.database.dao.TemplateDao
import com.example.chatmemo.data.local.database.entity.ChatRoomEntity
import com.example.chatmemo.data.local.database.entity.CommentEntity
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.value.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalChatRepositoryImp(
    private val roomDao: RoomDao,
    private val commentDao: CommentDao,
    private val templateDao: TemplateDao,
    private val phraseDao: PhraseDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalChatRepository {

    // 次のチャットルームID連番の値を返す
    override suspend fun getNextRoomId(): RoomId {
        return withContext(ioDispatcher) {
            val id = roomDao.getNextId() ?: 0
            return@withContext RoomId(id.toInt() + 1)
        }
    }

    // チャットルーム作成
    override suspend fun createRoom(chatRoom: ChatRoom) {
        withContext(ioDispatcher) {
            val chatRoomEntity = Converter.chatEntityFromChat(chatRoom)
            roomDao.insert(chatRoomEntity)
        }
    }

    // チャットルーム削除
    override suspend fun deleteRoom(roomId: RoomId) {
        withContext(ioDispatcher) {
            val id = roomId.value.toLong()
            roomDao.deleteById(id)
            commentDao.deleteById(id)
        }
    }

    // チャットルーム更新
    override suspend fun updateRoom(chatRoom: ChatRoom) {
        return withContext(ioDispatcher) {
            val chatRoomEntity = Converter.chatEntityFromChat(chatRoom)
            return@withContext roomDao.update(chatRoomEntity)
        }
    }

    // 全チャットルーム種奥
    override fun getRoomAll(): Flow<List<ChatRoom>> {
        return roomDao.getAll().filterNotNull().map { chatRoomEntitiyList ->
            chatRoomEntitiyList.map { chatRoomEntity ->
                chatRoomFromChatRoomEntity(chatRoomEntity)
            }
        }
    }

    // ルームIDに紐づくチャットルーム取得
    override fun getRoomById(roomId: RoomId): Flow<ChatRoom> {
        return roomDao.getRoomById(roomId.value.toLong()).filterNotNull().map { chatRoomEntity ->
            chatRoomFromChatRoomEntity(chatRoomEntity)
        }
    }

    // テンプレートIDに紐づくチャットルーム取得
    override suspend fun getRoomByTemplateId(templateId: TemplateId): List<ChatRoom> {
        return withContext(ioDispatcher) {
            return@withContext roomDao.getRoomByTemplateId(templateId.value.toLong()).map { chatRoomEntity ->
                chatRoomFromChatRoomEntity(chatRoomEntity)
            }
        }
    }

    // コメント追加
    override suspend fun addComment(comment: Comment, roomId: RoomId) {
        return withContext(ioDispatcher) {
            val commentEntity = Converter.commentEntityFromComment(comment, roomId)
            return@withContext commentDao.insert(commentEntity)
        }
    }

    // コメント更新
    override suspend fun updateComments(commentList: List<Comment>) {
        withContext(ioDispatcher) {
            commentList.forEach {
                val user = it.user.chageInt()
                val commentDate = it.time.toDataBaseDate()
                commentDao.updateUserBy(user, commentDate)
            }
        }
    }

    // チャットルームEntitiyからチャットルームオブジェクトへ変換
    private suspend fun chatRoomFromChatRoomEntity(chatRoomEntity: ChatRoomEntity): ChatRoom {
        val commentList: List<CommentEntity> = commentDao.getAllCommentByRoom(chatRoomEntity.id!!)
        val templateTitle = if (chatRoomEntity.templateId != null) {
            templateDao.getTemplateById(chatRoomEntity.templateId!!)
        } else {
            null
        }
        val templatePhrase = if (templateTitle != null) {
            phraseDao.getAllByTitle(templateTitle.id!!)
        } else {
            null
        }
        return Converter.chatFromBy(chatRoomEntity, commentList, templateTitle, templatePhrase)
    }
}
