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
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        lateinit var shared: MyApplication
        lateinit var database: AppDatabase

        const val TAG = "MyApplication"
    }

    // Grovalp Scope
    val applicationScope = MainScope()

    init {
        shared = this
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }
}
