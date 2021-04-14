package com.example.chatmemo.ui.phrase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Template
import com.example.chatmemo.model.repository.DataBaseRepository
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

/**
 * 定型文一覧画面　ロジック仕様
 */
class FixedPhraseListViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerTemplate = mock<Observer<List<Template>>>()

    // mock
    private lateinit var viewModel: FixedPhraseListViewModel
    private lateinit var databaseRepository: DataBaseRepository

    private val template1 = Template(1, "test")
    private val template2 = Template(2, "test")

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val room1 = ChatRoom(1, "test", null, null, null, null, "")
        val room2 = ChatRoom(1, "test", 1, 1, "", "", "")
        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.getPhraseTitle() } returns listOf(template1, template2)
            coEvery { it.getRoomByTemplateId(1) } returns listOf(room1, room2)
            coEvery { it.getRoomByTemplateId(2) } returns listOf()
            coEvery { it.deletePhraseByTitle(any()) } returns true
            coEvery { it.deleteTemplateTitle(any()) } returns Unit
        }
        viewModel = FixedPhraseListViewModel(databaseRepository)
        viewModel.phraseList.observeForever(observerTemplate)
        viewModel.status.observeForever(observerBoolean)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * リスト取得
     */
    @Test
    fun getList() {
        viewModel.getList()
        coVerify { (databaseRepository).getPhraseTitle() }
    }

    /**
     * 定型文削除
     */
    @Test
    fun deletePhrase() {
        // 使用しているルームが存在する
        viewModel.deletePhrase(template1)
        assertEquals(viewModel.status.value, false)
        // 使用していルームが存在しいない
        viewModel.deletePhrase(template2)
        assertEquals(viewModel.status.value, true)
        coVerify { (databaseRepository).deletePhraseByTitle(any()) }
        coVerify { (databaseRepository).deleteTemplateTitle(any()) }
    }
}
