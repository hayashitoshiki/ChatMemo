package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.BaseUnitTest
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class LocalChatRepositoryImpTest : BaseUnitTest() {

    // mock
    private lateinit var repository: LocalChatRepositoryImp
    private lateinit var templateDao: com.myapp.chatmemo.data.database.dao.TemplateDao
    private lateinit var phraseDao: com.myapp.chatmemo.data.database.dao.PhraseDao
    private lateinit var commentDao: com.myapp.chatmemo.data.database.dao.CommentDao
    private lateinit var roomDao: com.myapp.chatmemo.data.database.dao.RoomDao

    // data
    private val templateTableSize = 3
    private val roomId1 = RoomId(1)
    private val stringDatatime = "2020-04-30T12:20:30.666"
    private val datatime = LocalDateTime.parse(stringDatatime)
    private val comment1 = Comment("comment1", User.BLACK, CommentDateTime(datatime))
    private val comment2 = Comment("comment2", User.WHITE, CommentDateTime(datatime))
    private val commentEntity1 = Converter.commentEntityFromComment(comment1, roomId1)
    private val commentEntity2 = Converter.commentEntityFromComment(comment2, roomId1)
    private val commentEntityList = listOf(commentEntity1, commentEntity2)
    private val commentList = mutableListOf(comment1, comment2)
    private val templateMessage1 = TemplateMessage("message1")
    private val templateMessage2 = TemplateMessage("message2")
    private val templateMessageList = listOf(templateMessage1, templateMessage2)
    private val template = Template(TemplateId(1), "Template1", templateMessageList)
    private val tempalteMessageEntity1 = Converter.templateMessageEntityFromTemplateAndMessage(template, templateMessage1)
    private val tempalteMessageEntity2 = Converter.templateMessageEntityFromTemplateAndMessage(template, templateMessage2)
    private val templateMessageEntityList = listOf(tempalteMessageEntity1, tempalteMessageEntity2)
    private val templateEntity = Converter.templateEntityFromTemplate(template)
    private val templateConfiguration = TemplateConfiguration(template, TemplateMode.Order("順番"))
    private val chatRoom = ChatRoom(roomId1, "Test1", templateConfiguration, commentList)
    private val chatRoomList = listOf(chatRoom)
    private val chatRoomEntity = Converter.chatEntityFromChat(chatRoom)
    private val chatRoomEntityFlow = flow { emit(chatRoomEntity) }
    private val chatRoomEntityList = listOf(chatRoomEntity)
    private val chatRoomEntityListFlow = flow { emit(listOf(chatRoomEntity)) }

    @Before
    fun setUp() {
        roomDao = mockk<com.myapp.chatmemo.data.database.dao.RoomDao>().also {
            coEvery { it.getNextId() } returns templateTableSize.toLong()
            coEvery { it.insert(any()) } returns 1L
            coEvery { it.update(any()) } returns Unit
            coEvery { it.deleteById(any()) } returns Unit
            coEvery { it.getAll() } returns chatRoomEntityListFlow
            coEvery { it.getRoomById(any()) } returns chatRoomEntityFlow
            coEvery { it.getRoomByTemplateId(any()) } returns chatRoomEntityList
        }
        commentDao = mockk<com.myapp.chatmemo.data.database.dao.CommentDao>().also {
            coEvery { it.insert(any()) } returns Unit
            coEvery { it.getCommentByDate(any()) } returns commentEntity1
            coEvery { it.deleteById(any()) } returns Unit
            coEvery { it.getAllCommentByRoom(any()) } returns commentEntityList
            coEvery { it.update(any()) } returns Unit
        }
        templateDao = mockk<com.myapp.chatmemo.data.database.dao.TemplateDao>().also {
            coEvery { it.getTemplateById(any()) } returns templateEntity
        }
        phraseDao = mockk<com.myapp.chatmemo.data.database.dao.PhraseDao>().also {
            coEvery { it.getAllByTitle(any()) } returns templateMessageEntityList
        }
        repository = LocalChatRepositoryImp(roomDao, commentDao, templateDao, phraseDao)
    }

    @After
    fun tearDown() {
    }

    // region チャットルームの次の連番取得

    /**
     * テンプレートIDの次の連番取得
     *
     * 条件；なし
     * 結果：DBから取得した最後の連番の次の連番を返すこと
     */
    @Test
    fun getNextTemplateId() {
        runBlocking {
            val result = repository.getNextRoomId().value
            assertEquals(templateTableSize + 1, result)
        }
    }

    // endregion

    // region チャットルーム作成

    /**
     * チャットルーム作成
     *
     * 条件；なし
     * 結果：
     * ・チャットルームテーブルへの登録メソッドが呼ばれること
     * ・渡したチャットルームオブジェクトがそのままEntityへ変換されて登録されること
     */
    @Test
    fun createRoom() {
        runBlocking {
            repository.createRoom(chatRoom)
            coVerify(exactly = 1) { (roomDao).insert(any()) }
        }
    }

    // endregion

    // region チャットルーム削除

    /**
     * チャットルーム削除
     *
     * 条件；なし
     * 結果：
     * ・チャットルームテーブルへの削除メソッドが呼ばれること
     * ・コメントテーブルへの削除メソッドが呼ばれること
     * ・渡したチャットルームIDを下に呼ばれること
     */
    @Test
    fun deleteRoom() {
        runBlocking {
            val roomId = chatRoom.roomId
            val roomIdLong = roomId.value.toLong()
            repository.deleteRoom(roomId)
            coVerify(exactly = 1) { (roomDao).deleteById(roomIdLong) }
            coVerify(exactly = 1) { (commentDao).deleteById(roomIdLong) }
        }
    }

    // endregion

    // region チャットルーム更新

    /**
     * チャットルーム更新
     *
     * 条件；なし
     * 結果：
     * ・チャットルームテーブルへの更新メソッドが呼ばれること
     * ・渡したチャットルームがEntityへ変換されて更新されること
     */
    @Test
    fun updateRoom() {
        runBlocking {
            repository.updateRoom(chatRoom)
            coVerify(exactly = 1) { (roomDao).update(chatRoomEntity) }
        }
    }

    // endregion

    // region チャットルームIDに一致するチャットルーム取得

    /**
     * チャットルームIDに一致するチャットルーム取得
     *
     * 条件；なし
     * 結果：
     * ・引数のルームIDに紐づくチャットルームを取得するメソッドが呼ばれること
     * ・取得したチャットルームEntityがドメインモデルへ変換されて返ること
     */
    @Test
    fun getRoomById() {
        runBlocking {
            val result = repository.getRoomById(chatRoom.roomId)
                .first()
            assertEquals(chatRoom, result)
        }
    }

    // endregion

    // region テンプレートIDに紐づくチャットルーム取得

    /**
     * テンプレートIDに一致するチャットルーム取得
     *
     * 条件；なし
     * 結果：
     * ・チャットルームテーブルへの更新メソッドが呼ばれること
     * ・取得したチャットルームEntityがドメインモデルへ変換されて返ること
     */
    @Test
    fun getRoomByTemplateId() {
        runBlocking {
            val result = repository.getRoomByTemplateId(template.templateId)
                .first()
            assertEquals(chatRoom, result)
        }
    }

    // region 全チャットルーム取得

    /**
     * 全チャットルーム取得
     *
     * 条件；なし
     * 結果：DBに登録されてーるチャットルームを全て取得し、ドメインモデルへ変換された値が返ること
     */
    @Test
    fun getRoomAll() {
        runBlocking {
            val result = repository.getRoomAll()
                .first()
            result.forEachIndexed { index, chatRoom ->
                assertEquals(chatRoomList[index], chatRoom)
            }
        }
    }

    // endregion

    // region コメント追加

    /**
     * コメント追加
     *
     * 条件；なし
     * 結果：
     * ・コメント追加メソッドが呼ばれること
     * ・渡した値がそのままEntityへ変換されて追加されること
     */
    @Test
    fun addComment() {
        runBlocking {
            repository.addComment(comment1, chatRoom.roomId)
            coVerify(exactly = 1) { (commentDao).insert(any()) }
        }
    }

    // endregion

    // region コメント更新

    /**
     * コメント更新
     *
     * 条件；なし
     * 結果：
     * ・コメントテーブルを更新するメソッドが渡したコメントの回数分行われること
     */
    @Test
    fun updateComments() {
        runBlocking {
            repository.updateComments(commentList)
            coVerify(exactly = commentList.size) { (commentDao).update(any()) }
        }
    }

    // endregion
}
