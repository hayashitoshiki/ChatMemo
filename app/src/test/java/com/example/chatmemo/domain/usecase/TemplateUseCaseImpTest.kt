package com.example.chatmemo.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.value.TemplateId
import com.example.chatmemo.domain.value.TemplateMessage
import com.example.chatmemo.model.repository.TemplateDataBaseRepository
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

class TemplateUseCaseImpTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var templateDataBaseRepository: TemplateDataBaseRepository
    private lateinit var useCase: TemplateUseCaseImp
    private val templateMessage = TemplateMessage("testMessge")
    private val templateMessageList = listOf(templateMessage)
    private val template = Template(TemplateId(1), "testTemplate", templateMessageList)
    private val templateList = listOf(template)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        templateDataBaseRepository = mockk<TemplateDataBaseRepository>().also {
            coEvery { it.createTemplate(any()) } returns true
            coEvery { it.deleteTemplate(TemplateId(any())) } returns true
            coEvery { it.updateTemplate(any()) } returns true
            coEvery { it.getNextTemplateId() } returns TemplateId(1)
            coEvery { it.getTemplateAll() } returns MutableLiveData(templateList)
            coEvery { it.getTemplateMessageById(TemplateId(any())) } returns listOf(templateMessage)
        }
        useCase = TemplateUseCaseImp(templateDataBaseRepository)
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
     * 条件：なし
     * 結果：テンプレートを削除するメソッドが呼ばれること
     */
    @Test
    fun deleteTemplate() {
        runBlocking {
            useCase.deleteTemplate(template.templateId)
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