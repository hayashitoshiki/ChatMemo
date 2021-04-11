package com.example.chatmemo.ui.template

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.TemplateId
import com.example.chatmemo.domain.model.value.TemplateMessage
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
 * 定型文追加・編集画面　ロジック仕様
 */
class FixedPhraseAddViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<String>>()
    private val observerPhrase = mock<Observer<MutableList<TemplateMessage>>>()

    // mock
    private lateinit var viewModel: TempalteAddViewModel
    private lateinit var templateUseCase: TemplateUseCase

    private val templateMesage = TemplateMessage("testTemplateMessage1")
    private val templateMessageList = listOf(templateMesage)
    private val templateId = TemplateId(1)
    private val template = Template(templateId, "template1", templateMessageList)


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        templateUseCase = mockk<TemplateUseCase>().also {
            coEvery { it.getTemplateMessageById(TemplateId(any())) } returns templateMessageList
            coEvery { it.getNextTemplateId() } returns templateId
            coEvery { it.createTemplate(any()) } returns true
            coEvery { it.updateTemplate(any()) } returns true
        }
    }

    // 新規作成モードで画面表示
    private fun initNew() {
        viewModel = TempalteAddViewModel(null, templateUseCase)
        viewModel.isPhraseEnableSubmitButton.observeForever(observerBoolean)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.phraseList.observeForever(observerPhrase)
        viewModel.submitState.observeForever(observerBoolean)
        viewModel.titleText.observeForever(observerString)
        viewModel.phraseText.observeForever(observerString)
        viewModel.submitText.observeForever(observerString)
    }

    // 編集モードで画面表示
    private fun initUpdate() {
        viewModel = TempalteAddViewModel(template, templateUseCase)
        viewModel.isPhraseEnableSubmitButton.observeForever(observerBoolean)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.phraseList.observeForever(observerPhrase)
        viewModel.submitState.observeForever(observerBoolean)
        viewModel.titleText.observeForever(observerString)
        viewModel.phraseText.observeForever(observerString)
        viewModel.submitText.observeForever(observerString)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region 初期化

    /**
     * 初期化
     * 条件：新規作成
     * 結果：ボタン名が新規作成、タイトルが空、リストが空
     */
    @Test
    fun initByNew() {
        initNew()
        assertEquals("", viewModel.titleText.value)
        assertEquals(mutableListOf<TemplateMessage>(), viewModel.phraseList.value)
        assertEquals("追加", viewModel.submitText.value)
    }

    /**
     * 初期化
     * 条件：新規作成
     * 結果：ボタン名が更新、タイトルが空、リストが空
     */
    @Test
    fun initByUpdate() {
        initUpdate()
        assertEquals(template.title, viewModel.titleText.value)
        assertEquals(template.templateMessageList, viewModel.phraseList.value)
        assertEquals("更新", viewModel.submitText.value)
    }

    // endregion

    // region送信ボタン

    /**
     * 送信ボタン
     * 条件：新規作成時
     * 結果：新規作成メソッドが呼ばれる
     */
    @Test
    fun submitByNew() {
        initNew()
        viewModel.submit()
        coVerify(exactly = 1) { (templateUseCase).createTemplate(any()) }
        coVerify(exactly = 0) { (templateUseCase).updateTemplate(any()) }
    }

    /**
     * 送信ボタン
     * 条件：更新時
     * 結果：更新メソッドが呼ばれる
     */
    @Test
    fun submitByUpdate() {
        initUpdate()
        viewModel.submit()
        coVerify(exactly = 0) { (templateUseCase).createTemplate(any()) }
        coVerify(exactly = 1) { (templateUseCase).updateTemplate(any()) }
    }

    // endregion

    // region テンプレートメッセージ追加

    /**
     * テンプレートメッセージ追加
     * 条件；なし
     * 結果：テンプレートリストが１増えていること、テンプレートリストの最後の値が入力した値になっていること
     */
    @Test
    fun addPhrase() {
        initNew()
        val oldSize = viewModel.phraseList.value!!.size
        viewModel.phraseText.value = "test"
        viewModel.addPhrase()
        assertEquals(oldSize + 1, viewModel.phraseList.value!!.size)
        assertEquals("test", viewModel.phraseList.value!!.last().massage)
    }

    // endregion

    // region テンプレートメッセージ追加ボタンバリデート

    /**
     * 定型文文章追加ボタン活性・非活性
     * 条件：初期状態
     * 結果：falseが返る
     */
    @Test
    fun changePhraseSubmitButtonByIni() {
        initNew()
        viewModel.phraseText.value = ""
        assertEquals(false, viewModel.isPhraseEnableSubmitButton.value)
    }

    /**
     * 定型文文章追加ボタン活性・非活性
     * 条件：テンプレートメッセージ入力なし
     * 結果：falseが返る
     */
    @Test
    fun changePhraseSubmitButtonByNone() {
        initNew()
        viewModel.phraseText.value = ""
        assertEquals(false, viewModel.isPhraseEnableSubmitButton.value)
    }

    /**
     * 定型文文章追加ボタン活性・非活性
     * 条件：テンプレートメッセージ入力あり
     * 結果：trueが返る
     */
    @Test
    fun changePhraseSubmitButtonByNotNone() {
        initNew()
        viewModel.phraseText.value = "test"
        assertEquals(true, viewModel.isPhraseEnableSubmitButton.value)
    }

    // endregion

    // region 登録・変更ボタンバリデート

    /**
     * 登録・変更ボタンバリデート
     * 条件：新規作成の初期状態
     * 結果：falseが返る
     */
    @Test
    fun chaneSubmitButtonByIni() {
        initNew()
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 登録・変更ボタンバリデート
     * 条件：タイトル設定なし && テンプレートコメント登録なし
     * 結果：falseが返る
     */
    @Test
    fun chaneSubmitButtonByTitleNoneAndTemplateListNone() {
        initNew()
        viewModel.titleText.value = "test"
        viewModel.titleText.value = ""
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 登録・変更ボタンバリデート
     * 条件：タイトル設定あり && テンプレートコメント登録なし
     * 結果：falseが返る
     */
    @Test
    fun chaneSubmitButtonByTitleNotNoneAndTemplateListNone() {
        initNew()
        viewModel.titleText.value = "test"
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 登録・変更ボタンバリデート
     * 条件：タイトル設定なし && テンプレートコメント登録あり
     * 結果：falseが返る
     */
    @Test
    fun chaneSubmitButtonByTitleNoneAndTemplateListNotNone() {
        initNew()
        viewModel.phraseText.value = "testTemplate"
        addPhrase()
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 登録・変更ボタンバリデート
     * 条件：タイトル設定あり && テンプレートコメント登録あり
     * 結果：tureが返る
     */
    @Test
    fun chaneSubmitButtonByTitleNotNoneAndTemplateListNotNone() {
        initNew()
        viewModel.titleText.value = "test"
        addPhrase()
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    // endregion
}