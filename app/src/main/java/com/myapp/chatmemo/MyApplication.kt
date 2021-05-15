package com.myapp.chatmemo

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.myapp.chatmemo.data.repository.LocalChatRepositoryImp
import com.myapp.chatmemo.data.repository.LocalTemplateRepositoryImp
import com.myapp.chatmemo.database.AppDatabase
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.repository.LocalChatRepository
import com.myapp.chatmemo.domain.repository.LocalTemplateRepository
import com.myapp.chatmemo.domain.usecase.ChatUseCaseImp
import com.myapp.chatmemo.domain.usecase.TemplateUseCaseImp
import com.myapp.chatmemo.presentation.chat.*
import com.myapp.chatmemo.presentation.template.TempalteAddViewModel
import com.myapp.chatmemo.presentation.template.TemplateListViewModel
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
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database")
            .build()
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

        factory<com.myapp.chatmemo.domain.usecase.ChatUseCase> { ChatUseCaseImp(get(), applicationScope) }
        factory<com.myapp.chatmemo.domain.usecase.TemplateUseCase> { TemplateUseCaseImp(get(), get()) }

        factory<LocalTemplateRepository> { LocalTemplateRepositoryImp(database.templateDao(), database.phraseDao()) }
        factory<LocalChatRepository> {
            LocalChatRepositoryImp(
                database.roomDao(), database.commentDao(), database.templateDao(), database.phraseDao()
            )
        }
    }
}
