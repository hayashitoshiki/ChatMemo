package com.myapp.chatmemo

import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.value.*
import java.time.LocalDateTime

/**
 * テストデータ管理クラス
 */
object TestDataUtil {

    // region RoomID

    val roomId1 = RoomId(1)
    val roomId2 = RoomId(2)
    val roomId3 = RoomId(3)
    val roomId4 = RoomId(4)
    val roomId5 = RoomId(5)

    // endregion

    // region 時間定義

    private val time1 = CommentDateTime(LocalDateTime.now())
    private val time2 = CommentDateTime(LocalDateTime.now())
    private val time3 = CommentDateTime(LocalDateTime.now())
    private val time4 = CommentDateTime(LocalDateTime.now())
    private val time5 = CommentDateTime(LocalDateTime.now())
    private val time6 = CommentDateTime(LocalDateTime.now())
    private val time7 = CommentDateTime(LocalDateTime.now())

    // endregion

    // region comment

    val commentBlack = Comment("black comment", User.BLACK, time1)
    val commentWhite = Comment("white comment", User.BLACK, time2)

    val commentReversBlack = Comment(commentBlack.message, User.WHITE, commentBlack.time)
    val commentReversWhite = Comment(commentWhite.message, User.BLACK, commentWhite.time)

    // endregion

    // region commentList

    val commentListBlackOnly = mutableListOf(commentBlack)
    val commentListWhiteOnly = mutableListOf(commentWhite)
    val commentListBlackAndWhite = mutableListOf(commentBlack, commentWhite)
    val commentListReversBlackAndWhite = mutableListOf(commentReversBlack, commentReversWhite)
    val commentListNull = mutableListOf<Comment>()

    // endregion

    // region ChatRoom

    val chatRoom1 = ChatRoom(roomId1, "chatroom1", null, mutableListOf())
    val chatRoomByNotTemplate = ChatRoom(roomId2, "room by not template", null, mutableListOf())
    val chatRoomByCommentOnlyBlack = ChatRoom(roomId3, "room by comment black only", null, commentListBlackOnly)
    val chatRoomByCommentOnlyWhite = ChatRoom(roomId3, "room by comment black only", null, commentListWhiteOnly)
    val chatRoomByCommentBlackAndWhite = ChatRoom(roomId3, "room by comment black only", null, commentListBlackAndWhite)
    val chatRoomByCommentNull = ChatRoom(roomId3, "room by comment black only", null, commentListNull)

    // endregion

    // region chatroomList

    val chatRoomListAll = listOf(
        chatRoom1, chatRoomByNotTemplate, chatRoomByCommentOnlyBlack, chatRoomByCommentOnlyWhite,
        chatRoomByCommentBlackAndWhite, chatRoomByCommentNull
    )

    // endregion

    // region Template

    // endregion

    // region TemplateID

    val templateId1 = TemplateId(1)
    val tempalteId2 = TemplateId(2)
    val tempalteId3 = TemplateId(3)
    val tempalteId4 = TemplateId(4)
    val tempalteId5 = TemplateId(5)

    // endregion


    // region TemplateMessage

    val templateMessage1 = TemplateMessage("templateMessage1")
    val templateMessage2 = TemplateMessage("templateMessage2")
    val templateMessage3 = TemplateMessage("templateMessage3")

    // endregion


    // region templateMode

    val templateModeByOrderAndFirst = TemplateMode.Order("順番", 0)
    val templateModeByOrderAndLast = TemplateMode.Order("順番", 100)
    val templateModeByRandamAndFirst = TemplateMode.Randam("ランダム", mutableListOf())
    val templateModeByRandamAndLast = TemplateMode.Randam("ランダム", mutableListOf(1, 2, 3))

    // endregion

}
