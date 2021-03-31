package com.example.chatmemo.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.CommentEntity
import com.example.chatmemo.model.entity.PhraseEntity
import com.example.chatmemo.model.entity.TemplateEntity
import com.example.chatmemo.ui.MyApplication
import kotlinx.coroutines.Dispatchers
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
    override suspend fun addComment(comment: CommentEntity) {
        return withContext(Dispatchers.IO) {
            return@withContext commentDao.insert(comment)
        }
    }

    // コメント削除
    override suspend fun deleteCommentByRoomId(roomId: Long) {
        withContext(Dispatchers.IO) {
            commentDao.deleteById(roomId)
        }
    }

    // コメント更新
    override suspend fun updateComment(comments: List<CommentEntity>) {
        withContext(Dispatchers.IO) {
            comments.forEach {
                commentDao.update(it)
            }
        }
    }

    // 全コメント取得
    override suspend fun getCommentAll(roomId: Long): List<CommentEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext commentDao.getAllCommentByRoom(roomId)
        }
    }

    // endregion

    // region 定型文

    // テンプレートタイトル登録
    override suspend fun createTemplate(templateTitle: TemplateEntity): Boolean {
        return withContext(Dispatchers.IO) {
            var flg = true
            val list = templateDao.getAllByTitle(templateTitle.title)
            list.forEach {
                if (it.title == templateTitle.title) {
                    flg = false
                }
            }
            if (flg) {
                templateDao.insert(templateTitle)
            }
            return@withContext flg
        }
    }

    // テンプレートタイトル登録
    override suspend fun updateTemplate(templateTitle: TemplateEntity): Boolean {
        return withContext(Dispatchers.IO) {
            templateDao.update(templateTitle)
            return@withContext true
        }
    }

    // テンプレート削除
    override suspend fun deleteTemplateTitle(template: TemplateEntity) {
        withContext(Dispatchers.IO) {
            templateDao.delete(template)
        }
    }

    // タイトルに紐づくテンプレートタイトル取得
    override suspend fun getTemplateByTitle(title: String): TemplateEntity {
        return withContext(Dispatchers.IO) {
            return@withContext templateDao.getAllByTitle(title)[0]
        }
    }

    // タイトルに紐づくテンプレートタイトル取得
    override suspend fun getTemplateById(id: Long): TemplateEntity {
        return withContext(Dispatchers.IO) {
            return@withContext templateDao.getTemplateById(id)
        }
    }

    // テンプレートタイトル全取得
    override suspend fun getPhraseTitle(): List<TemplateEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext templateDao.getAll()
        }
    }

    // 定型文登録
    override suspend fun addPhrase(phraseList: ArrayList<PhraseEntity>): Boolean {
        return withContext(Dispatchers.IO) {
            phraseDao.getAll().forEach { p ->
                if (p.templateId == phraseList.first().templateId) {
                    return@withContext false
                }
            }
            phraseList.forEach {
                phraseDao.insert(it)
            }
            return@withContext true
        }
    }

    // コメント更新
    override suspend fun updatePhrase(
        phraseList: ArrayList<PhraseEntity>,
        templateId: Long
    ): Boolean {
        return withContext(Dispatchers.IO) {
            phraseDao.deleteById(templateId)
            phraseList.forEach {
                phraseDao.insert(it)
            }
            return@withContext true
        }
    }

    override suspend fun deletePhraseByTitle(templateId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                phraseDao.deleteById(templateId)
            } catch (e: Exception) {
                Log.e(TAG, "Failure :$e")
                return@withContext false
            }
            return@withContext true
        }
    }

    // タイトルに紐づいた定型文取得
    override suspend fun getPhraseByTitle(templateId: Long): List<PhraseEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext phraseDao.getAllByTitle(templateId)
        }
    }

    // endregion

    // region ルーム

    // ルーム作成
    override suspend fun createRoom(chatRoomEntity: ChatRoomEntity) {
        return withContext(Dispatchers.IO) {
            roomDao.insert(chatRoomEntity)
        }
    }

    // ルーム更新
    override suspend fun updateRoom(chatRoomEntity: ChatRoomEntity) {
        return withContext(Dispatchers.IO) {
            return@withContext roomDao.update(chatRoomEntity)
        }
    }

    // ルーム削除
    override suspend fun deleteRoom(id: Long) {
        withContext(Dispatchers.IO) {
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
    override fun getRoomById(id: Long): LiveData<ChatRoomEntity> {
        return roomDao.getRoomById(id)
    }

    // ルーム全取得
    override fun getRoomAll(): LiveData<List<ChatRoomEntity>> {
        return roomDao.getAll().map { it ->
            it.sortedByDescending { it.commentTime }
        }
    }

    // 指定したテンプレートが使用されているルーム取得
    override suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity> {
        return withContext(Dispatchers.IO) {
            return@withContext roomDao.getRoomByTemplateId(templateId)
        }
    }

    // endregion

}
