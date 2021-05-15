package com.myapp.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.myapp.chatmemo.BaseUnitTest
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.CommentDateTime
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.User
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class HomeViewModelTest : BaseUnitTest() {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var viewModel: HomeViewModel
    private lateinit var chatUseCase: com.myapp.chatmemo.domain.usecase.ChatUseCase

    // data
    private val roomId1 = RoomId(1)
    private val title = "testRoom"
    private val comment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val comment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val comment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val chatroom1 = ChatRoom(roomId1, title, null, commentList)
    private val chatRoomList = listOf(chatroom1)
    private val cahtRoomListFlow = flow { emit(chatRoomList) }

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        chatUseCase = mockk<com.myapp.chatmemo.domain.usecase.ChatUseCase>().also {
            every { it.getRoomAll() } returns cahtRoomListFlow
            coEvery { it.deleteRoom(RoomId(any())) } returns Unit
        }
        viewModel = HomeViewModel(chatUseCase)
        viewModel.chatRoomEntityList.observeForever(observerRoomList)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

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
