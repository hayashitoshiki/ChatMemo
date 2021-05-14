package com.myapp.chatmemo.ui

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.myapp.chatmemo.R

/**
 * メイン画面
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ChatMemo)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
    }

    // フッター表示
    fun showNavigationBottom() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        if (navView.visibility == View.GONE) {
            val anim1 = AnimationUtils.loadAnimation(baseContext, R.anim.slide_in_bottom)
            navView.startAnimation(anim1)
            navView.visibility = View.VISIBLE
        }
    }

    // フッター非表示
    fun hideNavigationBottom() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val anim1 = AnimationUtils.loadAnimation(baseContext, R.anim.slide_out_bottom)
        navView.startAnimation(anim1)
        navView.visibility = View.GONE
    }
}
