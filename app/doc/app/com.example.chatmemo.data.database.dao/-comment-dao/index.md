[app](../../index.md) / [com.example.chatmemo.data.database.dao](../index.md) / [CommentDao](./index.md)

# CommentDao

`interface CommentDao`

コメント用クエリ管理

### Functions

| Name | Summary |
|---|---|
| [deleteById](delete-by-id.md) | `abstract fun deleteById(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getAllCommentByRoom](get-all-comment-by-room.md) | `abstract suspend fun getAllCommentByRoom(roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`CommentEntity`](../../com.example.chatmemo.data.database.entity/-comment-entity/index.md)`>` |
| [insert](insert.md) | `abstract fun insert(comment: `[`CommentEntity`](../../com.example.chatmemo.data.database.entity/-comment-entity/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [update](update.md) | `abstract fun update(comment: `[`CommentEntity`](../../com.example.chatmemo.data.database.entity/-comment-entity/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [updateUserBy](update-user-by.md) | `abstract fun updateUserBy(user: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, createAt: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
