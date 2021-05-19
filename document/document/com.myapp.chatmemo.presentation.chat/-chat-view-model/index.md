[document](../../index.md) / [com.myapp.chatmemo.presentation.chat](../index.md) / [ChatViewModel](./index.md)

# ChatViewModel

`class ChatViewModel : `[`BaseViewModel`](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/index.md)

チャット画面_UIロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ChatViewModel(id: `[`RoomId`](../../com.myapp.chatmemo.domain.model.value/-room-id/index.md)`, chatUseCase: `[`ChatUseCase`](../../com.myapp.chatmemo.domain.usecase/-chat-use-case/index.md)`)`<br>チャット画面_UIロジック |

### Properties

| Name | Summary |
|---|---|
| [chatRoom](chat-room.md) | `val chatRoom: <ERROR CLASS>` |
| [commentList](comment-list.md) | `val commentList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.myapp.chatmemo.domain.model.value/-comment/index.md)`>>` |
| [commentText](comment-text.md) | `val commentText: <ERROR CLASS>` |
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [changeUser](change-user.md) | `fun changeUser(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [deleteRoom](delete-room.md) | `fun deleteRoom(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [submit](submit.md) | `fun submit(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [postValue](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/post-value.md) | `fun <T> `[`ViewModelLiveData`](../../com.myapp.chatmemo.presentation.utils.expansion/-view-model-live-data/index.md)`<`[`T`](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/post-value.md#T)`>.postValue(value: `[`T`](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/post-value.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setValue](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/set-value.md) | `fun <T> `[`ViewModelLiveData`](../../com.myapp.chatmemo.presentation.utils.expansion/-view-model-live-data/index.md)`<`[`T`](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/set-value.md#T)`>.setValue(value: `[`T`](../../com.myapp.chatmemo.presentation.utils.expansion/-base-view-model/set-value.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
