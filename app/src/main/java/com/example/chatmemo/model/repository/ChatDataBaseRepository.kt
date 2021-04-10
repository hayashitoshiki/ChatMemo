package com.example.chatmemo.model.repository

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId

/**
 * チャットに関するDataBaseCRUD用Repository
 */
interface ChatDataBaseRepository {
    // region ルーム
    /**
     * 次のID連番の値を返す
     * @return チャットルームDBのの次の連番
     */
    suspend fun getNextRoomId(): RoomId

    /**
     * 新規ルーム作成
     * @param chatRoom 追加するルーム
     */
    suspend fun createRoom(chatRoom: ChatRoom)

    /**
     * チャットルーム削除
     * @param roomId 削除するルームID
     */
    suspend fun deleteRoom(roomId: RoomId)

    /**
     * チャットルーム更新
     * @param chatRoom 更新するルーム
     */
    suspend fun updateRoom(chatRoom: ChatRoom)

    /**
     * チャットルームリスト取得
     * @return 全ルームリスト
     */
    fun getRoomAll(): LiveData<List<ChatRoom>>

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
    suspend fun getRoomByTemplateId(templateId: Long): List<ChatRoom>

    /**
     * メッセージ追加
     */
    suspend fun addComment(comment: Comment, roomId: RoomId)

    /**
     * メッセージアップデート
     */
    suspend fun updateComments(commentList: List<Comment>)

    // endregion

}