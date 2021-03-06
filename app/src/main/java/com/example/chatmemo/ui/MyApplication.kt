package com.example.chatmemo.ui

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.chatmemo.model.dao.AppDatabase
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.repository.DataBaseRepository
import com.example.chatmemo.model.repository.DataBaseRepositoryImp
import com.example.chatmemo.ui.chat.*
import com.example.chatmemo.ui.phrase.FixedPhraseAddViewModel
import com.example.chatmemo.ui.phrase.FixedPhraseListViewModel
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
        database = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "app_database"
        ).build()
    }

    // Koinモジュール
    private val module: Module = module {
        viewModel { HomeViewModel(get()) }
        viewModel { (id: Long) -> ChatViewModel(id, get()) }
        viewModel { (modeList: List<String>) -> RoomAddViewModel(modeList, get()) }
        viewModel { (room: ChatRoom, modeList: List<String>) ->
            RoomPhraseEditViewModel(
                room, modeList, get()
            )
        }
        viewModel { (room: ChatRoom) -> RoomTitleEditViewModel(room, get()) }
        viewModel { FixedPhraseAddViewModel(get()) }
        viewModel { FixedPhraseListViewModel(get()) }

        factory<DataBaseRepository> { DataBaseRepositoryImp() }
    }
}
