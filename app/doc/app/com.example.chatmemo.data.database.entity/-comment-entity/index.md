[app](../../index.md) / [com.example.chatmemo.data.local.database.entity](../index.md) / [CommentEntity](./index.md)

# CommentEntity

`data class CommentEntity`

コメントテーブル

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CommentEntity(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?, text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, user: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, createdAt: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)`<br>コメントテーブル |

### Properties

| Name | Summary |
|---|---|
| [createdAt](created-at.md) | `var createdAt: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>生成時間 yyyy/MM/dd HH:mm |
| [id](id.md) | `val id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?`<br>プライマリーキー |
| [roomId](room-id.md) | `var roomId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>紐づいているルームID |
| [text](text.md) | `var text: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>コメント |
| [user](user.md) | `var user: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>ユーザー |
