package com.example.chatmemo.domain.usecase

import androidx.lifecycle.LiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.value.Comment
import com.example.chatmemo.domain.value.RoomId
import com.example.chatmemo.domain.value.TemplateConfiguration

/**
 * チャットルームに関するビジネスロジック
 */
interface ChatUseCase {

    /**
     * チャットルーム作成
     * @property chatRoom 作成するチャットルーム
     */
    suspend fun createRoom(chatRoom: ChatRoom)

    /**
     * チャットルーム削除
     * @property roomId 削除するチャットルームのID
     */
    suspend fun deleteRoom(roomId: RoomId)

    /**
     * チャットルームアップデート
     * @property chatRoom 更新するチャットルーム
     */
    suspend fun updateRoom(chatRoom: ChatRoom)

    /**
     * 全てのチャットルーム取得
     * @return 全てのチャットルーム
     */
    fun getRoomAll(): LiveData<List<ChatRoom>>

    /**
     * RoomIDに紐づくチャットルーム取得
     * @property roomId 取得するチャットルームのID
     * @return IDni紐づくチャットルーム
     */
    fun getChatRoomByRoomById(roomId: RoomId): LiveData<ChatRoom>

    /**
     * 次のチャットルームの連番取得
     * @return 次のチャットルームの連弾
     */
    suspend fun getNextRoomId(): RoomId

    /**
     * チャットルームのコメントを全て反転する
     * @property commentList 変更するコメントリスト
     * @return ユーザを反転させたコメントリスト
     */
    suspend fun reverseAllCommentUser(commentList: List<Comment>): List<Comment>

    /**
     * テンプレートメッセージ送信
     * @property templateConfiguration チャットルームに設定しいるテンプレートの詳細設定
     * @property roomId チャットルームのID
     * @return テンプレートメッセージ出力後のテンプレートの詳細設定とテンプレートコメント
     */
    suspend fun addTemplateComment(
        templateConfiguration: TemplateConfiguration, roomId: RoomId
    ): Pair<TemplateConfiguration, Comment>

    /**
     * コメント送信
     * @property message 入力したメッセージ
     * @property roomId チャットルームのID
     * @return 送信されたコメント
     */
    suspend fun addComment(message: String, roomId: RoomId): Comment
}