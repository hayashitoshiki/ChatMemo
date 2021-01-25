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
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * ルーム定型文設定画面　ロジック仕様
 */
class ChatRoomPhraseEditViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<List<String>>>()
    private val observerInt = mock<Observer<Int>>()

    // mock
    private lateinit var viewModel: RoomPhraseEditViewModel
    private lateinit var databaseRepository: DataBaseRepository
    // 非選択
    private val room1 = ChatRoom(1, "test", null, null, null, null, "")
    // 選択
    private val room2 = ChatRoom(1, "test", 1, 1, "", "", "")

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val template1 = Template(1, "test")
        val template2 = Template(2, "test")
        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.getRoomByTitle(any()) } returns room1
            coEvery { it.getPhraseTitle() } returns listOf(template1, template2)
            coEvery { it.updateRoom(any()) } returns Unit
        }
        viewModel = RoomPhraseEditViewModel(room1, databaseRepository)
        viewModel.roomTitleList.observeForever(observerString)
        viewModel.roomTitleValueInt.observeForever(observerInt)
        viewModel.modeValueInt.observeForever(observerInt)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * 変更ボタン バリデート
     */
    @Test
    fun validate() {
        // 初期状態が定型文非選択時
        // 変更なし
        viewModel = RoomPhraseEditViewModel(room1, databaseRepository)
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 定型文のみ選択
        viewModel.roomTitleValueInt.value = 1
        viewModel.modeValueInt.value = 0
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 定型文選択 + 表示順選択
        viewModel.roomTitleValueInt.value = 1
        viewModel.modeValueInt.value = 1
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)

        // 初期状態が定型文選択時
        // 変更なし
        viewModel = RoomPhraseEditViewModel(room2, databaseRepository)
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 表示順のみ変更
        viewModel.roomTitleValueInt.value = 2
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文のみ変更
        viewModel.roomTitleValueInt.value = 1
        viewModel.modeValueInt.value = 2
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文変更 + 表示順変更
        viewModel.roomTitleValueInt.value = 2
        viewModel.modeValueInt.value = 2
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文なしに変更
        viewModel.roomTitleValueInt.value = 0
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }

    /**
     * 変更処理
     */
    @Test
    fun submit() {
        // 定型文非選択
        viewModel.roomTitleValueInt.value = 0
        runBlocking {
            viewModel.submit()
        }
        coVerify{(databaseRepository).updateRoom(any())}

        // 定型文選択
        viewModel.roomTitleValueInt.value = 1
        viewModel.modeValueInt.value = 1
        runBlocking {
            viewModel.submit()
        }
        coVerify{(databaseRepository).updateRoom(any())}
    }
}