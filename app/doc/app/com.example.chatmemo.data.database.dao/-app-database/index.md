[app](../../index.md) / [com.example.chatmemo.data.database.dao](../index.md) / [AppDatabase](./index.md)

# AppDatabase

`abstract class AppDatabase : RoomDatabase`

DB定義

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AppDatabase()`<br>DB定義 |

### Functions

| Name | Summary |
|---|---|
| [commentDao](comment-dao.md) | `abstract fun commentDao(): `[`CommentDao`](../-comment-dao/index.md) |
| [phraseDao](phrase-dao.md) | `abstract fun phraseDao(): `[`PhraseDao`](../-phrase-dao/index.md) |
| [roomDao](room-dao.md) | `abstract fun roomDao(): `[`RoomDao`](../-room-dao/index.md) |
| [templateDao](template-dao.md) | `abstract fun templateDao(): `[`TemplateDao`](../-template-dao/index.md) |
