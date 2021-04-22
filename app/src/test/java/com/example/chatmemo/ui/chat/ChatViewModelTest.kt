package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.*
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
 * チャット画面　ロジック仕様
 */
class ChatViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<String>>()
    private val observerComment = mock<Observer<List<Comment>>>()
    private val observerRoom = mock<Observer<ChatRoom>>()

    // mock
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatUseCase: ChatUseCase

    private val roomId1 = RoomId(1)
    private val roomId2 = RoomId(2)
    private val roomId3 = RoomId(3)
    private val title = "testRoom"
    private val templateMessage1 = TemplateMessage("testMessge1")
    private val templateMessage2 = TemplateMessage("testMessge2")
    private val templateMessage3 = TemplateMessage("testMessge3")
    private val templateMessageList = listOf(templateMessage1, templateMessage2, templateMessage3)
    private val template = Template(TemplateId(1), "testTemplate", templateMessageList)
    private val templateMode1 = TemplateMode.Order("順番", 0)
    private val templateMode2 = TemplateMode.Randam("ランダム", mutableListOf())
    private val templateConfiguration1 = TemplateConfiguration(template, templateMode1)
    private val templateConfiguration2 = TemplateConfiguration(template, templateMode2)
    private val comment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val comment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val comment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val reComment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val reComment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val reComment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val reCommentList = mutableListOf(reComment1, reComment2, reComment3)
    private val chatroom1 = ChatRoom(roomId1, title, null, commentList)
    private val chatroom2 = ChatRoom(roomId2, title, templateConfiguration1, commentList)
    private val chatroom3 = ChatRoom(roomId3, title, templateConfiguration2, commentList)
    private val userComment = Comment("test", User.BLACK, CommentDateTime(LocalDateTime.now()))

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        chatUseCase = mockk<ChatUseCase>().also {
            coEvery { it.getChatRoomByRoomById(RoomId(1)) } returns MutableLiveData(chatroom1)
            coEvery { it.getChatRoomByRoomById(RoomId(2)) } returns MutableLiveData(chatroom2)
            coEvery { it.getChatRoomByRoomById(RoomId(3)) } returns MutableLiveData(chatroom3)
            coEvery { it.reverseAllCommentUser(any()) } returns reCommentList
            coEvery { it.deleteRoom(RoomId(any())) } returns Unit
            coEvery { it.updateRoom(any()) } returns Unit
            coEvery { it.addComment(any(), RoomId(any())) } returns userComment
            coEvery { it.addTemplateComment(any(), RoomId(any())) } returns Pair(templateConfiguration1, comment1)
        }
        viewModel = ChatViewModel(roomId1, chatUseCase)
        viewModel.commentList.observeForever(observerComment)
        viewModel.commentText.observeForever(observerString)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.chatRoom.observeForever(observerRoom)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region コメント送信

    /**
     * コメント送信
     * 条件：テンプレート設定なし
     * 結果：
     * ・コメント送信メソッドが１回呼ばる
     * ・テンプレート文送信メソッドが１回も呼ばれない
     * ・コメントリストが１増加する
     */
    @ExperimentalCoroutinesApi
    @Test
    fun submitByTemplateNon() {
        runBlocking {
            viewModel = ChatViewModel(roomId1, chatUseCase)
            val oldCommentListSize = commentList.size
            viewModel.commentText.value = "test"
            viewModel.submit()
            delay(300)
            val newCommentListSize = viewModel.commentList.value!!.size
            assertEquals(oldCommentListSize, newCommentListSize - 1)
            coVerify(exactly = 1) { (chatUseCase).addComment(any(), RoomId(any())) }
            coVerify(exactly = 0) { (chatUseCase).addTemplateComment(any(), RoomId(any())) }
            coVerify(exactly = 1) { (chatUseCase).updateRoom(any()) }
        }
    }

    /**
     * コメント送信
     * 条件：テンプレート設定あり（順番）
     * 結果：
     * ・コメント送信メソッドが１回呼ばる
     * ・テンプレート文送信メソッドが１回呼ばれる
     * ・コメントリストが２増加する
     */
    @Test
    fun submitByTemplateOrderAndList() {
        runBlocking {
            viewModel = ChatViewModel(roomId2, chatUseCase)
            val oldCommentListSize = commentList.size
            viewModel.commentText.value = "test"
            viewModel.submit()
            delay(400)
            val newCommentListSize = viewModel.commentList.value!!.size
            assertEquals(oldCommentListSize, newCommentListSize - 2)
            coVerify(exactly = 1) { (chatUseCase).addComment(any(), RoomId(any())) }
            coVerify(exactly = 1) { (chatUseCase).addTemplateComment(any(), RoomId(any())) }
            coVerify(exactly = 1) { (chatUseCase).updateRoom(any()) }
        }
    }

    /**
     * コメント送信
     * 条件：テンプレート設定あり（ランダム）
     * 結果：
     * ・コメント送信メソッドが１回呼ばる
     * ・テンプレート文送信メソッドが１回呼ばれる
     * ・コメントリストが２増加する
     */
    @Test
    fun submitByTemplatRandam() {
        runBlocking {
            viewModel = ChatViewModel(roomId3, chatUseCase)
            val oldCommentListSize = commentList.size
            viewModel.commentText.value = "test"
            viewModel.submit()
            delay(400)
            val newCommentListSize = viewModel.commentList.value!!.size
            assertEquals(oldCommentListSize, newCommentListSize - 2)
            coVerify(exactly = 1) { (chatUseCase).addComment(any(), RoomId(any())) }
            coVerify(exactly = 1) { (chatUseCase).addTemplateComment(any(), RoomId(any())) }
            coVerify(exactly = 1) { (chatUseCase).updateRoom(any()) }
        }
    }

    // endregion

    // region ルーム削除

    /**
     * ルーム削除
     * 条件：なし
     * ルーム削除のメソッドが呼ばれる
     */
    @Test
    fun deleteRoom() {
        viewModel.deleteRoom()
        coVerify(exactly = 1) { (chatUseCase).deleteRoom(roomId1) }
    }

    // endregion

    // region ユーザー変更

    /**
     * ユーザーチェンジ
     * 条件：なし
     * 結果：コメント変換メソッドが１回呼ばれ、コメントリストの中の値が変化することること
     */
    @Test
    fun changeUser() {
        viewModel.changeUser()
        coVerify(exactly = 1) { (chatUseCase).reverseAllCommentUser(any()) }
        assertEquals(reCommentList, viewModel.commentList.value!!)
    }

    // endregion

    // region 送信ボタンのバリデーション

    /**
     * 送信ボタンの活性/非活性
     * 条件：入力中の文字あり
     * 結果：trueが返る
     */
    @Test
    fun changeSubmitButtonTrue() {
        viewModel.commentText.value = "test"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 送信ボタンの活性/非活性
     * 条件：入力中の文字なし
     * 結果：falseが返る
     */
    @Test
    fun changeSubmitButtonFalse() {
        viewModel.commentText.value = ""
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    // endregion
}
