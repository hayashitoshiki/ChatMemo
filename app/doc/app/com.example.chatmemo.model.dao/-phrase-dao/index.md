[app](../../index.md) / [com.example.chatmemo.model.dao](../index.md) / [PhraseDao](./index.md)

# PhraseDao

`interface PhraseDao`

定型文用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `abstract fun delete(comment: `[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [deleteAll](delete-all.md) | `abstract fun deleteAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [deleteById](delete-by-id.md) | `abstract fun deleteById(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAll](get-all.md) | `abstract fun getAll(): `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>` |
| [getAllByTitle](get-all-by-title.md) | `abstract suspend fun getAllByTitle(templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`>` |
| [insert](insert.md) | `abstract suspend fun insert(comment: `[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract suspend fun update(comment: `[`Phrase`](../../com.example.chatmemo.model.entity/-phrase/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
