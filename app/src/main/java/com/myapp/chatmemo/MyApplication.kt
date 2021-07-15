package com.myapp.chatmemo

import android.app.Application
import android.util.Log
import com.myapp.chatmemo.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import timber.log.Timber

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
        configureTimber()
    }

    private fun configureTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
