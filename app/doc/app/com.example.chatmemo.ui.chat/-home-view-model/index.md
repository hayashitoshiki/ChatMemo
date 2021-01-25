[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [HomeViewModel](./index.md)

# HomeViewModel

`class HomeViewModel : ViewModel`

ホーム画面_ロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HomeViewModel(dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>ホーム画面_ロジック |

### Properties

| Name | Summary |
|---|---|
| [chatRoomList](chat-room-list.md) | `val chatRoomList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>>` |

### Functions

| Name | Summary |
|---|---|
| [deleteRoom](delete-room.md) | `fun deleteRoom(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
