package com.example.chatmemo.model.repository

import androidx.lifecycle.LiveData
import com.example.chatmemo.model.entity.Comment
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Template

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
    suspend fun addComment(comment: Comment)

    /**
     * コメント更新
     *
     * @param comments 更新するコメント
     */
    suspend fun updateComment(comments: List<Comment>)

    /**
     * roomIdに関連するコメント削除
     */
    suspend fun deleteCommentByRoomId(roomId: Long)

    /**
     * ルームIDに関連するコメント全取得
     * @param roomId ルームID
     * @return 指定したルームのチャットリスト
     */
    suspend fun getCommentAll(roomId: Long): List<Comment>

    // endregion

    // region 定型文タイトル
    /**
     * 定型文登録
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun createTemplate(templateTitle: Template) : Boolean

    /**
     * 定型文更新
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun updateTemplate(templateTitle: Template) : Boolean

    /**
     * 指定の定型文削除
     * @param template 消したい定型文リストのタイトル
     * return 正常に削除できたか
     */
    suspend fun deleteTemplateTitle(template : Template)

    /**
     * 定型文のタイトル一覧取得
     * return 定型文のタイトル一覧
     */
    suspend fun getPhraseTitle(): List<Template>

    /**
     * タイトルに紐づいた定型文取得
     */
    suspend fun getTemplateByTitle(title: String) : Template

    /**
     * Idに紐づく定型文取得
     */
    suspend fun getTemplateById(id: Long) : Template
    // endregion

    // region 定型文
    /**
     * 定型文登録
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun addPhrase(phraseList: ArrayList<Phrase>) : Boolean

    /**
     * 定型文更新
     * @param phraseList 追加する定型文リスト
     * return 正常登録できたか
     */
    suspend fun updatePhrase(phraseList: ArrayList<Phrase>, templateId :Long) : Boolean

    /**
     * 指定の定型文削除
     * @param templateId 消したい定型文リストのタイトル
     * return 正常に削除できたか
     */
    suspend fun deletePhraseByTitle(templateId :Long): Boolean

    /**
     * タイトルに紐づいた定型文リスト取得
     */
    suspend fun getPhraseByTitle(templateId: Long) : List<Phrase>

    // endregion

    // region ルーム
    /**
     * 新規ルーム作成
     * @param chatRoom 追加するルーム
     */
    suspend fun createRoom(chatRoom : ChatRoom)

    /**
     * チャットルーム削除
     * @param id 削除するルームID
     */
    suspend fun deleteRoom(id: Long)

    /**
     * チャットルーム更新
     * @param chatRoom 更新するルーム
     */
    suspend fun updateRoom(chatRoom: ChatRoom)

    /**
     * ルーム取得
     * @param title 取得するルームのタイトル
     * @return タイトルに紐づいたルーム
     */
    suspend fun getRoomByTitle(title :String) : ChatRoom

    fun getRoomById(id: Long) : LiveData<ChatRoom>

    /**
     * 指定したテンプレートが使用されているルーム取得
     * @param templateId テンプレートID
     * @return 指定したテンプレートが使用されているルーム
     */
    suspend fun getRoomByTemplateId(templateId: Long) : List<ChatRoom>

    /**
     * チャットルームリスト取得
     * @return 全ルームリスト
     */
    fun getRoomAll() : LiveData<List<ChatRoom>>
    // endregion

}
