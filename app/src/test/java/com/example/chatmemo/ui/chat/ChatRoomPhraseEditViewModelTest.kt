package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.BaseUnitTest
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.*
import com.example.chatmemo.domain.usecase.ChatUseCase
import com.example.chatmemo.domain.usecase.TemplateUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * ルーム定型文設定画面　ロジック仕様
 */
class ChatRoomPhraseEditViewModelTest : BaseUnitTest() {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<String>>()
    private val observerTemplateList = mock<Observer<List<Template>>>()

    // mock
    private lateinit var viewModel: RoomPhraseEditViewModel
    private lateinit var chatUseCase: ChatUseCase
    private lateinit var templateUseCase: TemplateUseCase
    private val templateNon = Template(TemplateId(0), "選択なし", listOf())
    private val template1 = Template(TemplateId(1), "testTemplate1", listOf())
    private val template2 = Template(TemplateId(2), "testTemplate2", listOf())
    private val templateList = listOf(templateNon, template1, template2)

    private val roomId1 = RoomId(1)
    private val roomId2 = RoomId(2)
    private val title = "testRoom"
    private val templateMessageList = listOf<TemplateMessage>()
    private val template = Template(TemplateId(1), "testTemplate1", templateMessageList)
    private val templateMode1 = TemplateMode.Order("順番", 0)
    private val templateConfiguration1 = TemplateConfiguration(template, templateMode1)
    private val comment1 = Comment("testComment1", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val comment2 = Comment("testComment2", User.WHITE, CommentDateTime(LocalDateTime.now()))
    private val comment3 = Comment("testComment3", User.BLACK, CommentDateTime(LocalDateTime.now()))
    private val commentList = mutableListOf(comment1, comment2, comment3)
    private val chatroom1 = ChatRoom(roomId1, title, null, commentList)
    private val chatroom2 = ChatRoom(roomId2, title, templateConfiguration1, commentList)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        templateUseCase = mockk<TemplateUseCase>().also {
            coEvery { it.getSpinnerTemplateAll() } returns flow { emit(templateList) }
            coEvery { it.getTemplateAll() } returns flow { emit(templateList) }
        }
        chatUseCase = mockk()
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    private fun setChatRoom1() {
        viewModel = RoomPhraseEditViewModel(chatroom1, templateUseCase, chatUseCase)
        initObserber()
    }

    private fun setChatRoom2() {
        viewModel = RoomPhraseEditViewModel(chatroom2, templateUseCase, chatUseCase)
        initObserber()
    }

    private fun initObserber() {
        viewModel.templateTitleValue.observeForever(observerString)
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

    // region 初期化
    /**
     * 新規作成ボタン活性バリデート
     * 条件：選択なしの場合の初期状態
     * 結果：nullが設定される
     */
    @Test
    fun initByNone() {
        setChatRoom1()
        assertEquals(null, viewModel.templateTitleValue.value)
        assertEquals(null, viewModel.templateModeValue.value)
        assertEquals(null, viewModel.isEnableTemplateMode.value)
        assertEquals(null, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：選択済みの場合の初期状態
     * 結果：falseが設定される
     */
    @Test
    fun initByChoice() {
        setChatRoom2()
        assertEquals(template.title, viewModel.templateTitleValue.value)
        assertEquals(templateMode1.message, viewModel.templateModeValue.value)
        assertEquals(true, viewModel.isEnableTemplateMode.value)
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    // endregion

    // region 新規作成ボタンのバリデート

    /**
     * 新規作成ボタン活性バリデート
     * 条件：選択なしの場合の初期状態
     * 結果：nullが設定される
     */
    @Test
    fun validateInitByNone() {
        setChatRoom1()
        assertEquals(null, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：選択済みの場合の初期状態
     * 結果：falseが設定される
     */
    @Test
    fun validateInitByChoice() {
        setChatRoom2()
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択なしの状態でテンプレートは選択なしが選択されている
     * 結果：falseが設定される
     */
    @Test
    fun validateByNonAndTitleNull() {
        setChatRoom1()
        viewModel.templateTitleValue.value = "選択なし"
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択なしの状態でテンプレートが設定されていて表示形式が選択されていない
     * 結果：falseが設定される
     */
    @Test
    fun validateByNonAndTitleNotNullAndTemplateModeNull() {
        setChatRoom1()
        viewModel.templateTitleValue.value = "testTemplate"
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択なしの状態でテンプレートが設定されていて表示形式も選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByNoneAndTitleNotNullAndTemplateModeNotNull() {
        runBlocking {
            setChatRoom1()
            viewModel.templateTitleValue.value = "testTemplate"
            viewModel.templateModeValue.value = "順番"
            assertEquals(true, viewModel.isEnableSubmitButton.value)
        }
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されている状態でテンプレートは選択なしが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByChoiseAndTitleNull() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "選択なし"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されている状態でテンプレートが設定されていて表示形式が選択されていない
     * 結果：falseが設定される
     */
    @Test
    fun validateByChoiseAndTitleNotNullAndTemplateModeNull() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "testTemplate2"
        viewModel.templateModeValue.value = null
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されているの状態で同じテンプレートが設定されていて表示形式も同じものが選択されている
     * 結果：falseが設定される
     */
    @Test
    fun validateByChoiseAndTitleEqualAndTemplateModeEqual() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = "順番"
        assertEquals(false, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されているの状態で同じテンプレートが設定されていて表示形式も違うものが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByChoiseAndTitleEqualAndTemplateModeNotEqual() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = "ランダム"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されているの状態で違うテンプレートが設定されていて表示形式も同じものが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByChoiseAndTitleNotEqualAndTemplateModeEqual() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "testTemplate2"
        viewModel.templateModeValue.value = "順番"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
    }

    /**
     * 新規作成ボタン活性バリデート
     * 条件：初期状態が選択されているの状態で違うテンプレートが設定されていて表示形式も違うものが選択されている
     * 結果：trueが設定される
     */
    @Test
    fun validateByChoiseAndTitleNorEqualAndTemplateModeNotEqual() {
        setChatRoom2()
        viewModel.templateTitleValue.value = "testTemplate2"
        viewModel.templateModeValue.value = "ランダム"
        assertEquals(true, viewModel.isEnableSubmitButton.value)
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
        setChatRoom1()
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = "順番"
        viewModel.templateTitleValue.value = "testTemplate2"
        assertEquals("順番", viewModel.templateModeValue.value)
        assertEquals(true, viewModel.isEnableTemplateMode.value)
    }

    /**
     * テンプレート選択のバリデート
     * 条件：テンプレート表示形式が選択されている状態でテンプレート選択蘭で選択なしを選択
     * 結果：テンプレート表示形式が空になり非活性状態になる
     */
    @Test
    fun changedTemplateTitleValueByNonr() {
        setChatRoom1()
        viewModel.templateTitleValue.value = "testTemplate1"
        viewModel.templateModeValue.value = "順番"
        viewModel.templateTitleValue.value = templateNon.title
        assertEquals("", viewModel.templateModeValue.value)
        assertEquals(false, viewModel.isEnableTemplateMode.value)
    }

    // endregion
}
