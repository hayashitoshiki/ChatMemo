[app](../../index.md) / [com.example.chatmemo.model.dao](../index.md) / [CommentDao](./index.md)

# CommentDao

`interface CommentDao`

コメント用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [deleteById](delete-by-id.md) | `abstract fun deleteById(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAllCommentByRoom](get-all-comment-by-room.md) | `abstract fun getAllCommentByRoom(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`>` |
| [insert](insert.md) | `abstract fun insert(comment: `[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract fun update(comment: `[`Comment`](../../com.example.chatmemo.model.entity/-comment/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
