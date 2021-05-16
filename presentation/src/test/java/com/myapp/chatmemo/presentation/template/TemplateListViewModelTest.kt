package com.myapp.chatmemo.presentation.template

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.TemplateId
import com.myapp.chatmemo.domain.usecase.TemplateUseCase
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TemplateListViewModelTest {

    // region Coroutine関連

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    // endregion

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TemplateListViewModel
    private lateinit var templateUseCase: TemplateUseCase

    // region data

    private val templateId1 = TemplateId(1)

    private val template = Template(templateId1, "template1", listOf())
    private val chatRoomList = listOf(template)
    private val chatRoomListEmpty = listOf<Template>()
    private val templateListFlow = flow { emit(chatRoomList) }
    private val templateListEmptyFlow = flow { emit(chatRoomListEmpty) }

    // endregion

    private val observerTemplateList = mock<Observer<List<Template>>>()
    private val observerBoolean = mock<Observer<Boolean>>()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        templateUseCase = mockk<TemplateUseCase>().also {
            every { it.getTemplateAll() } returns templateListFlow
            coEvery { it.deleteTemplate(TemplateId(any())) } returns true
        }
        viewModel = TemplateListViewModel(templateUseCase)
        initObserver()
    }

    private fun initObserver() {
        viewModel.templateList.observeForever(observerTemplateList)
        viewModel.isNoDataText.observeForever(observerBoolean)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // region テンプレートリストなし文言表示制御

    /**
     * テンプレートリストなし文言表示制御
     *
     * 条件：取得したテンプレートリストが０件
     * 結果：テンプレートリストなし文言が表示される
     */
    @Test
    fun changeNoDataTextByNon() {
        templateUseCase = mockk<TemplateUseCase>().also {
            every { it.getTemplateAll() } returns templateListEmptyFlow
        }
        viewModel = TemplateListViewModel(templateUseCase)
        initObserver()
        val result = viewModel.isNoDataText.value
        assertEquals(true, result)
    }

    /**
     * テンプレートリストなし文言表示制御
     *
     * 条件：取得したテンプレートリストが１件以上
     * 結果：テンプレートリストなし文言が表示されない
     */
    @Test
    fun changeNoDataTextByNotNon() {
        val result = viewModel.isNoDataText.value
        assertEquals(false, result)
    }

    // endregion

    // region テンプレート削除

    /**
     * テンプレート削除
     *
     * 条件：なし
     * 結果：テンプレートリスト削除メソッドが呼ばれること
     */
    @Test
    fun deleteTemplate() {
        viewModel.deletePhrase(template)
        coVerify { (templateUseCase).deleteTemplate(template.templateId) }
    }

    // endregion
}