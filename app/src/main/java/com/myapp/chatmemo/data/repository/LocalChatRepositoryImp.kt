package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.local.database.dao.CommentDao
import com.myapp.chatmemo.data.local.database.dao.PhraseDao
import com.myapp.chatmemo.data.local.database.dao.RoomDao
import com.myapp.chatmemo.data.local.database.dao.TemplateDao
import com.myapp.chatmemo.data.local.database.entity.ChatRoomEntity
import com.myapp.chatmemo.data.local.database.entity.CommentEntity
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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
            val newChatRoomEntity = Converter.chatEntityFromChat(chatRoom)
            val oldChatRoomEntity = withContext(Dispatchers.Default) {
                roomDao.getRoomById(chatRoom.roomId.value.toLong())
                    .first()
            }
            oldChatRoomEntity.update(newChatRoomEntity)
            return@withContext roomDao.update(oldChatRoomEntity)
        }
    }

    // 全チャットルーム取得
    override fun getRoomAll(): Flow<List<ChatRoom>> {
        return roomDao.getAll()
            .filterNotNull()
            .map { chatRoomEntitiyList ->
                chatRoomEntitiyList.sortedByDescending { it.updateAt }
                    .map { chatRoomEntity -> chatRoomFromChatRoomEntity(chatRoomEntity) }
            }
    }

    // ルームIDに紐づくチャットルーム取得
    override fun getRoomById(roomId: RoomId): Flow<ChatRoom> {
        return roomDao.getRoomById(roomId.value.toLong())
            .filterNotNull()
            .map { chatRoomEntity ->
                chatRoomFromChatRoomEntity(chatRoomEntity)
            }
    }

    // テンプレートIDに紐づくチャットルーム取得
    override suspend fun getRoomByTemplateId(templateId: TemplateId): List<ChatRoom> {
        return withContext(ioDispatcher) {
            return@withContext roomDao.getRoomByTemplateId(templateId.value.toLong())
                .map { chatRoomEntity ->
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
                val oldComment = commentDao.getCommentByDate(it.time.date)
                val newCommentEntity = Converter.commentEntityFromComment(it, RoomId(oldComment.roomId.toInt()))
                oldComment.update(newCommentEntity)
                commentDao.update(oldComment)
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
