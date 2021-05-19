[document](../index.md) / [com.myapp.chatmemo.presentation.utils.expansion](./index.md)

## Package com.myapp.chatmemo.presentation.utils.expansion

UI用拡張クラス定義

### Types

| Name | Summary |
|---|---|
| [BaseViewModel](-base-view-model/index.md) | `abstract class BaseViewModel : ViewModel`<br>BaseViewModel |
| [ViewModelLiveData](-view-model-live-data/index.md) | `open class ViewModelLiveData<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : LiveData<`[`T`](-view-model-live-data/index.md#T)`>`<br>カスタムLiveData カプセル化のためJavaクラスで生成 |

### Properties

| Name | Summary |
|---|---|
| [firsText](firs-text.md) | `val `[`Template`](../com.myapp.chatmemo.domain.model.entity/-template/index.md)`.firsText: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [text](text.md) | `val `[`TemplateMode`](../com.myapp.chatmemo.domain.model.value/-template-mode/index.md)`.text: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [setText](set-text.md) | `fun setText(view: `[`AutoCompleteTextView`](https://developer.android.com/reference/android/widget/AutoCompleteTextView.html)`, text: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
