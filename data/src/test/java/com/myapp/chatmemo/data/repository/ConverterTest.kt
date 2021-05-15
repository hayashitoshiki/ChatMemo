package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.BaseUnitTest
import com.myapp.chatmemo.data.database.entity.ChatRoomEntity
import com.myapp.chatmemo.data.database.entity.CommentEntity
import com.myapp.chatmemo.data.database.entity.TemplateMessageEntity
import com.myapp.chatmemo.data.database.entity.TemplateTitleEntity
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

/**
 * ドメインモデルのコンバーターのロジック仕様
 */
class ConverterTest : BaseUnitTest() {

    // mock
    private val converter = Converter

    // data
    private val phraseEntity1 = TemplateMessageEntity(1, "phraseEntity1", 1)
    private val phraseEntityList = listOf(phraseEntity1)
    private val templateMessage1 = TemplateMessage("message1")
    private val templateMessage2 = TemplateMessage("message2")
    private val templateMessageList = listOf(templateMessage1, templateMessage2)
    private val template = Template(TemplateId(1), "Template1", templateMessageList)
    private val templateTitleEntity = TemplateTitleEntity(1, "templateTitle1")
    private val template1 = Template(TemplateId(1), "template1", templateMessageList)
    private val stringDatatime = "2020-04-30T12:20:30.666"
    private val datatime = LocalDateTime.parse(stringDatatime)
    private val chatRoomId1 = RoomId(1)
    private val comment1 = Comment("comment1", User.BLACK, CommentDateTime(datatime))
    private val commentList = mutableListOf(comment1)
    private val commentListByEmpty = mutableListOf<Comment>()
    private val commentEntity1 = CommentEntity(1, "commentEntity1", 1, datatime, 1)
    private val commentEntityList = listOf(commentEntity1)
    private val templateByOrder = TemplateMode.Order("順番")
    private val templateByRandam = TemplateMode.Randam("ランダム")
    private val templateConfigurationByOrder = TemplateConfiguration(template, templateByOrder)
    private val templateConfigurationByRandam = TemplateConfiguration(template, templateByRandam)
    private val chatRoomByTemplateNull = ChatRoom(RoomId(1), "TemplateNull", null, commentList)
    private val chatRoomByOrder = ChatRoom(RoomId(2), "Order", templateConfigurationByOrder, commentList)
    private val chatRoomByRandam = ChatRoom(RoomId(3), "Randam", templateConfigurationByRandam, commentList)
    private val chatRoomByCommentNull = ChatRoom(RoomId(4), "CommentNull", templateConfigurationByOrder, commentListByEmpty)
    private val chatRoomByCommentNotNull = ChatRoom(RoomId(4), "CommentNotNull", templateConfigurationByOrder, commentList)
    private val chatRoomEntityByTemplateNull =
        ChatRoomEntity(1, "templateNull", null, null, null, comment1.message, comment1.time.date)
    private val chatRoomEntityByOrder = ChatRoomEntity(
        1, "templateOrder", 1, templateByOrder.getInt(), templateByOrder.position.toString(), comment1.message,
        comment1.time.date
    )
    private val chatRoomEntityByRandamInit =
        ChatRoomEntity(1, "templateRandam", 1, templateByRandam.getInt(), "", comment1.message, comment1.time.date)
    private val chatRoomEntityByRandam =
        ChatRoomEntity(1, "templateRandam", 1, templateByRandam.getInt(), ",2", comment1.message, comment1.time.date)

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    // region  テンプレート文Entityからテンプレート文オブジェクトへ変換

    /**
     * テンプレート文Entityからテンプレート文オブジェクトへ変換
     *
     * 条件：なし
     * 結果：テンプレート文Entityのメッセージがテンプレート文オブジェクトのメッセージに格納されること
     */
    @Test
    fun templateMessageFromPharaseEntity() {
        val result = Converter.templateMessageFromPharaseEntity(phraseEntity1)
        assertEquals(phraseEntity1.text, result.massage)
    }

    // endregion

    // region  テンプレート文オブジェクトからテンプレート文Entityへ変換

    /**
     * テンプレートオブジェクトとテンプレート文オブジェクトからテンプレート文Entityへ変換
     *
     * 条件：なし
     * 結果：
     * ・テンプレート文EntityのIDがnullになること
     * ・テンプレート文オブジェクトのメッセージがテンプレート文Entityのメッセージに格納されること
     * ・テンプレートオブジェクトのIDがテンプレート文EntityのテンプレートIDへ格納されること
     */
    @Test
    fun praseEntityFromTemplateAndMessage() {
        val result = Converter.templateMessageEntityFromTemplateAndMessage(template1, templateMessage1)
        assertEquals(null, result.id)
        assertEquals(templateMessage1.massage, result.text)
        assertEquals(template1.templateId.value.toLong(), result.templateId)
    }

    // endregion

    // region  テンプレートオブジェクトからからテンプレート文Entityへ変換

    /**
     * テンプレート文Entityからテンプレート文オブジェクトへ変換
     *
     * 条件：なし
     * 結果：
     * ・テンプレートオブジェクトのIDがテンプレートEntityのIDへ格納されること
     * ・テンプレートオブジェクトのタイトルがテンプレートEntityのタイトルに格納されること
     */
    @Test
    fun templateEntityFromTemplate() {
        val result = Converter.templateEntityFromTemplate(template1)
        assertEquals(template1.templateId.value.toLong(), result.id)
        assertEquals(template1.title, result.title)
    }

    // endregion

    // region  テンプレートEntityとテンプレート文Entityからテンプレートオブジェクトへ変換

    /**
     * テンプレートEntityとテンプレート文Entityからテンプレートオブジェクトへ変換
     *
     * 条件：なし
     * 結果：
     * ・テンプレートオブジェクトのIDがテンプレートEntityのIDへ格納されること
     * ・テンプレートオブジェクトのタイトルがテンプレートEntityのタイトルに格納されること
     */
    @Test
    fun templateFromTemplateEntityAndPhraseEntity() {
        val result = Converter.templateFromTemplateEntityAndPhraseEntity(templateTitleEntity, phraseEntityList)
        val templateMessageList = phraseEntityList.map { Converter.templateMessageFromPharaseEntity(it) }
        assertEquals(templateTitleEntity.id!!.toInt(), result.templateId.value)
        assertEquals(templateTitleEntity.title, result.title)
        assertEquals(templateMessageList, result.templateMessageList)
    }

    // endregion

    // region コメントオブジェクトからコメントEntityへ変換

    /**
     * コメントオブジェクトからコメントEntityへ変換
     *
     * 条件：なし
     * 結果：
     * ・コメントEntityのIDがnull
     * ・コメントEntityのメッセージがコメントオブジェクトのメッセージ
     * ・コメントEntityのユーザがコメントオブジェクトのユーザ
     * ・コメントEntityのコメント時間がコメントオブジェクトのコメント時間
     * ・コメントEntityのルームIDがテンプレートオブジェクトのルームID
     */
    @Test
    fun commentEntityFromComment() {
        val result = Converter.commentEntityFromComment(comment1, chatRoomId1)
        assertEquals(null, result.id)
        assertEquals(comment1.message, result.text)
        assertEquals(comment1.user.chageInt(), result.user)
        assertEquals(comment1.time.date, result.commentTime)
        assertEquals(chatRoomId1.value.toLong(), result.roomId)
    }

    // endregion

    // region コメントオブジェクトからコメントEntityへ変換

    /**
     * コメントEntityからコメントオブジェクトへ変換
     *
     * 条件：なし
     * 結果：
     * ・テンプレートオブジェクトのIDがテンプレートEntityのID
     * ・テンプレートオブジェクトのタイトルがテンプレートEntityのタイトル
     */
    @Test
    fun commentFromCommentEntity() {
        val result = Converter.commentFromCommentEntity(commentEntity1)
        assertEquals(commentEntity1.text, result.message)
        assertEquals(User.getUser(commentEntity1.user), result.user)
        assertEquals(commentEntity1.commentTime, result.time.date)
    }

    // endregion

    // region ChatRoomモデルからChatRoomEntityへ変換

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * 条件：テンプレートなし
     * 結果：
     * ・チャットルームEntityのIdがチャットルームオブジェクトのルームID
     * ・チャットルームEntityのタイトルへチャットルームオブジェクトのタイトル
     * ・チャットルームEntityのテンプレートIDへnull
     * ・チャットルームEntityのテンプレートモードへnull
     * ・チャットルームEntityのテンプレートポジションへnull
     * ・チャットルームEntityの最終コメントがチャットルームオブジェクトのコメントリストの最後のメッセージ
     * ・チャットルームEntityの最終コメント時間がチャットルームオブジェクトのコメントリストの最後のコメントの時間
     */
    @Test
    fun chatEntityFromChatByTemplateNull() {
        val result = Converter.chatEntityFromChat(chatRoomByTemplateNull)
        val id = chatRoomByTemplateNull.roomId.value.toLong()
        val title = chatRoomByTemplateNull.title
        val templateId = null
        val templateMode = null
        val templatePosition = null
        val lastCommentMessage = chatRoomByTemplateNull.commentList.last().message
        val lastCommentTime = chatRoomByTemplateNull.commentList.last().time.date
        assertEquals(id, result.id)
        assertEquals(title, result.title)
        assertEquals(templateId, result.templateId)
        assertEquals(templateMode, result.templateMode)
        assertEquals(templatePosition, result.phrasePoint)
        assertEquals(lastCommentMessage, result.commentLast)
        assertEquals(lastCommentTime, result.commentTime)
    }

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * 条件：テンプレートの表示モードが順番
     * 結果：
     * ・チャットルームEntityのIdがチャットルームオブジェクトのルームID
     * ・チャットルームEntityのタイトルがチャットルームオブジェクトのタイトル
     * ・チャットルームEntityのテンプレートIDがチャットルームオブジェクトのテンプレートID
     * ・チャットルームEntityのテンプレートモードがチャットルームオブジェクトのテンプレートモード
     * ・チャットルームEntityのテンプレートポジションへチャットルームオブジェクトのテンプレートポジション
     * ・チャットルームEntityの最終コメントへチャットルームオブジェクトのコメントリストの最後のメッセージ
     * ・チャットルームEntityの最終コメント時間がチャットルームオブジェクトのコメントリストの最後のコメントの時間
     */
    @Test
    fun chatEntityFromChatByOrder() {
        val result = Converter.chatEntityFromChat(chatRoomByOrder)
        val id = chatRoomByOrder.roomId.value.toLong()
        val title = chatRoomByOrder.title
        val templateId = chatRoomByOrder.templateConfiguration!!.template.templateId.value.toLong()
        val templateMode = chatRoomByOrder.templateConfiguration!!.templateMode.getInt()
        val templatePosition = (chatRoomByOrder.templateConfiguration!!.templateMode as TemplateMode.Order).position.toString()
        val lastCommentMessage = chatRoomByOrder.commentList.last().message
        val lastCommentTime = chatRoomByOrder.commentList.last().time.date
        assertEquals(id, result.id)
        assertEquals(title, result.title)
        assertEquals(templateId, result.templateId)
        assertEquals(templateMode, result.templateMode)
        assertEquals(templatePosition, result.phrasePoint)
        assertEquals(lastCommentMessage, result.commentLast)
        assertEquals(lastCommentTime, result.commentTime)
    }

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * 条件：テンプレートの表示モードがランダム
     * 結果：
     * ・チャットルームEntityのIdがチャットルームオブジェクトのルームID
     * ・チャットルームEntityのタイトルがチャットルームオブジェクトのタイトル
     * ・チャットルームEntityのテンプレートIDがチャットルームオブジェクトのテンプレートID
     * ・チャットルームEntityのテンプレートモードがチャットルームオブジェクトのテンプレートモード
     * ・チャットルームEntityのテンプレートポジションがチャットルームオブジェクトのテンプレートポジション
     * ・チャットルームEntityの最終コメントがチャットルームオブジェクトのコメントリストの最後のメッセージ
     * ・チャットルームEntityの最終コメント時間がチャットルームオブジェクトのコメントリストの最後のコメントの時間
     */
    @Test
    fun chatEntityFromChatByRandam() {
        val result = Converter.chatEntityFromChat(chatRoomByRandam)
        val id = chatRoomByRandam.roomId.value.toLong()
        val title = chatRoomByRandam.title
        val templateId = chatRoomByRandam.templateConfiguration!!.template.templateId.value.toLong()
        val templateMode = chatRoomByRandam.templateConfiguration!!.templateMode.getInt()
        val templatePosition =
            (chatRoomByRandam.templateConfiguration!!.templateMode as TemplateMode.Randam).position.joinToString(",")
        val lastCommentMessage = chatRoomByRandam.commentList.last().message
        val lastCommentTime = chatRoomByRandam.commentList.last().time.date
        assertEquals(id, result.id)
        assertEquals(title, result.title)
        assertEquals(templateId, result.templateId)
        assertEquals(templateMode, result.templateMode)
        assertEquals(templatePosition, result.phrasePoint)
        assertEquals(lastCommentMessage, result.commentLast)
        assertEquals(lastCommentTime, result.commentTime)
    }

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * 条件：コメントが投稿済
     * 結果：
     * ・チャットルームEntityのIdがチャットルームオブジェクトのルームID
     * ・チャットルームEntityのタイトルがチャットルームオブジェクトのタイトル
     * ・チャットルームEntityのテンプレートIDがチャットルームオブジェクトのテンプレートID
     * ・チャットルームEntityのテンプレートモードがチャットルームオブジェクトのテンプレートモード
     * ・チャットルームEntityのテンプレートポジションがチャットルームオブジェクトのテンプレートポジション
     * ・チャットルームEntityの最終コメントがチャットルームオブジェクトのコメントリストの最後のメッセージ
     * ・チャットルームEntityの最終コメント時間がチャットルームオブジェクトのコメントリストの最後のコメントの時間
     */
    @Test
    fun chatEntityFromChatByCommentNotNull() {
        val result = Converter.chatEntityFromChat(chatRoomByCommentNotNull)
        val id = chatRoomByCommentNotNull.roomId.value.toLong()
        val title = chatRoomByCommentNotNull.title
        val templateId = chatRoomByCommentNotNull.templateConfiguration!!.template.templateId.value.toLong()
        val templateMode = chatRoomByCommentNotNull.templateConfiguration!!.templateMode.getInt()
        val templatePosition =
            (chatRoomByCommentNotNull.templateConfiguration!!.templateMode as TemplateMode.Order).position.toString()
        val lastCommentMessage = chatRoomByCommentNotNull.commentList.last().message
        val lastCommentTime = chatRoomByCommentNotNull.commentList.last().time.date
        assertEquals(id, result.id)
        assertEquals(title, result.title)
        assertEquals(templateId, result.templateId)
        assertEquals(templateMode, result.templateMode)
        assertEquals(templatePosition, result.phrasePoint)
        assertEquals(lastCommentMessage, result.commentLast)
        assertEquals(lastCommentTime, result.commentTime)
    }

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * 条件：コメントが未投稿
     * 結果：
     * ・チャットルームEntityのIdがチャットルームオブジェクトのルームID
     * ・チャットルームEntityのタイトルがチャットルームオブジェクトのタイトル
     * ・チャットルームEntityのテンプレートIDがチャットルームオブジェクトのテンプレートID
     * ・チャットルームEntityのテンプレートモードがチャットルームオブジェクトのテンプレートモード
     * ・チャットルームEntityのテンプレートポジションがチャットルームオブジェクトのテンプレートポジション
     * ・チャットルームEntityの最終コメントがnull
     * ・チャットルームEntityの最終コメント時間がnull
     */
    @Test
    fun chatEntityFromChatByCommentNull() {
        val result = Converter.chatEntityFromChat(chatRoomByCommentNull)
        val id = chatRoomByCommentNull.roomId.value.toLong()
        val title = chatRoomByCommentNull.title
        val templateId = chatRoomByCommentNull.templateConfiguration!!.template.templateId.value.toLong()
        val templateMode = chatRoomByCommentNull.templateConfiguration!!.templateMode.getInt()
        val templatePosition =
            (chatRoomByCommentNull.templateConfiguration!!.templateMode as TemplateMode.Order).position.toString()
        val lastCommentMessage = null
        val lastCommentTime = null
        assertEquals(id, result.id)
        assertEquals(title, result.title)
        assertEquals(templateId, result.templateId)
        assertEquals(templateMode, result.templateMode)
        assertEquals(templatePosition, result.phrasePoint)
        assertEquals(lastCommentMessage, result.commentLast)
        assertEquals(lastCommentTime, result.commentTime)
    }

    // endregion

    // region チャットルーム・コメント・テンプレートタイトル・テンプレート文の各Entitykaraチャットルームオブジェクトへ変換

    /**
     * 各Entityからチャットルームオブジェクトへ変換
     *
     * 条件：テンプレートが設定されていない
     * 結果：
     * ・チャットルームオブジェクトのIDがチャットルームEntityのID
     * ・チャットルームオブジェクトのタイトルがチャットルームEntityのタイトル
     * ・チャットルームオブジェクトのコメントリストがチャットコメントEntityリスト
     * ・チャットルームオブジェクトのテンプレート設定がnull
     */
    @Test
    fun chatFromByTemplateNull() {
        val result = Converter.chatFromBy(chatRoomEntityByTemplateNull, commentEntityList, null, null)
        val id = RoomId(chatRoomEntityByTemplateNull.id!!.toInt())
        val title = chatRoomEntityByTemplateNull.title
        val commentList = commentEntityList.map { Converter.commentFromCommentEntity(it) }
        assertEquals(id, result.roomId)
        assertEquals(title, result.title)
        assertEquals(commentList, result.commentList)
        assertEquals(null, result.templateConfiguration)
    }

    /**
     * 各Entityからチャットルームオブジェクトへ変換
     *
     * 条件：テンレート表示形式が順番
     * 結果：
     * ・チャットルームオブジェクトのIDがチャットルームEntityのID
     * ・チャットルームオブジェクトのタイトルがチャットルームEntityのタイトル
     * ・チャットルームオブジェクトのコメントリストがチャットコメントEntityリスト
     * ・チャットルームオブジェクトのテンプレート設定が下記の設定
     * 　・テンプレート：テンプレートタイトルEntityとテンプレート文Entityから変換されたテンプレートオブジェクト
     * 　・テンプレート表示形式：順番（チャットルームEntityのテンプレート表示形式に紐づく表示形式）
     * 　・テンプレートの次の表示位置：チャットルームEntityのテンプレート表示位置
     */
    @Test
    fun chatFromByTemplateOrder() {
        val result = Converter.chatFromBy(chatRoomEntityByOrder, commentEntityList, templateTitleEntity, phraseEntityList)
        val id = RoomId(chatRoomEntityByOrder.id!!.toInt())
        val title = chatRoomEntityByOrder.title
        val commentList = commentEntityList.map { Converter.commentFromCommentEntity(it) }
        val template = Converter.templateFromTemplateEntityAndPhraseEntity(templateTitleEntity, phraseEntityList)
        val templatePosition = chatRoomEntityByOrder.phrasePoint!!.toInt()
        assertEquals(id, result.roomId)
        assertEquals(title, result.title)
        assertEquals(commentList, result.commentList)
        assertEquals(template, result.templateConfiguration!!.template)
        assertEquals(true, result.templateConfiguration!!.templateMode is TemplateMode.Order)
        assertEquals(templatePosition, (result.templateConfiguration!!.templateMode as TemplateMode.Order).position)
    }

    /**
     * 各Entityからチャットルームオブジェクトへ変換
     *
     * 条件：テンレート表示形式がランダム
     * 結果：
     * ・チャットルームオブジェクトのIDがチャットルームEntityのID
     * ・チャットルームオブジェクトのタイトルがチャットルームEntityのタイトル
     * ・チャットルームオブジェクトのコメントリストがチャットコメントEntityリスト
     * ・チャットルームオブジェクトのテンプレート設定が下記の設定
     * 　・テンプレート：テンプレートタイトルEntityとテンプレート文Entityから変換されたテンプレートオブジェクト
     * 　・テンプレート表示形式：ランダム（チャットルームEntityのテンプレート表示形式に紐づく表示形式）
     * 　・テンプレートの次の表示位置：チャットルームEntityのテンプレート表示位置
     */
    @Test
    fun chatFromByTemplateRandam() {
        val result = Converter.chatFromBy(chatRoomEntityByRandam, commentEntityList, templateTitleEntity, phraseEntityList)
        val id = RoomId(chatRoomEntityByRandam.id!!.toInt())
        val title = chatRoomEntityByRandam.title
        val commentList = commentEntityList.map { Converter.commentFromCommentEntity(it) }
        val template = Converter.templateFromTemplateEntityAndPhraseEntity(templateTitleEntity, phraseEntityList)
        val templatePosition = chatRoomEntityByRandam.phrasePoint!!.split(",")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .toMutableList()
        assertEquals(id, result.roomId)
        assertEquals(title, result.title)
        assertEquals(commentList, result.commentList)
        assertEquals(template, result.templateConfiguration!!.template)
        assertEquals(true, result.templateConfiguration!!.templateMode is TemplateMode.Randam)
        assertEquals(templatePosition, (result.templateConfiguration!!.templateMode as TemplateMode.Randam).position)
    }

    /**
     * 各Entityからチャットルームオブジェクトへ変換
     *
     * 条件：テンレート表示形式がランダム（初期表示）
     * 結果：
     * ・チャットルームオブジェクトのIDがチャットルームEntityのID
     * ・チャットルームオブジェクトのタイトルがチャットルームEntityのタイトル
     * ・チャットルームオブジェクトのコメントリストがチャットコメントEntityリスト
     * ・チャットルームオブジェクトのテンプレート設定が下記の設定
     * 　・テンプレート：テンプレートタイトルEntityとテンプレート文Entityから変換されたテンプレートオブジェクト
     * 　・テンプレート表示形式：ランダム（チャットルームEntityのテンプレート表示形式に紐づく表示形式）
     * 　・テンプレートの次の表示位置：初期状態（空のリスト）
     */
    @Test
    fun chatFromByTemplateRandamInit() {
        val result = Converter.chatFromBy(chatRoomEntityByRandamInit, commentEntityList, templateTitleEntity, phraseEntityList)
        val id = RoomId(chatRoomEntityByRandamInit.id!!.toInt())
        val title = chatRoomEntityByRandamInit.title
        val commentList = commentEntityList.map { Converter.commentFromCommentEntity(it) }
        val template = Converter.templateFromTemplateEntityAndPhraseEntity(templateTitleEntity, phraseEntityList)
        val templatePosition = mutableListOf<Int>()
        assertEquals(id, result.roomId)
        assertEquals(title, result.title)
        assertEquals(commentList, result.commentList)
        assertEquals(template, result.templateConfiguration!!.template)
        assertEquals(true, result.templateConfiguration!!.templateMode is TemplateMode.Randam)
        assertEquals(templatePosition, (result.templateConfiguration!!.templateMode as TemplateMode.Randam).position)
    }
    // endregion
}
