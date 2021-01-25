[app](../../index.md) / [com.example.chatmemo.model.entity](../index.md) / [ChatRoom](./index.md)

# ChatRoom

`data class ChatRoom : `[`Serializable`](https://developer.android.com/reference/java/io/Serializable.html)

トークルーム

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ChatRoom(id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?, title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?, templateMode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?, phrasePoint: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, commentLast: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, commentTime: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)`<br>トークルーム |

### Properties

| Name | Summary |
|---|---|
| [commentLast](comment-last.md) | `var commentLast: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>最新コメント：一言もコメントしていない場合はnull |
| [commentTime](comment-time.md) | `var commentTime: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>最新コメント時間：一言もコメントしていない場合はnull |
| [id](id.md) | `val id: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?`<br>プライマリーキー |
| [phrasePoint](phrase-point.md) | `var phrasePoint: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>定型文の表示位置メモ：定型文を設定していない場合はnull |
| [templateId](template-id.md) | `var templateId: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?`<br>定型文タイトル：設定していない場合はnull |
| [templateMode](template-mode.md) | `var templateMode: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>定型文の表示モード:定型文を設定していない場合はnull |
| [title](title.md) | `var title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>部屋名 |
