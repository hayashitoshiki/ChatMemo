[app](../../index.md) / [com.example.chatmemo.ui.adapter](../index.md) / [PhraseListAdapter](./index.md)

# PhraseListAdapter

`class PhraseListAdapter : Adapter<`[`ViewHolder`](-view-holder/index.md)`>`

定型文作成画面用のリストビューアダプター

### Types

| Name | Summary |
|---|---|
| [OnItemClickListener](-on-item-click-listener/index.md) | `interface OnItemClickListener` |
| [ViewHolder](-view-holder/index.md) | `class ViewHolder : ViewHolder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PhraseListAdapter(items: `[`ArrayList`](https://developer.android.com/reference/java/util/ArrayList.html)`<`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>)`<br>定型文作成画面用のリストビューアダプター |

### Functions

| Name | Summary |
|---|---|
| [getItemCount](get-item-count.md) | `fun getItemCount(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [onBindViewHolder](on-bind-view-holder.md) | `fun onBindViewHolder(holder: `[`ViewHolder`](-view-holder/index.md)`, position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreateViewHolder](on-create-view-holder.md) | `fun onCreateViewHolder(parent: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`, viewType: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ViewHolder`](-view-holder/index.md) |
| [setData](set-data.md) | `fun setData(items: `[`ArrayList`](https://developer.android.com/reference/java/util/ArrayList.html)`<`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setOnItemClickListener](set-on-item-click-listener.md) | `fun setOnItemClickListener(listener: `[`OnItemClickListener`](-on-item-click-listener/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
