[app](../../index.md) / [com.example.chatmemo.data.local.database.dao](../index.md) / [TemplateDao](./index.md)

# TemplateDao

`interface TemplateDao`

定型文用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `abstract suspend fun delete(template: `[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [deleteByTemplateId](delete-by-template-id.md) | `abstract suspend fun deleteByTemplateId(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAll](get-all.md) | `abstract fun getAll(): LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md)`>>` |
| [getAllByTitle](get-all-by-title.md) | `abstract suspend fun getAllByTitle(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md)`>` |
| [getNextId](get-next-id.md) | `abstract suspend fun getNextId(): `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [getTemplateById](get-template-by-id.md) | `abstract suspend fun getTemplateById(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md) |
| [insert](insert.md) | `abstract suspend fun insert(template: `[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract suspend fun update(template: `[`TemplateEntity`](../../com.example.chatmemo.data.database.entity/-template-entity/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
