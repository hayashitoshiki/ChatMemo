package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.model.entity.ChatRoomEntity
import com.example.chatmemo.model.entity.TemplateEntity
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
 * ルーム作成顔面 ロジック仕様
 */
class ChatRoomEntityAddViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerString = mock<Observer<String>>()

    // mock
    private lateinit var viewModel: RoomAddViewModel
    private lateinit var databaseRepository: DataBaseRepository

    private val modeList = listOf("順番", "ランダム")
    private val template = TemplateEntity(null, "test")

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val room = ChatRoomEntity(null, "test", 0, 0, "", "", "")
        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.getRoomByTitle("") } returns room
            coEvery { it.getPhraseTitle() } returns listOf(template)
        }
        viewModel = RoomAddViewModel(modeList, databaseRepository)
        viewModel.templateTitleValue.observeForever(observerString)
        viewModel.titleText.observeForever(observerString)
        viewModel.modeValue.observeForever(observerString)
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
        viewModel.templateTitleValue.value = "選択なし"
        runBlocking {
            viewModel.createRoom()
        }
        coVerify { (databaseRepository).createRoom(any()) }
        // 定型文選択
        viewModel.templateTitleValue.value = template.title
        viewModel.modeValue.value = modeList[0]
        runBlocking {
            viewModel.createRoom()
        }
        coVerify { (databaseRepository).createRoom(any()) }
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
        viewModel.templateTitleValue.value = "選択なし"
        viewModel.modeValue.value = null
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名未入力 & 定型文指定あり + リストの表示形式選択あり
        viewModel.titleText.value = ""
        viewModel.templateTitleValue.value = template.title
        viewModel.modeValue.value = modeList[0]
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択なし
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = template.title
        viewModel.modeValue.value = null
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // ルーム名入力済 & 定型文指定なし
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = "選択なし"
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択あり(順番)
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = template.title
        viewModel.modeValue.value = modeList[0]
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // ルーム名入力済 & 定型文指定あり + リストの表示形式選択あり(ランダム)
        viewModel.titleText.value = "test"
        viewModel.modeValue.value = modeList[1]
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }
}