# Module app
ChatMemo
### アーキテクチャ
MVVM + Clean Architecture + DDD

## 構成
* Data -- Data層
    * DataBase -- データベース関連
        * Dao -- データベースへのクエリ管理
        * Entity -- データベースのテーブル管理
    * Repository -- データベース・APIへのCRUD処理管理
*  Domain --  Domain層
    * Model -- ドメインモデル管理
        * Entity -- エンティティ管理
        * Value -- 値オブジェクト管理
    * UseCase -- ビジネスロジック管理
* UI -- Presentation層
    * Utils 共通部分管理
    * ○○　○○タブ内の画面管理

# Package com.myapp.chatmemo
アプリベース設定管理
# Package com.myapp.chatmemo.common
共通拡張クラス管理

# Package com.myapp.chatmemo.database
データベース設定管理

# Package com.myapp.chatmemo.data.database.dao
DBクエリ管理

# Package com.myapp.chatmemo.data.database.entity
DBのテーブル構成管理

# Package com.myapp.chatmemo.data.repository
DB・APIへのCRUD管理

# Package com.myapp.chatmemo.domain.model.entity
エンティティ管理

# Package com.myapp.chatmemo.domain.repository
リポジトリ定義管理

# Package  com.myapp.chatmemo.domain.model.value
値オブジェクト管理

# Package com.myapp.chatmemo.domain.usecase
ビジネスロジック管理

# Package com.myapp.chatmemo.presentation
画面管理

# Package com.myapp.chatmemo.ui
UIベース画面管理

# Package com.myapp.chatmemo.presentation.chat
チャットタブ画面管理

# Package com.myapp.chatmemo.presentation.template
テンプレート文設定タブ画面管理

# Package com.myapp.chatmemo.presentation.utils.expansion
UI用拡張クラス定義

# Package com.myapp.chatmemo.presentation.utils.transition
UI用アニメーションクラス管理
