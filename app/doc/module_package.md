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

# Package com.example.chatmemo.data.database.dao
DBクエリ管理

# Package com.example.chatmemo.data.database.entity
DBのテーブル構成管理

# Package com.example.chatmemo.data.repository
DB・APIへのCRUD管理

# Package com.example.chatmemo.domain.model.entity
エンティティ管理

# Package  com.example.chatmemo.domain.model.value
値オブジェクト管理

# Package com.example.chatmemo.domain.usecase
ビジネスロジック管理

# Package com.example.chatmemo.ui
UIベース画面管理

# Package com.example.chatmemo.ui.chat
チャットタブ画面管理

# Package com.example.chatmemo.ui.phrase
テンプレート文設定タブ画面管理

# Package com.example.chatmemo.ui.adapter
アダプター管理

# Package com.example.chatmemo.ui.utils
UI共通処理管理

# Package com.example.chatmemo.ui.transition
アニメーション管理