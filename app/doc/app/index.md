[app](./index.md)

ChatMemo

### アーキテクチャ

MVVM + Clean Architecture + DDD

## 構成

* Data -- Data層
  * DataBase -- データベース関連
      * Dao -- データベースへのクエリ管理
      * Entity -- データベースのテーブル管理
  * Repository -- データベース・APIへのCRUD処理管理
* Domain --  Domain層
   * Model -- ドメインモデル管理
      * Entity -- エンティティ管理
      * Value -- 値オブジェクト管理
   * UseCase -- ビジネスロジック管理
* UI -- Presentation層
  * Utils 共通部分管理
  * ○○　○○タブ内の画面管理

### Packages

| Name | Summary |
|---|---|
| [com.example.chatmemo.data.database.dao](com.example.chatmemo.data.database.dao/index.md) | DBクエリ管理 |
| [com.example.chatmemo.data.database.entity](com.example.chatmemo.data.database.entity/index.md) | DBのテーブル構成管理 |
| [com.example.chatmemo.data.repository](com.example.chatmemo.data.repository/index.md) | DB・APIへのCRUD管理 |
| [com.example.chatmemo.domain.model.entity](com.example.chatmemo.domain.model.entity/index.md) | エンティティ管理 |
| [com.example.chatmemo.domain.model.value](com.example.chatmemo.domain.model.value/index.md) | 値オブジェクト管理 |
| [com.example.chatmemo.domain.usecase](com.example.chatmemo.domain.usecase/index.md) | ビジネスロジック管理 |
| [com.example.chatmemo.ui](com.example.chatmemo.ui/index.md) | UIベース画面管理 |
| [com.example.chatmemo.ui.adapter](com.example.chatmemo.ui.adapter/index.md) | アダプター管理 |
| [com.example.chatmemo.ui.chat](com.example.chatmemo.ui.chat/index.md) | チャットタブ画面管理 |
| [com.example.chatmemo.ui.template](com.example.chatmemo.ui.template/index.md) |  |
| [com.example.chatmemo.ui.utils](com.example.chatmemo.ui.utils/index.md) | UI共通処理管理 |
| [com.example.chatmemo.ui.utils.transition](com.example.chatmemo.ui.utils.transition/index.md) |  |

### Index

[All Types](alltypes/index.md)