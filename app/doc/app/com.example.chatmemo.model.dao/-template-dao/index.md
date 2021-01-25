[app](../../index.md) / [com.example.chatmemo.model.dao](../index.md) / [TemplateDao](./index.md)

# TemplateDao

`interface TemplateDao`

定型文用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `abstract suspend fun delete(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAll](get-all.md) | `abstract fun getAll(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`>` |
| [getAllByTitle](get-all-by-title.md) | `abstract suspend fun getAllByTitle(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`>` |
| [getTemplateById](get-template-by-id.md) | `abstract suspend fun getTemplateById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md) |
| [insert](insert.md) | `abstract suspend fun insert(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract suspend fun update(template: `[`Template`](../../com.example.chatmemo.model.entity/-template/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
