[document](./index.md)

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
| [com.myapp.chatmemo](com.myapp.chatmemo/index.md) | アプリベース設定管理 |
| [com.myapp.chatmemo.common](com.myapp.chatmemo.common/index.md) | 共通拡張クラス管理 |
| [com.myapp.chatmemo.data.database.dao](com.myapp.chatmemo.data.database.dao/index.md) | DBクエリ管理 |
| [com.myapp.chatmemo.data.database.entity](com.myapp.chatmemo.data.database.entity/index.md) | DBのテーブル構成管理 |
| [com.myapp.chatmemo.data.repository](com.myapp.chatmemo.data.repository/index.md) | DB・APIへのCRUD管理 |
| [com.myapp.chatmemo.database](com.myapp.chatmemo.database/index.md) | データベース設定管理 |
| [com.myapp.chatmemo.di](com.myapp.chatmemo.di/index.md) |  |
| [com.myapp.chatmemo.domain.model.entity](com.myapp.chatmemo.domain.model.entity/index.md) | エンティティ管理 |
| [com.myapp.chatmemo.domain.model.value](com.myapp.chatmemo.domain.model.value/index.md) | 値オブジェクト管理 |
| [com.myapp.chatmemo.domain.repository](com.myapp.chatmemo.domain.repository/index.md) | リポジトリ定義管理 |
| [com.myapp.chatmemo.domain.usecase](com.myapp.chatmemo.domain.usecase/index.md) | ビジネスロジック管理 |
| [com.myapp.chatmemo.presentation](com.myapp.chatmemo.presentation/index.md) | 画面管理 |
| [com.myapp.chatmemo.presentation.chat](com.myapp.chatmemo.presentation.chat/index.md) | チャットタブ画面管理 |
| [com.myapp.chatmemo.presentation.template](com.myapp.chatmemo.presentation.template/index.md) | テンプレート文設定タブ画面管理 |
| [com.myapp.chatmemo.presentation.utils.expansion](com.myapp.chatmemo.presentation.utils.expansion/index.md) | UI用拡張クラス定義 |
| [com.myapp.chatmemo.presentation.utils.transition](com.myapp.chatmemo.presentation.utils.transition/index.md) | UI用アニメーションクラス管理 |

### Index

[All Types](alltypes/index.md)