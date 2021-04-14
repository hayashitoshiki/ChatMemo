package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.value.Comment
import com.example.chatmemo.domain.model.value.CommentDateTime
import com.example.chatmemo.domain.model.value.RoomId
import com.example.chatmemo.domain.model.value.User
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.mockk
import java.time.LocalDateTime
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
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
    private lateinit var chatUseCase: ChatUseCase

    private val roomId1 = RoomId(1)
    private val title = "testRoom"
    private val comment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val comment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val comment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val chatroom1 = ChatRoom(roomId1, title, null, commentList)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        chatUseCase = mockk()
        viewModel = RoomTitleEditViewModel(chatroom1, chatUseCase)
        viewModel.newRoomTitle.observeForever(observerString)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.oldRoomTitle.observeForever(observerString)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region 更新ボタンのバリデート
    /**
     * 変更ボタンの活性/非活性
     * 条件：初期状態
     * 結果：falseが返る
     */
    @Test
    fun changeSubmitButtonByInit() {
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 変更ボタンの活性/非活性
     * 条件：新しいタイトルが入力されていない
     * 結果：falseが返る
     */
    @Test
    fun changeSubmitButtonByTitleNone() {
        viewModel.newRoomTitle.value = ""
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 変更ボタンの活性/非活性
     * 条件：入力したタイトルが元のタイトルと同じ
     * 結果：falseが返る
     */
    @Test
    fun changeSubmitButtonByTitleEqual() {
        viewModel.newRoomTitle.value = "testRoom"
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 変更ボタンの活性/非活性
     * 条件：入力したタイトルが元のタイトルと異なる
     * 結果：trueが返る
     */
    @Test
    fun changeSubmitButtonTitleNotEqual() {
        viewModel.newRoomTitle.value = "testRoom2"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
    }

    // endregion
}
