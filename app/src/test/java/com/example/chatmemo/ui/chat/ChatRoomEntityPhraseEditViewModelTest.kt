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
 * ルーム定型文設定画面　ロジック仕様
 */
class ChatRoomEntityPhraseEditViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerList = mock<Observer<List<String>>>()
    private val observerString = mock<Observer<String>>()

    // mock
    private lateinit var viewModel: RoomPhraseEditViewModel
    private lateinit var databaseRepository: DataBaseRepository

    // 非選択
    private val room1 = ChatRoomEntity(1, "test", null, null, null, null, "")

    // 選択
    private val room2 = ChatRoomEntity(1, "test", 1, 1, "", "", "")
    private val modeList = listOf("順番", "ランダム")
    private val template1 = TemplateEntity(1, "test1")
    private val template2 = TemplateEntity(2, "test2")

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)


        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.getRoomByTitle(any()) } returns room1
            coEvery { it.getPhraseTitle() } returns listOf(template1, template2)
            coEvery { it.updateRoom(any()) } returns Unit
        }
        viewModel = RoomPhraseEditViewModel(room1, modeList, databaseRepository)
        viewModel.templateTitleList.observeForever(observerList)
        viewModel.templateTitleValue.observeForever(observerString)
        viewModel.modeValue.observeForever(observerString)
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
        viewModel = RoomPhraseEditViewModel(room1, modeList, databaseRepository)
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 定型文のみ選択
        viewModel.templateTitleValue.value = template1.title
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 定型文選択 + 表示順選択
        viewModel.templateTitleValue.value = template1.title
        viewModel.modeValue.value = modeList[0]
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)

        // 初期状態が定型文選択時
        // 変更なし
        viewModel = RoomPhraseEditViewModel(room2, modeList, databaseRepository)
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
        // 表示順のみ変更
        viewModel.templateTitleValue.value = template2.title
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文のみ変更
        viewModel.templateTitleValue.value = template1.title
        viewModel.modeValue.value = modeList[1]
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文変更 + 表示順変更
        viewModel.templateTitleValue.value = template2.title
        viewModel.modeValue.value = modeList[1]
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
        // 定型文なしに変更
        viewModel.templateTitleValue.value = "選択なし"
        viewModel.validate()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }

    /**
     * 変更処理
     */
    @Test
    fun submit() {
        // 定型文非選択
        viewModel.templateTitleValue.value = "選択なし"
        runBlocking {
            viewModel.submit()
        }
        coVerify { (databaseRepository).updateRoom(any()) }

        // 定型文選択
        viewModel.templateTitleValue.value = template1.title
        viewModel.modeValue.value = modeList[0]
        runBlocking {
            viewModel.submit()
        }
        coVerify { (databaseRepository).updateRoom(any()) }
    }
}