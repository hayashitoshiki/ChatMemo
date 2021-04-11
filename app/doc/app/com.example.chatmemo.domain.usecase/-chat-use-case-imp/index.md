[app](../../index.md) / [com.example.chatmemo.domain.usecase](../index.md) / [ChatUseCaseImp](./index.md)

# ChatUseCaseImp

`class ChatUseCaseImp : `[`ChatUseCase`](../-chat-use-case/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ChatUseCaseImp(chatDataBaseRepository: `[`ChatDataBaseRepository`](../../com.example.chatmemo.data.repository/-chat-data-base-repository/index.md)`)` |

### Functions

| Name | Summary |
|---|---|
| [addComment](add-comment.md) | `suspend fun addComment(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, roomId: `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)`): `[`Comment`](../../com.example.chatmemo.domain.model.value/-comment/index.md)<br>コメント送信 |
| [addTemplateComment](add-template-comment.md) | `suspend fun addTemplateComment(templateConfiguration: `[`TemplateConfiguration`](../../com.example.chatmemo.domain.model.value/-template-configuration/index.md)`, roomId: `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`TemplateConfiguration`](../../com.example.chatmemo.domain.model.value/-template-configuration/index.md)`, `[`Comment`](../../com.example.chatmemo.domain.model.value/-comment/index.md)`>`<br>テンプレートメッセージ送信 |
| [createRoom](create-room.md) | `suspend fun createRoom(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.domain.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>チャットルーム作成 |
| [deleteRoom](delete-room.md) | `suspend fun deleteRoom(roomId: `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>チャットルーム削除 |
| [getChatRoomByRoomById](get-chat-room-by-room-by-id.md) | `fun getChatRoomByRoomById(roomId: `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)`): LiveData<`[`ChatRoom`](../../com.example.chatmemo.domain.model.entity/-chat-room/index.md)`>`<br>RoomIDに紐づくチャットルーム取得 |
| [getNextRoomId](get-next-room-id.md) | `suspend fun getNextRoomId(): `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)<br>次のチャットルームの連番取得 |
| [getRoomAll](get-room-all.md) | `fun getRoomAll(): LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.domain.model.entity/-chat-room/index.md)`>>`<br>全てのチャットルーム取得 |
| [reverseAllCommentUser](reverse-all-comment-user.md) | `suspend fun reverseAllCommentUser(commentList: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.domain.model.value/-comment/index.md)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.domain.model.value/-comment/index.md)`>`<br>チャットルームのコメントを全て反転する |
| [updateRoom](update-room.md) | `suspend fun updateRoom(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.domain.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>チャットルームアップデート |
