[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [RoomAddViewModel](./index.md)

# RoomAddViewModel

`class RoomAddViewModel : ViewModel`

新規ルーム作成画面_ロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoomAddViewModel(dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>新規ルーム作成画面_ロジック |

### Properties

| Name | Summary |
|---|---|
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [modeValueInt](mode-value-int.md) | `val modeValueInt: MutableLiveData<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
| [phraseTitleList](phrase-title-list.md) | `val phraseTitleList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |
| [phraseTitleValueInt](phrase-title-value-int.md) | `val phraseTitleValueInt: MutableLiveData<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
| [titleText](title-text.md) | `val titleText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [changeSubmitButton](change-submit-button.md) | `fun changeSubmitButton(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [checkedChangeMode](checked-change-mode.md) | `fun checkedChangeMode(checkedId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [createRoom](create-room.md) | `suspend fun createRoom(): `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md) |
