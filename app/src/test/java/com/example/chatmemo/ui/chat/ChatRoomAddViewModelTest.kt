package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.*
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.model.repository.DataBaseRepository
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
 * ルーム作成顔面 ロジック仕様
 */
class ChatRoomAddViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerInt = mock<Observer<Int>>()
    private val observerString = mock<Observer<String>>()

    // mock
    private lateinit var viewModel: RoomAddViewModel
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
        }
        viewModel = RoomAddViewModel(databaseRepository)
        viewModel.phraseTitleValueInt.observeForever(observerInt)
        viewModel.titleText.observeForever(observerString)
        viewModel.modeValueInt.observeForever(observerInt)
        viewModel.modeValueInt.value = 0
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * 新規ルーム作成ボタンロジック
     *
     */
    @Test
    fun createRoom() {
        // 定型文非選択
        viewModel.phraseTitleValueInt.value = 0
        runBlocking {
            viewModel.createRoom()
        }
        coVerify{(databaseRepository).createRoom(any())}
        // 定型文選択
        viewModel.phraseTitleValueInt.value = 1
        viewModel.modeValueInt.value = 1
        runBlocking {
            viewModel.createRoom()
        }
        coVerify{(databaseRepository).createRoom(any())}
    }

    /**
     * 作成ボタンの活性/非活性ロジック
     * 観点 活性 = true、非活性 = false
     */
    @Test
    fun changeSubmitButton() {
        // 初期状態
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名未入力 & 定型文指定なし
        viewModel.titleText.value = ""
        viewModel.phraseTitleValueInt.value = 0
        viewModel.modeValueInt.value = 0
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名未入力 & 定型文指定あり + リストの表示形式選択あり
        viewModel.titleText.value = ""
        viewModel.phraseTitleValueInt.value = 1
        viewModel.modeValueInt.value = 1
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択なし
        viewModel.titleText.value = "test"
        viewModel.phraseTitleValueInt.value = 1
        viewModel.modeValueInt.value = 0
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名入力済 & 定型文指定なし
        viewModel.titleText.value = "test"
        viewModel.phraseTitleValueInt.value = 0
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択あり(順番)
        viewModel.titleText.value = "test"
        viewModel.phraseTitleValueInt.value = 1
        viewModel.modeValueInt.value = 1
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択あり(ランダム)
        viewModel.titleText.value = "test"
        viewModel.modeValueInt.value = 2
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }
}