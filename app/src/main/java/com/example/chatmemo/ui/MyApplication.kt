package com.example.chatmemo.ui

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.chatmemo.data.local.database.dao.AppDatabase
import com.example.chatmemo.data.repository.LocalChatRepository
import com.example.chatmemo.data.repository.LocalChatRepositoryImp
import com.example.chatmemo.data.repository.LocalTemplateRepository
import com.example.chatmemo.data.repository.LocalTemplateRepositoryImp
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.RoomId
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.ChatUseCaseImp
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCaseImp
import com.example.chatmemo.ui.chat.*
import com.example.chatmemo.ui.template.TempalteAddViewModel
import com.example.chatmemo.ui.template.TemplateListViewModel
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class MyApplication : Application() {

    companion object {
        lateinit var shared: MyApplication
        lateinit var database: AppDatabase

        const val TAG = "MyApplication"
    }

    // Grovalp Scope
    private val applicationScope = MainScope()

    init {
        shared = this
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")

        startKoin {
            androidContext(applicationContext)
            modules(module)
        }

        // AppDatabaseをビルドする
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
    }

    // Koinモジュール
    private val module: Module = module {
        viewModel { HomeViewModel(get()) }
        viewModel { (id: RoomId) -> ChatViewModel(id, get()) }
        viewModel { RoomAddViewModel(get(), get()) }
        viewModel { (chatroom: ChatRoom) -> RoomPhraseEditViewModel(chatroom, get(), get()) }
        viewModel { (chatroom: ChatRoom) -> RoomTitleEditViewModel(chatroom, get()) }
        viewModel { (template: Template?) -> TempalteAddViewModel(template, get()) }
        viewModel { TemplateListViewModel(get()) }

        factory<ChatUseCase> { ChatUseCaseImp(get(), applicationScope) }
        factory<TemplateUseCase> { TemplateUseCaseImp(get(), get()) }

        factory<LocalTemplateRepository> { LocalTemplateRepositoryImp(database.templateDao(), database.phraseDao()) }
        factory<LocalChatRepository> {
            LocalChatRepositoryImp(database.roomDao(), database.commentDao(), database.templateDao(), database.phraseDao())
        }
    }
}
