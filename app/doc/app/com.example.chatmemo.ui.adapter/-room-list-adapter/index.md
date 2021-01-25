[app](../../index.md) / [com.example.chatmemo.ui.adapter](../index.md) / [RoomListAdapter](./index.md)

# RoomListAdapter

`class RoomListAdapter : Adapter<`[`ViewHolder`](-view-holder/index.md)`>`

ルームリスト用アダプター

### Types

| Name | Summary |
|---|---|
| [OnItemClickListener](-on-item-click-listener/index.md) | `interface OnItemClickListener` |
| [ViewHolder](-view-holder/index.md) | `class ViewHolder : ViewHolder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `RoomListAdapter(items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>)`<br>ルームリスト用アダプター |

### Functions

| Name | Summary |
|---|---|
| [getItemCount](get-item-count.md) | `fun getItemCount(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [onBindViewHolder](on-bind-view-holder.md) | `fun onBindViewHolder(holder: `[`ViewHolder`](-view-holder/index.md)`, position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onCreateViewHolder](on-create-view-holder.md) | `fun onCreateViewHolder(parent: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`, viewType: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`ViewHolder`](-view-holder/index.md) |
| [setData](set-data.md) | `fun setData(items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ChatRoom`](../../com.example.chatmemo.model.entity/-chat-room/index.md)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [setOnItemClickListener](set-on-item-click-listener.md) | `fun setOnItemClickListener(listener: `[`OnItemClickListener`](-on-item-click-listener/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
