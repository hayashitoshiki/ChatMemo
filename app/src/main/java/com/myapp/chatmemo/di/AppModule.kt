package com.myapp.chatmemo.di

import android.content.Context
import androidx.room.Room
import com.myapp.chatmemo.MyApplication
import com.myapp.chatmemo.data.database.dao.CommentDao
import com.myapp.chatmemo.data.database.dao.PhraseDao
import com.myapp.chatmemo.data.database.dao.RoomDao
import com.myapp.chatmemo.data.database.dao.TemplateDao
import com.myapp.chatmemo.data.repository.LocalChatRepositoryImp
import com.myapp.chatmemo.data.repository.LocalTemplateRepositoryImp
import com.myapp.chatmemo.database.AppDatabase
import com.myapp.chatmemo.domain.repository.LocalChatRepository
import com.myapp.chatmemo.domain.repository.LocalTemplateRepository
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.domain.usecase.ChatUseCaseImp
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.myapp.chatmemo.domain.usecase.TemplateUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    // UseCase
    @Binds
    abstract fun bindChatUseCase(chatUseCaseImp: ChatUseCaseImp): ChatUseCase

    @Binds
    abstract fun bindTemplateUseCase(templateUseCaseImp: TemplateUseCaseImp): TemplateUseCase


    // Repository
    @Binds
    abstract fun bindLocalTemplateRepository(localTemplateRepositoryImp: LocalTemplateRepositoryImp): LocalTemplateRepository

    @Binds
    abstract fun bindLocalChatRepository(localChatRepositoryImp: LocalChatRepositoryImp): LocalChatRepository

}



@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Dao
    @Provides
    @Singleton
    fun provideCommentDao(db: AppDatabase): CommentDao {
        return db.commentDao()
    }

    @Provides
    @Singleton
    fun providePhrasetDao(db: AppDatabase): PhraseDao {
        return db.phraseDao()
    }

    @Provides
    @Singleton
    fun provideRoomDao(db: AppDatabase): RoomDao {
        return db.roomDao()
    }

    @Provides
    @Singleton
    fun provideTemplateDao(db: AppDatabase): TemplateDao {
        return db.templateDao()
    }


    // coroutine
    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return MyApplication.shared.applicationScope
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}
