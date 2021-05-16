package com.myapp.chatmemo.presentation.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.CommentDateTime
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.User
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.time.LocalDateTime

/**
 * ホーム画面ロジック仕様
 */
class HomeViewModelTest {

    // region Coroutine関連

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    // endregoin

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var viewModel: HomeViewModel
    private lateinit var chatUseCase: ChatUseCase

    // data
    private val roomId1 = RoomId(1)
    private val title = "testRoom"
    private val comment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val comment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val comment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val chatroom1 = ChatRoom(roomId1, title, null, commentList)
    private val chatRoomList = listOf(chatroom1)
    private val chatRoomListEmpty = listOf<ChatRoom>()
    private val cahtRoomListFlow = flow { emit(chatRoomList) }
    private val chatRoomListEmptyFlow = flow { emit(chatRoomListEmpty) }

    private val observerRoomList = mock<Observer<List<ChatRoom>>>()
    private val observerBoolean = mock<Observer<Boolean>>()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        chatUseCase = mockk<ChatUseCase>().also {
            every { it.getRoomAll() } returns cahtRoomListFlow
            coEvery { it.deleteRoom(RoomId(any())) } returns Unit
        }
        viewModel = HomeViewModel(chatUseCase)
        initObserver()
    }

    private fun initObserver() {
        viewModel.chatRoomEntityList.observeForever(observerRoomList)
        viewModel.isNoDataText.observeForever(observerBoolean)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // regoin ルームリストなし文言表示制御

    /**
     * ルームリストなし文言表示制御
     *
     * 条件：取得したルームリストが０件
     * 結果：ルームリストなし文言が表示される
     */
    @Test
    fun changeNoDataTextByNon() {
        chatUseCase = mockk<ChatUseCase>().also {
            every { it.getRoomAll() } returns chatRoomListEmptyFlow
        }
        viewModel = HomeViewModel(chatUseCase)
        initObserver()
        val result = viewModel.isNoDataText.value
        assertEquals(true, result)
    }

    /**
     * ルームリストなし文言表示制御
     *
     * 条件：取得したルームリストが１件以上
     * 結果：ルームリストなし文言が表示されない
     */
    @Test
    fun changeNoDataTextByNotNon() {
        val result = viewModel.isNoDataText.value
        assertEquals(false, result)
    }

    // endregion

    // region  ルームリスト取得

    /**
     * ルームリスト取得
     *
     * 条件：なし
     * 結果：ルームリスト取得メソッドの結果が格納されること
     */
    @Test
    fun getRoomList() {
        val result = viewModel.chatRoomEntityList.value
        assertEquals(chatRoomList, result)
    }

    // endregion

    // region ルーム削除

    /**
     * ルーム削除
     *
     * 条件：なし
     * 結果：指定したルーム名でルーム削除のメソッドが呼ばれること
     */
    @Test
    fun deleteRoom() {
        viewModel.deleteRoom(roomId1)
        coVerify { (chatUseCase).deleteRoom(roomId1) }
    }

    // endregion
}
