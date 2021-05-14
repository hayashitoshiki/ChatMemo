package com.myapp.chatmemo

import androidx.lifecycle.Observer
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope

abstract class BaseUnitTest {

    // region Coroutine関連
    @ExperimentalCoroutinesApi
    protected val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    protected val testScope = TestCoroutineScope(testDispatcher)

    protected val observerRoomList = mock<Observer<List<ChatRoom>>>()

    // endregoin
}
