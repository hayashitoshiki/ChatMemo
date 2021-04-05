package com.example.chatmemo.model.repository

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.CommentEntity
import com.example.chatmemo.model.entity.PhraseEntity
import com.example.chatmemo.model.entity.TemplateEntity

/**
 * ローカルDBへのアーティスト情報関連のRepository
 *
 */
interface DataBaseRepository {

    // region コメント
    /**
     * コメント追加
     *
     * @param comment 追加するコメント
     */
    suspend fun addComment(comment: Comment, roomId: RoomId)

    /**
     * コメント更新
     *
     * @param comments 更新するコメント
     */
    suspend fun updateComment(comments: List<Comment>, roomId: RoomId)

    /**
     * roomIdに関連するコメント削除
     */
    suspend fun deleteCommentByRoomId(roomId: Long)

    /**
     * ルームIDに関連するコメント全取得
     * @param roomId ルームID
     * @return 指定したルームのチャットリスト
     */
    suspend fun getCommentAll(roomId: Long): List<CommentEntity>

    // endregion

    // region 定型文タイトル
    /**
     * 定型文登録
     * @param templateTitle 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun createTemplate(templateTitle: TemplateEntity): Boolean

    /**
     * 定型文更新
     * @param templateTitle 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun updateTemplate(templateTitle: TemplateEntity): Boolean

    /**
     * 指定の定型文削除
     * @param template 消したい定型文リストのタイトル
     * return 正常に削除できたか
     */
    suspend fun deleteTemplateTitle(template: TemplateEntity)

    /**
     * 定型文のタイトル一覧取得
     * return 定型文のタイトル一覧
     */
    suspend fun getPhraseTitle(): List<Template>

    /**
     * タイトルに紐づいた定型文取得
     */
    suspend fun getTemplateByTitle(title: String): TemplateEntity

    /**
     * Idに紐づく定型文取得
     */
    suspend fun getTemplateById(id: Long): TemplateEntity
    // endregion

    // region 定型文
    /**
     * 定型文登録
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun addPhrase(phraseList: ArrayList<PhraseEntity>): Boolean

    /**
     * 定型文更新
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun updatePhrase(phraseList: ArrayList<PhraseEntity>, templateId: Long): Boolean

    /**
     * 指定の定型文削除
     * @param templateId 消したい定型文リストのタイトル
     * return 正常に削除できたか
     */
    suspend fun deletePhraseByTitle(templateId: Long): Boolean

    /**
     * タイトルに紐づいた定型文リスト取得
     */
    suspend fun getPhraseByTitle(templateId: Long): List<PhraseEntity>

    // endregion

    // region ルーム
    /**
     * 次のID連番の値を返す
     * @return チャットルームDBのの次の連番
     */
    suspend fun getNextId(): RoomId

    /**
     * 新規ルーム作成
     * @param chatRoom 追加するルーム
     */
    suspend fun createRoom(chatRoom: ChatRoom)

    /**
     * チャットルーム削除
     * @param id 削除するルームID
     */
    suspend fun deleteRoom(id: Long)

    /**
     * チャットルーム更新
     * @param chatRoomEntity 更新するルーム
     */
    suspend fun updateRoom(chatRoomEntity: ChatRoom)

    /**
     * ルーム取得
     * @param title 取得するルームのタイトル
     * @return タイトルに紐づいたルーム
     */
    suspend fun getRoomByTitle(title: String): ChatRoomEntity

    /**
     * RoomIdに一致するルーム取得
     * @param roomId 取得するルームID
     * @return RoomIdに紐づいたルーム
     */
    fun getRoomById(roomId: RoomId): LiveData<ChatRoom>

    /**
     * 指定したテンプレートが使用されているルーム取得
     * @param templateId テンプレートID
     * @return 指定したテンプレートが使用されているルーム
     */
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoomEntity>

    /**
     * チャットルームリスト取得
     * @return 全ルームリスト
     */
    fun getRoomAll(): LiveData<List<ChatRoom>>
    // endregion

}
