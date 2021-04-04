package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.domain.model.Template
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.example.chatmemo.domain.value.TemplateId
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 * ルーム作成顔面 ロジック仕様
 */
class ChatRoomAddViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // mock
    private lateinit var viewModel: RoomAddViewModel
    private val templateNon = Template(TemplateId(0), "選択なし", listOf())
    private val template1 = Template(TemplateId(1), "testTemplate", listOf())
    private val template2 = Template(TemplateId(2), "testTemplate2", listOf())
    private val templateList = listOf(template1, template2)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val observerString = mock<Observer<String>>()
        val observerInt = mock<Observer<Int>>()
        val observerBoolean = mock<Observer<Boolean>>()

        val templateUseCase = mockk<TemplateUseCase>().also {
            coEvery { it.getTemplateAll() } returns templateList
        }
        val chatUseCase = mockk<ChatUseCase>()
        viewModel = RoomAddViewModel(templateUseCase, chatUseCase)
        viewModel.templateTitleValue.observeForever(observerString)
        viewModel.titleText.observeForever(observerString)
        viewModel.tempalteModeValue.observeForever(observerString)
        viewModel.isEnableTemplateMode.observeForever(observerBoolean)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    // region 新規作成ボタンのバリデート

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態
     * 結果：falseが設定される
     */
    @Test
    fun validateByInit() {
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが未入力
     * 結果：falseが設定される
     */
    @Test
    fun validateByTitleNull() {
        viewModel.titleText.value = ""
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.tempalteModeValue.value = "順番"
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
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
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済みでテンプレートは選択なしが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByTitleNotNullAndTemplateNon() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = "選択なし"
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：タイトルが入力済みでテンプレートが設定されているが表示形式が設定されていない
     * 結果：falseが設定される
     */
    @Test
    fun validateByTemplateNotNullAndTemplateModeNull() {
        viewModel.titleText.value = "test"
        viewModel.templateTitleValue.value = "順番"
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, false)
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
        viewModel.tempalteModeValue.value = "順番"
        viewModel.changeSubmitButton()
        assertEquals(viewModel.isEnableSubmitButton.value!!, true)
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
        viewModel.tempalteModeValue.value = "順番"
        viewModel.templateTitleValue.value = "testTemplate2"
        assertEquals(viewModel.tempalteModeValue.value!!, "順番")
        assertEquals(viewModel.isEnableTemplateMode.value!!, true)
    }

    /**
     * テンプレート選択のバリデート
     * 条件：テンプレート表示形式が選択されている状態でテンプレート選択蘭で選択なしを選択
     * 結果：テンプレート表示形式が空になり非活性状態になる
     */
    @Test
    fun changedTemplateTitleValueByNonr() {
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.tempalteModeValue.value = "順番"
        viewModel.templateTitleValue.value = templateNon.title
        assertEquals(viewModel.tempalteModeValue.value!!, "")
        assertEquals(viewModel.isEnableTemplateMode.value!!, false)
    }

    // endregion

}