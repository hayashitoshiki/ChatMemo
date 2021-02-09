package com.example.chatmemo.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmemo.R
import com.example.chatmemo.model.entity.ChatRoom


class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ChatMemo)
        setContentView(R.layout.activity_chat)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val data = intent.getSerializableExtra("data") as ChatRoom
        val fragment = ChatFragment.newInstance(data)
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, fragment).commit()
    }
}