package com.example.chatmemo.ui.chat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.chatmemo.model.entity.ChatRoom
import com.example.chatmemo.model.entity.Comment
import com.example.chatmemo.model.entity.Phrase
import com.example.chatmemo.model.repository.DataBaseRepository
import com.nhaarman.mockito_kotlin.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * チャット画面　ロジック仕様
 */
class ChatViewModelTest {

    // LiveData用
    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    // observer
    private val observerBoolean = mock<Observer<Boolean>>()
    private val observerString = mock<Observer<String>>()
    private val observerComment = mock<Observer<List<Comment>>>()
    private val observerRoom = mock<Observer<ChatRoom>>()

    // mock
    private lateinit var viewModel: ChatViewModel
    private lateinit var databaseRepository: DataBaseRepository
    private val room1 = ChatRoom(1, "test", null, null, null, null, "")
    private val room2 = ChatRoom(2, "test", 1, 1, null, null, "")


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val comment1 = Comment(1,"first",1,"",1)
        val comment2 = Comment(2,"second",1,"",1)
        val phrase1 = Phrase(1,"first",1)
        val phrase2 = Phrase(2,"second",1)
        databaseRepository = mockk<DataBaseRepository>().also {
            coEvery { it.deleteRoom(any()) } returns Unit
            coEvery { it.deleteCommentByRoomId(any()) } returns Unit
            coEvery { it.getCommentAll(any()) } returns listOf(comment1,comment2)
            coEvery { it.getPhraseByTitle(any()) } returns listOf(phrase1,phrase2)
            coEvery { it.updateRoom(any()) } returns Unit
            coEvery { it.updateComment(any()) } returns Unit
            coEvery { it.addComment(any()) } returns Unit
            every { it.getRoomById(room1.id!!) } returns MutableLiveData(room1)
            every { it.getRoomById(room2.id!!) } returns MutableLiveData(room2)
        }
        viewModel = ChatViewModel(room1.id!!, databaseRepository)
        viewModel.commentList.observeForever(observerComment)
        viewModel.commentText.observeForever(observerString)
        viewModel.isEnableSubmitButton.observeForever(observerBoolean)
        viewModel.chatRoom.observeForever(observerRoom)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * ルーム更新
     */
    @Test
    fun updateRoom() {
        // 初回呼び出しのみコメント取得を行う
        viewModel.updateRoom(room1)
        viewModel.updateRoom(room1)
        coVerify(exactly = 1) { (databaseRepository).getCommentAll(any())}
        // 定型文の設定がある時のみ定型文の取得を行う
        viewModel = ChatViewModel(room1.id!!, databaseRepository)
        viewModel.updateRoom(room1)
        viewModel = ChatViewModel(room2.id!!, databaseRepository)
        viewModel.updateRoom(room2)
        coVerify(exactly = 1) { (databaseRepository).getPhraseByTitle(any()) }
    }

    /**
     * コメント送信
     */
    @Test
    fun submit() {
        viewModel.commentText.value = "test"
        viewModel.submit()
        coVerify { (databaseRepository).addComment(any()) }
        coVerify { (databaseRepository).updateRoom(any()) }
    }

    /**
     * ルーム削除
     */
    @Test
    fun deleteRoom() {
        viewModel.deleteRoom()
        coVerify { (databaseRepository).deleteRoom(any()) }
        coVerify { (databaseRepository).deleteCommentByRoomId(any()) }
    }

    /**
     * ユーザーチェンジ
     */
    @Test
    fun changeUser() {
        viewModel.changeUser()
        coVerify { (databaseRepository).updateComment(any()) }
    }

    /**
     * 送信ボタンの活性/非活性
     */
    @Test
    fun changeSubmitButton() {
        // 入力文字なし
        viewModel.changeSubmitButton("")
        assertEquals(viewModel.isEnableSubmitButton.value, false)
        // 入力文字あり
        viewModel.changeSubmitButton("test")
        assertEquals(viewModel.isEnableSubmitButton.value, true)
    }
}