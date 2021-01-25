[app](../../index.md) / [com.example.chatmemo.ui.phrase](../index.md) / [FixedPhraseListViewModel](./index.md)

# FixedPhraseListViewModel

`class FixedPhraseListViewModel : ViewModel`

定型文一覧画面_UIロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `FixedPhraseListViewModel(dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>定型文一覧画面_UIロジック |

### Properties

| Name | Summary |
|---|---|
| [phraseList](phrase-list.md) | `val phraseList: LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`>>` |
| [status](status.md) | `val status: MutableLiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [deletePhrase](delete-phrase.md) | `fun deletePhrase(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getList](get-list.md) | `fun getList(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
