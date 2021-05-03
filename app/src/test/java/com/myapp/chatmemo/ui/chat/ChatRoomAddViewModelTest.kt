package com.myapp.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.myapp.chatmemo.BaseUnitTest
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.RoomId
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.model.value.TemplateMode
import com.myapp.chatmemo.domain.usecase.ChatUseCase
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * ルーム作成顔面 ロジック仕様
 */
class ChatRoomAddViewModelTest : BaseUnitTest() {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var viewModel: RoomAddViewModel
    private val templateNon = Template(TemplateId(0), "選択なし", listOf())
    private val template1 = Template(TemplateId(1), "testTemplate", listOf())
    private val template2 = Template(TemplateId(2), "testTemplate2", listOf())
    private val templateList = listOf(templateNon, template1, template2)
    private val templateMode1 = TemplateMode.Order("順番", 0)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val observerString = mock<Observer<String>>()
        val observerTemplateMode = mock<Observer<List<TemplateMode>>>()
        val observerBoolean = mock<Observer<Boolean>>()
        val observerTemplateList = mock<Observer<List<Template>>>()

        val templateUseCase = mockk<TemplateUseCase>().also {
            coEvery { it.getSpinnerTemplateAll() } returns flow { emit(templateList) }
        }
        val chatUseCase = mockk<ChatUseCase>().also {
            coEvery { it.getNextRoomId() } returns RoomId(1)
            coEvery { it.createRoom(any()) } returns Unit
        }
        viewModel = RoomAddViewModel(templateUseCase, chatUseCase)
        viewModel.templateTitleValue.observeForever(observerString)
        viewModel.titleText.observeForever(observerString)
        viewModel.templateModeList.observeForever(observerTemplateMode)
        viewModel.templateModeValue.observeForever(observerString)
        viewModel.isEnableTemplateMode.observeForever(observerBoolean)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.templateTitleList.observeForever(observerTemplateList)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region 新規作成ボタン

    /**
     * 新規作成ボタン
     * 条件：テンプレート選択欄で何も選択しない
     * 結果：テンプレート詳細設定がnullになること
     */
    @Test
    fun createByNoChoice() {
        viewModel.titleText.value = "test"
        runBlocking {
            val result = viewModel.createRoom()
            assertEquals(null, result.templateConfiguration)
        }
    }

    /**
     * 新規作成ボタン
     * 条件：テンプレート選択欄で”選択なし”を選択
     * 結果：テンプレート詳細設定がnullになること
     */
    @Test
    fun createByNoTemplate() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = templateNon.title
        runBlocking {
            val result = viewModel.createRoom()
            assertEquals(null, result.templateConfiguration)
        }
    }

    /**
     * 新規作成ボタン
     * 条件：テンプレート選択欄でテンプレートを選択
     * 結果：
     * ・選択したテンプレートが設定されていること
     * ・選択したテンプレート表示形式が設定されていること
     */
    @Test
    fun createByChoiceTemplate() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = template1.title
        viewModel.templateModeValue.value = templateMode1.message
        runBlocking {
            val result = viewModel.createRoom()
            assertNotEquals(null, result.templateConfiguration)
            assertEquals(template1.title, result.templateConfiguration!!.template.title)
            assertEquals(templateMode1, result.templateConfiguration!!.templateMode)
        }
    }

    // endregion

    // region 新規作成ボタンのバリデート

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態
     * 結果：falseが設定される
     */
    @Test
    fun validateByInit() {
        viewModel.changeSubmitButton()
        assertEquals(false, viewModel.isEnableSubmitButton.value!!)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが未入力
     * 結果：falseが設定される
     */
    @Test
    fun validateByTitleNull() {
        viewModel.titleText.value = ""
        viewModel.templateTitleValue.value = template1.title
        viewModel.templateModeValue.value = templateMode1.message
        viewModel.changeSubmitButton()
        assertEquals(false, viewModel.isEnableSubmitButton.value!!)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済み(他の設定なし)
     * 結果：trueが設定される
     */
    @Test
    fun validateByTitleNotNull() {
        viewModel.titleText.value = "test"
        viewModel.changeSubmitButton()
        assertEquals(true, viewModel.isEnableSubmitButton.value!!)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済みでテンプレートは選択なしが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByTitleNotNullAndTemplateNon() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = templateNon.title
        viewModel.changeSubmitButton()
        assertEquals(true, viewModel.isEnableSubmitButton.value!!)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済みでテンプレートが設定されているが表示形式が設定されていない
     * 結果：falseが設定される
     */
    @Test
    fun validateByTemplateNotNullAndTemplateModeNull() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = templateMode1.message
        viewModel.changeSubmitButton()
        assertEquals(false, viewModel.isEnableSubmitButton.value!!)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済みでテンプレートが設定されていて表示形式も設定されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByTemplateNotNullAndTemplateModeNotNull() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = templateMode1.message
        viewModel.changeSubmitButton()
        assertEquals(true, viewModel.isEnableSubmitButton.value!!)
    }

    // endregion

    // region テンプレート入力欄のバリデート

    /**
     * テンプレート選択のバリデート
     * 条件：テンプレート表示形式が選択されている状態でテンプレート選択蘭で選択なし以外を選択
     * 結果：テンプレート表示形式が空になり活性状態になる
     */
    @Test
    fun changedTemplateTitleByNotNone() {
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = templateMode1.message
        viewModel.templateTitleValue.value = "testTemplate2"
        assertEquals(templateMode1.message, viewModel.templateModeValue.value!!)
        assertEquals(true, viewModel.isEnableTemplateMode.value!!)
    }

    /**
     * テンプレート選択のバリデート
     * 条件：テンプレート表示形式が選択されている状態でテンプレート選択蘭で選択なしを選択
     * 結果：テンプレート表示形式が空になり非活性状態になる
     */
    @Test
    fun changedTemplateTitleValueByNonr() {
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = templateMode1.message
        viewModel.templateTitleValue.value = templateNon.title
        assertEquals("", viewModel.templateModeValue.value!!)
        assertEquals(false, viewModel.isEnableTemplateMode.value!!)
    }

    // endregion
}
