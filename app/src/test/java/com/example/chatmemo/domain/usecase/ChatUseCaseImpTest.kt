package com.example.chatmemo.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.ChatRoom
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.*
import com.example.chatmemo.model.repository.ChatDataBaseRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.time.LocalDateTime

class ChatUseCaseImpTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var chatDataBaseRepository: ChatDataBaseRepository
    private lateinit var useCase: ChatUseCaseImp

    private val time1 = CommentDateTime(LocalDateTime.now())
    private val time2 = CommentDateTime(LocalDateTime.now())
    private val time3 = CommentDateTime(LocalDateTime.now())
    private val roomId1 = RoomId(1)
    private val comment1 = Comment("testComment1", User.BLACK, time1)
    private val comment2 = Comment("testComment2", User.WHITE, time2)
    private val comment3 = Comment("testComment3", User.BLACK, time3)
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val reComment1 = Comment("testComment1", User.WHITE, time1)
    private val reComment2 = Comment("testComment2", User.BLACK, time2)
    private val reComment3 = Comment("testComment3", User.WHITE, time3)
    private val reCommentList = mutableListOf(reComment1, reComment2, reComment3)
    private val chatroom1 = ChatRoom(roomId1, "room1", null, commentList)

    private val templateMessage1 = TemplateMessage("test1")
    private val templateMessage2 = TemplateMessage("test2")
    private val templateMessage3 = TemplateMessage("test3")
    private val templateMessageList = mutableListOf(
        templateMessage1, templateMessage2, templateMessage3
    )
    private val template = Template(TemplateId(1), "testTemplate1", templateMessageList)
    private val templateMode1 = TemplateMode.Order("順番", 0)
    private val templateMode2 = TemplateMode.Randam("ランダム", mutableListOf())
    private val templateConfiguration1 = TemplateConfiguration(template, templateMode1)
    private val templateConfiguration2 = TemplateConfiguration(template, templateMode2)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        chatDataBaseRepository = mockk<ChatDataBaseRepository>().also {
            coEvery { it.getNextRoomId() } returns RoomId(1)
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.deleteRoom(RoomId(any())) } returns Unit
            coEvery { it.updateRoom(any()) } returns Unit
            coEvery { it.getRoomAll() } returns MutableLiveData(listOf(chatroom1))
            coEvery { it.getRoomById(RoomId(any())) } returns MutableLiveData(chatroom1)
            coEvery { it.getRoomByTemplateId(any()) } returns listOf(chatroom1)
            coEvery { it.addComment(any(), RoomId(any())) } returns Unit
            coEvery { it.updateComments(any()) } returns Unit
        }
        useCase = ChatUseCaseImp(chatDataBaseRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * チャットルームを更新する
     * 条件：なし
     * 結果：チャットルームを更新するメソッドが呼ばれること
     */
    @Test
    fun updateRoom() {
        runBlocking {
            useCase.updateRoom(chatroom1)
            coVerify { (chatDataBaseRepository).updateRoom(chatroom1) }
        }
    }

    /**
     * 全てのチャットルームを取得する
     * 条件：なし
     * 結果：全てのチャットルームを取得するメソッドが１回呼ばれること
     */
    @Test
    fun getRoomAll() {
        useCase.getRoomAll()
        coVerify(exactly = 1) { (chatDataBaseRepository).getRoomAll() }
    }

    /**
     * 指定したチャットルームを削除する
     * 条件：なし
     * 結果：指定したチャットルームを削除するメソッドが１回呼ばれること
     */
    @Test
    fun deleteRoom() {
        runBlocking {
            useCase.deleteRoom(roomId1)
            coVerify(exactly = 1) { (chatDataBaseRepository).deleteRoom(roomId1) }
        }
    }

    /**
     * チャットルームを作成する
     * 条件：なし
     * 結果：チャットルームを作成するメソッドが呼ばれること
     */
    @Test
    fun createRoom() {
        runBlocking {
            useCase.createRoom(chatroom1)
            coVerify(exactly = 1) { (chatDataBaseRepository).createRoom(chatroom1) }
        }
    }

    /**
     * 次のチャットルームの連番IDを取得する
     * 条件：なし
     * 結果：次のチャットルームの連番IDを取得するメソッドが呼ばれること
     */
    @Test
    fun getNextRoomId() {
        runBlocking {
            useCase.getNextRoomId()
            coVerify(exactly = 1) { (chatDataBaseRepository).getNextRoomId() }
        }
    }

    /**
     * ルームIDに一致するチャットルームを取得する
     * 条件；なし
     * 結果：ルームIDに一致するチャットルームを取得するメソッドが呼ばれること
     */
    @Test
    fun getChatRoomByRoomById() {
        useCase.getChatRoomByRoomById(roomId1)
        coVerify(exactly = 1) { (chatDataBaseRepository).getRoomById(roomId1) }
    }

    /**
     * 受け取ったコメントのユーザを反転させる
     * 条件：なし
     * 結果；
     * ・受け取ったユーザのコメントが反転されて返されること
     * ・コメントをアップデートするメソッドが呼ばれること
     */
    @Test
    fun reverseAllCommentUser() {
        runBlocking {
            val result = useCase.reverseAllCommentUser(commentList)
            assertEquals(reCommentList, result)
            coVerify(exactly = 1) { (chatDataBaseRepository).updateComments(reCommentList) }
        }

    }

    /**
     * コメントを送信するメソッド
     * 条件：なし
     * 結果：
     * ・コメントを保存するメソッドが呼ばれること
     * ・送信したメソッドと同じ値を返すこと
     */
    @Test
    fun addComment() {
        runBlocking {
            val result = useCase.addComment(comment1.message, roomId1)
            assertEquals(comment1.message, result.message)
            assertEquals(comment1.user, result.user)
            coVerify(exactly = 1) { (chatDataBaseRepository).addComment(result, roomId1) }
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式が順番のテンプレート
     * 結果：
     */
    @Test
    fun addTemplateCommentByOrder() {
        runBlocking {
            val result = useCase.addTemplateComment(templateConfiguration1, roomId1)
            assertEquals(1, (result.first.templateMode as TemplateMode.Order).position)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式が順番のテンプレートを最後まで繰り返す
     * 結果：
     */
    @Test
    fun addTemplateCommentByOderAndReset() {
        runBlocking {
            val result1 = useCase.addTemplateComment(templateConfiguration1, roomId1)
            val result2 = useCase.addTemplateComment(result1.first, roomId1)
            val result3 = useCase.addTemplateComment(result2.first, roomId1)
            assertEquals(0, (result3.first.templateMode as TemplateMode.Order).position)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式がランダムのテンプレート
     * 結果：
     */
    @Test
    fun addTemplateCommentByRandam() {
        runBlocking {
            val result = useCase.addTemplateComment(templateConfiguration2, roomId1)
            assertEquals(1, (result.first.templateMode as TemplateMode.Randam).position.size)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式がランダムのテンプレートを最後まで繰り返す
     * 結果：
     */
    @Test
    fun addTemplateCommentByRandamAndReset() {
        runBlocking {
            val result1 = useCase.addTemplateComment(templateConfiguration2, roomId1)
            val result2 = useCase.addTemplateComment(result1.first, roomId1)
            val result3 = useCase.addTemplateComment(result2.first, roomId1)
            assertEquals(0, (result3.first.templateMode as TemplateMode.Randam).position.size)
        }
    }
}