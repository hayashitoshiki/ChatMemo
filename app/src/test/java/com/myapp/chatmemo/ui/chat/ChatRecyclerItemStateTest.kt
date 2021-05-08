package com.myapp.chatmemo.ui.chat

import com.myapp.chatmemo.BaseUnitTest
import com.myapp.chatmemo.domain.model.value.Comment
import com.myapp.chatmemo.domain.model.value.CommentDateTime
import com.myapp.chatmemo.domain.model.value.User
import com.myapp.chatmemo.ui.utils.expansion.toLocalDateTime
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * チャット画面_コメントリストアイテム ロジック使用
 */
class ChatRecyclerItemStateTest : BaseUnitTest() {

    // mock
    private lateinit var chatRecyclerItemState: ChatRecyclerItemState

    // data
    private val stringDatatime1 = "2020/04/29 12:20:30.666"
    private val stringDatatime2 = "2020/04/30 12:20:30.666"
    private val datatime1 = stringDatatime1.toLocalDateTime()
    private val datatime2 = stringDatatime2.toLocalDateTime()
    private val commentday1 = Comment("day1", User.BLACK, CommentDateTime(datatime1))
    private val commentday2 = Comment("day2", User.BLACK, CommentDateTime(datatime2))

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // region ヘッダーセクションの表示制御

    /**
     * ヘッダーセクションの表示制御
     * 条件：１つ目のコメント
     * 結果：表示
     */
    @Test
    fun isHeaderByFirstComment() {
        chatRecyclerItemState = ChatRecyclerItemState(null, commentday1)
        val result = chatRecyclerItemState.isHeader
        assertEquals(true, result)
    }

    /**
     * ヘッダーセクションの表示制御
     * 条件：現在のコメントと１つ前のコメントの投稿日時が同じ
     * 結果：非表示
     */
    @Test
    fun isHeaderBySameDate() {
        chatRecyclerItemState = ChatRecyclerItemState(commentday1, commentday1)
        val result = chatRecyclerItemState.isHeader
        assertEquals(false, result)
    }

    /**
     * ヘッダーセクションの表示制御
     * 条件：現在のコメントと１つ前のコメントの投稿日時が異なる
     * 結果：表示
     */
    @Test
    fun isHeaderByNotSameDate() {
        chatRecyclerItemState = ChatRecyclerItemState(commentday1, commentday2)
        val result = chatRecyclerItemState.isHeader
        assertEquals(true, result)
    }

    // endregion
}