package com.example.chatmemo.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.data.repository.ChatDataBaseRepository
import com.example.chatmemo.data.repository.TemplateDataBaseRepository
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.*
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

class TemplateUseCaseImpTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var chatDataBaseRepository: ChatDataBaseRepository
    private lateinit var templateDataBaseRepository: TemplateDataBaseRepository
    private lateinit var useCase: TemplateUseCaseImp
    private val templateMessage = TemplateMessage("testMessge")
    private val templateMessageList = listOf(templateMessage)
    private val template = Template(TemplateId(1), "testTemplate", templateMessageList)
    private val templateList = listOf(template)
    private val roomId1 = RoomId(1)
    private val time1 = CommentDateTime(LocalDateTime.now())
    private val time2 = CommentDateTime(LocalDateTime.now())
    private val time3 = CommentDateTime(LocalDateTime.now())
    private val comment1 = Comment("testComment1", User.BLACK, time1)
    private val comment2 = Comment("testComment2", User.WHITE, time2)
    private val comment3 = Comment("testComment3", User.BLACK, time3)
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val chatroom1 = ChatRoom(roomId1, "room1", null, commentList)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        chatDataBaseRepository = mockk<ChatDataBaseRepository>().also {
            coEvery { it.getRoomByTemplateId(TemplateId(1)) } returns mutableListOf(chatroom1)
            coEvery { it.getRoomByTemplateId(TemplateId(2)) } returns mutableListOf()
        }
        templateDataBaseRepository = mockk<TemplateDataBaseRepository>().also {
            coEvery { it.createTemplate(any()) } returns true
            coEvery { it.deleteTemplate(TemplateId(any())) } returns true
            coEvery { it.updateTemplate(any()) } returns true
            coEvery { it.getNextTemplateId() } returns TemplateId(1)
            coEvery { it.getTemplateAll() } returns MutableLiveData(templateList)
            coEvery { it.getTemplateMessageById(TemplateId(any())) } returns listOf(templateMessage)
        }
        useCase = TemplateUseCaseImp(chatDataBaseRepository, templateDataBaseRepository)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * テンプレート作成
     * 条件：なし
     * 結果：テンプレートを作成するメソッドが呼ばれること
     */
    @Test
    fun createTemplate() {
        runBlocking {
            useCase.createTemplate(template)
            coVerify(exactly = 1) { (templateDataBaseRepository).createTemplate(template) }
        }
    }

    /**
     * テンプレート削除
     * 条件：指定したIDのテンプレートを使用しているチャットルームが存在している
     * 結果：
     * ・テンプレートを削除するメソッドが呼ばれること
     * ・trueが返ること
     */
    @Test
    fun deleteTemplateByUse() {
        runBlocking {
            val result = useCase.deleteTemplate(TemplateId(2))
            assertEquals(false, result)
            coVerify(exactly = 0) { (templateDataBaseRepository).deleteTemplate(TemplateId(2)) }
        }
    }

    /**
     * テンプレート削除
     * 条件：指定したIDのテンプレートを使用しているチャットルームが存在していない
     * 結果：
     * ・テンプレートを削除するメソッドが呼ばれること
     * ・trueが返ること
     */
    @Test
    fun deleteTemplateByNotUse() {
        runBlocking {
            val result = useCase.deleteTemplate(template.templateId)
            assertEquals(true, result)
            coVerify(exactly = 1) { (templateDataBaseRepository).deleteTemplate(template.templateId) }
        }
    }

    /**
     * テンプレート更新
     * 条件：なし
     * 結果：テンプレート更新を行うメソッドが呼ばれること
     */
    @Test
    fun updateTemplate() {
        runBlocking {
            useCase.updateTemplate(template)
            coVerify(exactly = 1) { (templateDataBaseRepository).updateTemplate(template) }
        }
    }

    /**
     * 全てのテンプレートを取得する
     * 条件：なし
     * 結果：全てのテンプレートを取得するメソッドが呼ばれること
     */
    @Test
    fun getTemplateAll() {
        useCase.getTemplateAll()
        coVerify(exactly = 1) { (templateDataBaseRepository).getTemplateAll() }
    }

    /**
     * 選択欄表示用のテンプレートリスト取得する
     * 条件：なし
     * 結果：
     * ・全てのテンプレートを取得するメソッドが呼ばれること
     * ・全てのテンプレートリスト＋選択なし用のオブジェクトが返ること
     */
    @Test
    fun getSpinnerTemplateAll() {
        val result = useCase.getSpinnerTemplateAll()
        coVerify(exactly = 1) { (templateDataBaseRepository).getTemplateAll() }
        assertEquals(templateList.size + 1, result.value!!.size)
        assertEquals(templateList[0], result.value!![1])
    }

    /**
     * 次のテンプレートの連番IDを取得する
     * 条件：なし
     * 結果：次のテンプレートの連番IDを取得するメソッドが呼ばれること
     */
    @Test
    fun getNextTemplateId() {
        runBlocking {
            useCase.getNextTemplateId()
            coVerify(exactly = 1) { (templateDataBaseRepository).getNextTemplateId() }
        }
    }

    /**
     * テンプレートIDに紐づくテンプレートメッセージを取得する
     * 条件：なし
     * 結果：テンプレートIDに紐づくテンプレートメッセージを取得するメソッドが呼ばれること
     */
    @Test
    fun getTemplateMessageByTemplateId() {
        runBlocking {
            useCase.getTemplateMessageById(template.templateId)
            coVerify(exactly = 1) { (templateDataBaseRepository).getTemplateMessageById(template.templateId) }
        }
    }
}