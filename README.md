# ChatMemo
壁打ちアプリ

## 内容
１日のルーティンを記録したり、自分の発言が相手から見たらどんなふうに感じるのか確認したり、テンプレートを用いて面接の練習をしたり。。。

## 機能
* 一人チャット機能
* 返信テンプレートを用いた自動返信機能
 
## 言語
Kotlin

## アーキテクチャ
MVVM + Clean Architecture

## 主な使用技術
###  ネイティブ(ライブラリ)
* DI
  * Koin
* 非同期
  * Coroutine
  * Coroutine Flow
* Database
  * Room
* UI
  * LiveData
  * DataBinding
  * Navigation
  * ViewModel
  * swipereveallayout

### バックエンド
* なし　


## イメージ
ホーム画面

<img src="https://github.com/hayashitoshiki/ChatMemo/blob/master/picture/Screenshot_home.png" width="400">  

チャット画面  

<img src="https://github.com/hayashitoshiki/ChatMemo/blob/master/picture/Screenshot_chat.png" width="400">  

## 主なソース

### アプリ基盤(app)
https://github.com/hayashitoshiki/ChatMemo/tree/master/app/src/main

### 画面関連(presentasion)
https://github.com/hayashitoshiki/ChatMemo/tree/master/presentation/src/main

### 業務ロジック(domain)
https://github.com/hayashitoshiki/ChatMemo/tree/master/domain/src/main/java/com/myapp/chatmemo/domain

### データアクセス(data)
https://github.com/hayashitoshiki/ChatMemo/tree/master/data/src/main/java/com/myapp/chatmemo/data

### 共通処理(commone)
https://github.com/hayashitoshiki/ChatMemo/tree/master/common/src/main/java/com/myapp/chatmemo/common

## 詳細仕様
* 画面仕様  
https://github.com/hayashitoshiki/ChatMemo/tree/master/presentation/src/test/java/com/myapp/chatmemo/presentation

* 機能仕様  
https://github.com/hayashitoshiki/ChatMemo/tree/master/domain/src/test/java/com/myapp/chatmemo/domain/usecase

## ソースドキュメント
https://github.com/hayashitoshiki/ChatMemo/blob/master/document/document/index.md
