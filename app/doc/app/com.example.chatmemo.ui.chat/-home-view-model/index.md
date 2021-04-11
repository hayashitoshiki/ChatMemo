[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [HomeViewModel](./index.md)

# HomeViewModel

`class HomeViewModel : ViewModel`

ホーム画面_ロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HomeViewModel(chatUseCase: `[`ChatUseCase`](../../com.example.chatmemo.domain.usecase/-chat-use-case/index.md)`)`<br>ホーム画面_ロジック |

### Properties

| Name | Summary |
|---|---|
| [chatRoomEntityList](chat-room-entity-list.md) | `val chatRoomEntityList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.domain.model.entity/-chat-room/index.md)`>>` |

### Functions

| Name | Summary |
|---|---|
| [deleteRoom](delete-room.md) | `fun deleteRoom(roomId: `[`RoomId`](../../com.example.chatmemo.domain.model.value/-room-id/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
