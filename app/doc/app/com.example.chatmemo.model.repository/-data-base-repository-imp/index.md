[app](../../index.md) / [com.example.chatmemo.model.repository](../index.md) / [DataBaseRepositoryImp](./index.md)

# DataBaseRepositoryImp

`class DataBaseRepositoryImp : `[`DataBaseRepository`](../-data-base-repository/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DataBaseRepositoryImp()` |

### Functions

| Name | Summary |
|---|---|
| [addComment](add-comment.md) | `suspend fun addComment(comment: `[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>コメント追加 |
| [addPhrase](add-phrase.md) | `suspend fun addPhrase(phraseList: <ERROR CLASS><`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文登録 |
| [createRoom](create-room.md) | `suspend fun createRoom(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>新規ルーム作成 |
| [createTemplate](create-template.md) | `suspend fun createTemplate(templateTitle: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文登録 |
| [deleteCommentByRoomId](delete-comment-by-room-id.md) | `suspend fun deleteCommentByRoomId(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>roomIdに関連するコメント削除 |
| [deletePhraseByTitle](delete-phrase-by-title.md) | `suspend fun deletePhraseByTitle(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>指定の定型文削除 |
| [deleteRoom](delete-room.md) | `suspend fun deleteRoom(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>チャットルーム削除 |
| [deleteTemplateTitle](delete-template-title.md) | `suspend fun deleteTemplateTitle(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>指定の定型文削除 |
| [getCommentAll](get-comment-all.md) | `suspend fun getCommentAll(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`>`<br>ルームIDに関連するコメント全取得 |
| [getPhraseByTitle](get-phrase-by-title.md) | `suspend fun getPhraseByTitle(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>`<br>タイトルに紐づいた定型文リスト取得 |
| [getPhraseTitle](get-phrase-title.md) | `suspend fun getPhraseTitle(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`>`<br>定型文のタイトル一覧取得 return 定型文のタイトル一覧 |
| [getRoomAll](get-room-all.md) | `fun getRoomAll(): LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>>`<br>チャットルームリスト取得 |
| [getRoomById](get-room-by-id.md) | `fun getRoomById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): LiveData<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>` |
| [getRoomByTemplateId](get-room-by-template-id.md) | `suspend fun getRoomByTemplateId(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>`<br>指定したテンプレートが使用されているルーム取得 |
| [getRoomByTitle](get-room-by-title.md) | `suspend fun getRoomByTitle(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)<br>ルーム取得 |
| [getTemplateById](get-template-by-id.md) | `suspend fun getTemplateById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)<br>Idに紐づく定型文取得 |
| [getTemplateByTitle](get-template-by-title.md) | `suspend fun getTemplateByTitle(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)<br>タイトルに紐づいた定型文取得 |
| [updateComment](update-comment.md) | `suspend fun updateComment(comments: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>コメント更新 |
| [updatePhrase](update-phrase.md) | `suspend fun updatePhrase(phraseList: <ERROR CLASS><`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>, templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文更新 |
| [updateRoom](update-room.md) | `suspend fun updateRoom(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>チャットルーム更新 |
| [updateTemplate](update-template.md) | `suspend fun updateTemplate(templateTitle: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文更新 |

### Companion Object Properties

| Name | Summary |
|---|---|
| [TAG](-t-a-g.md) | `const val TAG: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
