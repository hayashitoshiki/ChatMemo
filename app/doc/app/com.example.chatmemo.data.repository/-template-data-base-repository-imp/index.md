[app](../../index.md) / [com.example.chatmemo.data.repository](../index.md) / [TemplateDataBaseRepositoryImp](./index.md)

# TemplateDataBaseRepositoryImp

`class TemplateDataBaseRepositoryImp : `[`TemplateDataBaseRepository`](../-template-data-base-repository/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `TemplateDataBaseRepositoryImp()` |

### Functions

| Name | Summary |
|---|---|
| [createTemplate](create-template.md) | `suspend fun createTemplate(template: `[`Template`](../../com.example.chatmemo.domain.model.entity/-template/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文登録 |
| [deleteTemplate](delete-template.md) | `suspend fun deleteTemplate(templateId: `[`TemplateId`](../../com.example.chatmemo.domain.model.value/-template-id/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>指定の定型文削除 |
| [getNextTemplateId](get-next-template-id.md) | `suspend fun getNextTemplateId(): `[`TemplateId`](../../com.example.chatmemo.domain.model.value/-template-id/index.md)<br>次のID連番の値を返す |
| [getTemplateAll](get-template-all.md) | `fun getTemplateAll(): LiveData<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Template`](../../com.example.chatmemo.domain.model.entity/-template/index.md)`>>`<br>定型文のタイトル一覧取得 return 定型文のタイトル一覧 |
| [getTemplateMessageById](get-template-message-by-id.md) | `suspend fun getTemplateMessageById(templateId: `[`TemplateId`](../../com.example.chatmemo.domain.model.value/-template-id/index.md)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`TemplateMessage`](../../com.example.chatmemo.domain.model.value/-template-message/index.md)`>`<br>タイトルに紐づいた定型文リスト取得 |
| [updateTemplate](update-template.md) | `suspend fun updateTemplate(template: `[`Template`](../../com.example.chatmemo.domain.model.entity/-template/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>定型文更新 |
