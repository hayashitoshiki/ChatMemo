[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [ChatViewModel](./index.md)

# ChatViewModel

`class ChatViewModel : ViewModel`

チャット画面_UIロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ChatViewModel(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>チャット画面_UIロジック |

### Properties

| Name | Summary |
|---|---|
| [chatRoom](chat-room.md) | `val chatRoom: LiveData<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>` |
| [commentList](comment-list.md) | `val commentList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`>>` |
| [commentText](comment-text.md) | `val commentText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [changeSubmitButton](change-submit-button.md) | `fun changeSubmitButton(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [changeUser](change-user.md) | `fun changeUser(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [deleteRoom](delete-room.md) | `fun deleteRoom(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [submit](submit.md) | `fun submit(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [updateRoom](update-room.md) | `fun updateRoom(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
