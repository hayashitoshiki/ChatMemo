[app](../../index.md) / [com.example.chatmemo.ui.template](../index.md) / [TempalteAddViewModel](./index.md)

# TempalteAddViewModel

`class TempalteAddViewModel : `[`BaseViewModel`](../../com.example.chatmemo.ui.utils/-base-view-model/index.md)

定型文作成画面_UIロジック

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TempalteAddViewModel(template: `[`Template`](../../com.example.chatmemo.domain.model.entity/-template/index.md)`?, templateUseCase: `[`TemplateUseCase`](../../com.example.chatmemo.domain.usecase/-template-use-case/index.md)`)`<br>定型文作成画面_UIロジック |

### Properties

| Name | Summary |
|---|---|
| [isEnableSubmitButton](is-enable-submit-button.md) | `val isEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [isPhraseEnableSubmitButton](is-phrase-enable-submit-button.md) | `val isPhraseEnableSubmitButton: LiveData<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [phraseList](phrase-list.md) | `val phraseList: `[`ViewModelLiveData`](../../com.example.chatmemo.ui.utils/-view-model-live-data/index.md)`<`[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`TemplateMessage`](../../com.example.chatmemo.domain.model.value/-template-message/index.md)`>>` |
| [phraseText](phrase-text.md) | `val phraseText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [submitState](submit-state.md) | `val submitState: `[`ViewModelLiveData`](../../com.example.chatmemo.ui.utils/-view-model-live-data/index.md)`<`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`>` |
| [submitText](submit-text.md) | `val submitText: `[`ViewModelLiveData`](../../com.example.chatmemo.ui.utils/-view-model-live-data/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [titleText](title-text.md) | `val titleText: MutableLiveData<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [addPhrase](add-phrase.md) | `fun addPhrase(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [submit](submit.md) | `fun submit(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [updatePhraseList](update-phrase-list.md) | `fun updatePhraseList(items: `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`TemplateMessage`](../../com.example.chatmemo.domain.model.value/-template-message/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [postValue](../../com.example.chatmemo.ui.utils/-base-view-model/post-value.md) | `fun <T> `[`ViewModelLiveData`](../../com.example.chatmemo.ui.utils/-view-model-live-data/index.md)`<`[`T`](../../com.example.chatmemo.ui.utils/-base-view-model/post-value.md#T)`>.postValue(value: `[`T`](../../com.example.chatmemo.ui.utils/-base-view-model/post-value.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setValue](../../com.example.chatmemo.ui.utils/-base-view-model/set-value.md) | `fun <T> `[`ViewModelLiveData`](../../com.example.chatmemo.ui.utils/-view-model-live-data/index.md)`<`[`T`](../../com.example.chatmemo.ui.utils/-base-view-model/set-value.md#T)`>.setValue(value: `[`T`](../../com.example.chatmemo.ui.utils/-base-view-model/set-value.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |