[document](../../index.md) / [com.myapp.chatmemo.domain.model.entity](../index.md) / [ChatRoom](./index.md)

# ChatRoom

`data class ChatRoom : `[`Serializable`](https://developer.android.com/reference/java/io/Serializable.html)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ChatRoom(roomId: `[`RoomId`](../../com.myapp.chatmemo.domain.model.value/-room-id/index.md)`, title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, templateConfiguration: `[`TemplateConfiguration`](../../com.myapp.chatmemo.domain.model.value/-template-configuration/index.md)`?, commentList: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`Comment`](../../com.myapp.chatmemo.domain.model.value/-comment/index.md)`>)` |

### Properties

| Name | Summary |
|---|---|
| [commentList](comment-list.md) | `var commentList: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`Comment`](../../com.myapp.chatmemo.domain.model.value/-comment/index.md)`>` |
| [roomId](room-id.md) | `val roomId: `[`RoomId`](../../com.myapp.chatmemo.domain.model.value/-room-id/index.md) |
| [templateConfiguration](template-configuration.md) | `var templateConfiguration: `[`TemplateConfiguration`](../../com.myapp.chatmemo.domain.model.value/-template-configuration/index.md)`?` |
| [title](title.md) | `var title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
