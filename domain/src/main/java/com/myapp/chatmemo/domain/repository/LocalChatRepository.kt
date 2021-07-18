package com.myapp.chatmemo.domain.repository

import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.TemplateId
import kotlinx.coroutines.flow.Flow

/**
 * チャットに関するローカルデータCRUD用Repository
 */
interface LocalChatRepository {

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
    fun getRoomAll(): Flow<List<ChatRoom>>

    /**
     * RoomIdに一致するルーム取得
     * @param roomId 取得するルームID
     * @return RoomIdに紐づいたルーム
     */
    fun getRoomById(roomId: RoomId): Flow<ChatRoom>

    /**
     * 指定したテンプレートが使用されているルーム取得
     * @param templateId テンプレートID
     * @return 指定したテンプレートが使用されているルーム
     */
    suspend fun getRoomByTemplateId(templateId: TemplateId): List<ChatRoom>

    /**
     * メッセージ追加
     * @param comment コメント
     * @param roomId チャットルームID
     */
    suspend fun addComment(
        comment: Comment,
        roomId: RoomId
    )

    /**
     * メッセージアップデート
     * @param commentList コメントリスト
     */
    suspend fun updateComments(commentList: List<Comment>)

    // endregion
}
