[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [RoomTitleEditViewModel](./index.md)

# RoomTitleEditViewModel

`class RoomTitleEditViewModel : ViewModel`

ルーム名変更ダイアログ_ロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoomTitleEditViewModel(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`, dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>ルーム名変更ダイアログ_ロジック |

### Properties

| Name | Summary |
|---|---|
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [newRoomTitle](new-room-title.md) | `val newRoomTitle: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [oldRoomTitle](old-room-title.md) | `val oldRoomTitle: LiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [changeRoomName](change-room-name.md) | `suspend fun changeRoomName(roomName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [changeSubmitButton](change-submit-button.md) | `fun changeSubmitButton(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
