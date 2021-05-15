package com.myapp.chatmemo.domain.usecase

import com.myapp.chatmemo.domain.BaseUnitTest
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.*
import com.myapp.chatmemo.domain.repository.LocalChatRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ChatUseCaseImpTest : BaseUnitTest() {

    private lateinit var localChatRepository: LocalChatRepository
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
    private val templateMessageList = mutableListOf(templateMessage1, templateMessage2, templateMessage3)
    private val template = Template(TemplateId(1), "testTemplate1", templateMessageList)
    private val templateMode1 = TemplateMode.Order("順番", 0)
    private val templateMode2 = TemplateMode.Randam("ランダム", mutableListOf())
    private val templateConfiguration1 = TemplateConfiguration(template, templateMode1)
    private val templateConfiguration2 = TemplateConfiguration(template, templateMode2)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        localChatRepository = mockk<LocalChatRepository>().also {
            coEvery { it.getNextRoomId() } returns RoomId(1)
            coEvery { it.createRoom(any()) } returns Unit
            coEvery { it.deleteRoom(RoomId(any())) } returns Unit
            coEvery { it.updateRoom(any()) } returns Unit
            coEvery { it.getRoomAll() } returns flow { emit(listOf(chatroom1)) }
            coEvery { it.getRoomById(RoomId(any())) } returns flow { emit(chatroom1) }
            coEvery { it.getRoomByTemplateId(TemplateId(any())) } returns listOf(chatroom1)
            coEvery { it.addComment(any(), RoomId(any())) } returns Unit
            coEvery { it.updateComments(any()) } returns Unit
        }
        useCase = ChatUseCaseImp(localChatRepository, testScope, testDispatcher)
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
            coVerify(exactly = 1) { (localChatRepository).updateRoom(chatroom1) }
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
        coVerify(exactly = 1) { (localChatRepository).getRoomAll() }
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
            coVerify(exactly = 1) { (localChatRepository).deleteRoom(roomId1) }
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
            coVerify(exactly = 1) { (localChatRepository).createRoom(chatroom1) }
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
            coVerify(exactly = 1) { (localChatRepository).getNextRoomId() }
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
        coVerify(exactly = 1) { (localChatRepository).getRoomById(roomId1) }
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
            coVerify(exactly = 1) { (localChatRepository).updateComments(reCommentList) }
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
            coVerify(exactly = 1) { (localChatRepository).addComment(result, roomId1) }
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式が順番のテンプレート
     * 結果：
     * ・コメント追加repositoryが呼ばれること
     * ・次に表示するコメントの添字が1になっていること
     * ・渡した時のpositionのコメントが返ること
     */
    @Test
    fun addTemplateCommentByOrder() {
        runBlocking {
            val result = useCase.addTemplateComment(templateConfiguration1, roomId1)
            val expectedPositoin = 1
            val expectedMessage = templateConfiguration1.template.templateMessageList[0]
            coVerify(exactly = 1) { (localChatRepository).addComment(result.second, roomId1) }
            assertEquals(expectedPositoin, (result.first.templateMode as TemplateMode.Order).position)
            assertEquals(expectedMessage.massage, result.second.message)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式が順番のテンプレートを最後まで繰り返す
     * 結果：
     * ・次に表示するコメントの添字が0になっていること
     */
    @Test
    fun addTemplateCommentByOderAndReset() {
        runBlocking {
            val result1 = useCase.addTemplateComment(templateConfiguration1, roomId1)
            val result2 = useCase.addTemplateComment(result1.first, roomId1)
            val result3 = useCase.addTemplateComment(result2.first, roomId1)
            val expectedPositoin = 0
            assertEquals(expectedPositoin, (result3.first.templateMode as TemplateMode.Order).position)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式がランダムのテンプレート
     * 結果：
     * ・コメント追加repositoryが呼ばれること
     * ・ランダムカウントが１つ進んでいること
     * ・追加されたpositionのコメントが返ること
     */
    @Test
    fun addTemplateCommentByRandam() {
        runBlocking {
            val result = useCase.addTemplateComment(templateConfiguration2, roomId1)
            val position = (result.first.templateMode as TemplateMode.Randam).position.first()
            val expectedMessage = templateConfiguration1.template.templateMessageList[position]
            coVerify(exactly = 1) { (localChatRepository).addComment(result.second, roomId1) }
            assertEquals(1, (result.first.templateMode as TemplateMode.Randam).position.size)
            assertEquals(expectedMessage.massage, result.second.message)
        }
    }

    /**
     * テンプレート文を送信するメソッド
     * 条件：表示形式がランダムのテンプレートを最後まで繰り返す
     * 結果：
     * ・ランダムカウントが空になっていること
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
