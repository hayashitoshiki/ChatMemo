[app](../../index.md) / [com.example.chatmemo.model.dao](../index.md) / [RoomDao](./index.md)

# RoomDao

`interface RoomDao`

定型文用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [deleteById](delete-by-id.md) | `abstract suspend fun deleteById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAll](get-all.md) | `abstract fun getAll(): LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>>` |
| [getRoomById](get-room-by-id.md) | `abstract fun getRoomById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): LiveData<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>` |
| [getRoomByTemplateId](get-room-by-template-id.md) | `abstract suspend fun getRoomByTemplateId(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>` |
| [getRoomByTitle](get-room-by-title.md) | `abstract suspend fun getRoomByTitle(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>` |
| [insert](insert.md) | `abstract suspend fun insert(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract suspend fun update(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
