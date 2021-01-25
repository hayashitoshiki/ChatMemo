[app](../../index.md) / [com.example.chatmemo.ui.phrase](../index.md) / [FixedPhraseAddViewModel](./index.md)

# FixedPhraseAddViewModel

`class FixedPhraseAddViewModel : ViewModel`

定型文作成画面_UIロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `FixedPhraseAddViewModel(dataBaseRepository: `[`DataBaseRepository`](../../com.example.chatmemo.model.repository/-data-base-repository/index.md)`)`<br>定型文作成画面_UIロジック |

### Properties

| Name | Summary |
|---|---|
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [isPhraseEnableSubmitButton](is-phrase-enable-submit-button.md) | `val isPhraseEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [phraseList](phrase-list.md) | `val phraseList: LiveData<<ERROR CLASS><`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>>` |
| [phraseText](phrase-text.md) | `val phraseText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [submitState](submit-state.md) | `val submitState: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [submitText](submit-text.md) | `val submitText: LiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [titleText](title-text.md) | `val titleText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [addPhrase](add-phrase.md) | `fun addPhrase(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [changePhraseSubmitButton](change-phrase-submit-button.md) | `fun changePhraseSubmitButton(phrase: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [changeSubmitButton](change-submit-button.md) | `fun changeSubmitButton(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [init](init.md) | `fun init(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removePhraseList](remove-phrase-list.md) | `fun removePhraseList(position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [submit](submit.md) | `fun submit(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
