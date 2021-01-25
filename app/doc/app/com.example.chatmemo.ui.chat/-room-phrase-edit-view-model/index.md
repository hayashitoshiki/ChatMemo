[app](../../index.md) / [com.example.chatmemo.ui.chat](../index.md) / [RoomPhraseEditViewModel](./index.md)

# RoomPhraseEditViewModel

`class RoomPhraseEditViewModel : ViewModel`

ルームの定型文設定変更ダイアログ_ロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoomPhraseEditViewModel(chatRoom: `[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`, dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>ルームの定型文設定変更ダイアログ_ロジック |

### Properties

| Name | Summary |
|---|---|
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [modeValueInt](mode-value-int.md) | `val modeValueInt: MutableLiveData<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |
| [roomTitleList](room-title-list.md) | `val roomTitleList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>>` |
| [roomTitleValueInt](room-title-value-int.md) | `val roomTitleValueInt: MutableLiveData<`[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [checkedChangeMode](checked-change-mode.md) | `fun checkedChangeMode(checkedId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [submit](submit.md) | `suspend fun submit(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [validate](validate.md) | `fun validate(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
