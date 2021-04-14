package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.model.repository.DataBaseRepository
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * ルーム名変更ダイアログ　ロジック仕様
 */
class ChatRoomTitleEditViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<String>>()

    // mock
    private lateinit var viewModel: RoomTitleEditViewModel
    private lateinit var databaseRepository: DataBaseRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val room = ChatRoom(null, "test", 0, 0, "", "", "")
        val template = Template(null, "test")
        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.getRoomByTitle("") } returns room
            coEvery { it.getPhraseTitle() } returns listOf(template)
            coEvery { it.updateRoom(any()) } returns Unit
        }
        viewModel = RoomTitleEditViewModel(room, databaseRepository)
        viewModel.newRoomTitle.observeForever(observerString)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.oldRoomTitle.observeForever(observerString)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * ルーム名変更
     */
    @Test
    fun changeRoomName() {
        runBlocking {
            viewModel.changeRoomName("test")
            coVerify { (databaseRepository).updateRoom(any()) }
        }
    }

    /**
     * 変更ボタンの活性/非活性
     */
    @Test
    fun changeSubmitButton() {
        // 入力文字なし
        viewModel.newRoomTitle.value = ""
        assertEquals(viewModel.isEnableSubmitButton.value, false)
        // 入力文字あり && 同じ文字列
        viewModel.newRoomTitle.value = "test"
        assertEquals(viewModel.isEnableSubmitButton.value, false)
        // 入力文字あり && 違う文字列
        viewModel.newRoomTitle.value = "test2"
        assertEquals(viewModel.isEnableSubmitButton.value, true)
    }
}
