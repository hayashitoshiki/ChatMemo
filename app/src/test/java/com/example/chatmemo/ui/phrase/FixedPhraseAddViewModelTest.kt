package com.example.chatmemo.ui.phrase

/**
 * 定型文追加・編集画面　ロジック仕様
 */
class FixedPhraseAddViewModelTest {
    //
    //    // LiveData用
    //    @Rule
    //    @JvmField
    //    val rule: TestRule = InstantTaskExecutorRule()
    //
    //    // observer
    //    private val observerBoolean = mock<Observer<Boolean>>()
    //    private val observerString = mock<Observer<String>>()
    //    private val observerPhrase = mock<Observer<ArrayList<PhraseEntity>>>()
    //
    //    // mock
    //    private lateinit var viewModel: FixedPhraseAddViewModel
    //    private lateinit var databaseRepository: DataBaseRepository
    //
    //    private val template1 = TemplateEntity(1, "test")
    //    private val template2 = TemplateEntity(2, "test")
    //
    //    @ExperimentalCoroutinesApi
    //    @Before
    //    fun setUp() {
    //        Dispatchers.setMain(Dispatchers.Unconfined)
    //        val phrase1 = PhraseEntity(1, "first", 1)
    //        val phrase2 = PhraseEntity(2, "second", 1)
    //        val room1 = ChatRoomEntity(1, "test", null, null, null, null, "")
    //        val room2 = ChatRoomEntity(1, "test", 1, 1, "", "", "")
    //        databaseRepository = mockk<DataBaseRepository>().also {
    //            coEvery { it.createTemplate(any()) } returns true
    //            coEvery { it.updateTemplate(any()) } returns true
    //            coEvery { it.addPhrase(any()) } returns true
    //            coEvery { it.updatePhrase(any(), any()) } returns true
    //            coEvery { it.getPhraseByTitle(any()) } returns listOf(phrase1, phrase2)
    //            coEvery { it.getTemplateByTitle(any()) } returns template1
    //            coEvery { it.getPhraseTitle() } returns listOf(template1, template2)
    //            coEvery { it.getTemplateById(template1.id!!) } returns template1
    //            coEvery { it.getRoomByTemplateId(1) } returns listOf(room1, room2)
    //            coEvery { it.getRoomByTemplateId(2) } returns listOf()
    //            coEvery { it.deletePhraseByTitle(any()) } returns true
    //            coEvery { it.deleteTemplateTitle(any()) } returns Unit
    //        }
    //        viewModel = FixedPhraseAddViewModel(databaseRepository)
    //        viewModel.isPhraseEnableSubmitButton.observeForever(observerBoolean)
    //        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
    //        viewModel.phraseList.observeForever(observerPhrase)
    //        viewModel.submitState.observeForever(observerBoolean)
    //        viewModel.titleText.observeForever(observerString)
    //        viewModel.phraseText.observeForever(observerString)
    //        viewModel.submitText.observeForever(observerString)
    //    }
    //
    //    @ExperimentalCoroutinesApi
    //    @After
    //    fun tearDown() {
    //        Dispatchers.resetMain()
    //    }
    //
    //    /**
    //     * 初期化処理
    //     */
    //    @Test
    //    fun init() {
    //        // 既存更新
    //        viewModel.init(null)
    //        assertEquals(viewModel.submitText.value, "追加")
    //        // 新規登録
    //        viewModel.init(template1)
    //        assertEquals(viewModel.submitText.value, "更新")
    //    }
    //
    //    /**
    //     * 送信ボタン
    //     */
    //    @Test
    //    fun submit() {
    //        // 新規登録
    //        viewModel.init(null)
    //        viewModel.submit()
    //        coVerify { (databaseRepository).createTemplate(any()) }
    //        coVerify { (databaseRepository).addPhrase(any()) }
    //        // 既存更新
    //        viewModel.init(template1)
    //        viewModel.submit()
    //        coVerify { (databaseRepository).updateTemplate(any()) }
    //        coVerify { (databaseRepository).updatePhrase(any(), any()) }
    //    }
    //
    //    /**
    //     * 定型文リスト追加
    //     */
    //    @Test
    //    fun addPhrase() {
    //        val oldSize = viewModel.phraseList.value!!.size
    //        viewModel.addPhrase()
    //        assertEquals(viewModel.phraseList.value!!.size, oldSize + 1)
    //    }
    //
    //    /**
    //     * 定型文文章追加ボタン活性・非活性
    //     */
    //    @Test
    //    fun changePhraseSubmitButton() {
    //        // 入力なし
    //        //viewModel.changePhraseSubmitButton("")
    //        assertEquals(viewModel.isPhraseEnableSubmitButton.value, false)
    //        // 入力あり
    //        viewModel.phraseText.value = "test"
    //        // viewModel.changePhraseSubmitButton("test")
    //        assertEquals(viewModel.isPhraseEnableSubmitButton.value, true)
    //    }
    //
    //    /**
    //     * 登録・更新ボタン活性・非活性制御
    //     */
    //    @Test
    //    fun changeSubmitButton() {
    //        // タイトルなし + 定型文リストなし
    //        viewModel.titleText.value = ""
    //        //viewModel.changeSubmitButton()
    //        assertEquals(viewModel.isEnableSubmitButton.value, false)
    //        // タイトルあり + 定型文リストなし
    //        viewModel.titleText.value = "aaa"
    //        //viewModel.changeSubmitButton()
    //        assertEquals(viewModel.isEnableSubmitButton.value, false)
    //        // タイトルなし + 定型文リストあり
    //        viewModel.titleText.value = ""
    //        viewModel.addPhrase()
    //        //viewModel.changeSubmitButton()
    //        assertEquals(viewModel.isEnableSubmitButton.value, false)
    //        // タイトルあり + 定型文リストあり
    //        viewModel.titleText.value = "aaa"
    //        viewModel.addPhrase()
    //        //viewModel.changeSubmitButton()
    //        assertEquals(viewModel.isEnableSubmitButton.value, true)
    //    }
}