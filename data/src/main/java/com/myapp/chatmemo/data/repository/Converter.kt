package com.myapp.chatmemo.data.repository

import com.myapp.chatmemo.data.database.entity.ChatRoomEntity
import com.myapp.chatmemo.data.database.entity.CommentEntity
import com.myapp.chatmemo.data.database.entity.TemplateMessageEntity
import com.myapp.chatmemo.data.database.entity.TemplateTitleEntity
import com.myapp.chatmemo.domain.model.entity.ChatRoom
import com.myapp.chatmemo.domain.model.entity.Template
import com.myapp.chatmemo.domain.model.value.*
import java.time.LocalDateTime

object Converter {

    // region テンプレート関連

    /**
     * テンプレート文Entityからテンプレート文Entityへ変換
     *
     * @param templateMessage テンプレート文Entity
     * @return テンプレート文Entity
     */
    fun templateMessageFromPharaseEntity(templateMessage: TemplateMessageEntity): TemplateMessage {
        val message = templateMessage.text
        return TemplateMessage(message)
    }

    /**
     * テンプレートオブジェクトとテンプレート文オブジェクトからテンプレート文Entityへ変換
     *
     * @param template テンプレートオブジェクト
     * @param message テンプレート文オブジェクト
     * @return テンプレート文Entity
     */
    fun templateMessageEntityFromTemplateAndMessage(
        template: Template,
        message: TemplateMessage
    ): TemplateMessageEntity {
        val templateId = template.templateId.value
        val phraseTitle = message.massage
        return TemplateMessageEntity(null, phraseTitle, templateId)
    }

    /**
     * テンプレートオブジェクトからテンプレートEntityへ変換
     *
     * @param template テンプレートオブジェクト
     * @return テンプレートEntity
     */
    fun templateEntityFromTemplate(template: Template): TemplateTitleEntity {
        val templateId = template.templateId.value
        val templateTitle = template.title
        return TemplateTitleEntity(templateId, templateTitle)
    }

    /**
     * テンプレートEntityとテンプレート文Entityからテンプレートオブジェクトへ変換
     *
     * @param templateTitleEntity テンプレートEntity
     * @param templateMessageEntityList テンプレート文Entity
     * @return テンプレートオブジェクト
     */
    fun templateFromTemplateEntityAndPhraseEntity(
        templateTitleEntity: TemplateTitleEntity,
        templateMessageEntityList: List<TemplateMessageEntity>
    ): Template {
        if (templateTitleEntity.id == null) {
            throw NullPointerException("登録していないEntityをコンバートしようとしています")
        }
        val templateId = TemplateId(templateTitleEntity.id)
        val tempalteTitle = templateTitleEntity.title
        val templateMessageList = templateMessageEntityList.map { templateMessageFromPharaseEntity(it) }
        return Template(templateId, tempalteTitle, templateMessageList)
    }

    // endregion

    // region チャットルーム関連

    /**
     * コメントオブジェクトからコメントEntityへ変換
     *
     * @param comment コメントオブジェクト
     * @param roomId チャットルームID
     * @return コメントEntity
     */
    fun commentEntityFromComment(
        comment: Comment,
        roomId: RoomId
    ): CommentEntity {
        val message = comment.message
        val user = comment.user.chageInt()
        val date = comment.time.date
        val roomIdLong = roomId.value
        return CommentEntity(null, message, user, date, roomIdLong)
    }

    /**
     * コメントEntityからコメントオブジェクトへ変換
     *
     * @param commentEntity コメントEntity
     * @return コメントオブジェクト
     */
    fun commentFromCommentEntity(commentEntity: CommentEntity): Comment {
        val message = commentEntity.text
        val user = User.getUser(commentEntity.user)
        val commentDate = CommentDateTime(commentEntity.commentTime)
        return Comment(message, user, commentDate)
    }

    /**
     * ChatRoomモデルからChatRoomEntityへ変換
     *
     * @param chatRoom チャットルームモデル
     * @return チャットルームEntity
     */
    fun chatEntityFromChat(chatRoom: ChatRoom): ChatRoomEntity {
        val id = chatRoom.roomId.value
        val title = chatRoom.title
        val templateId: Long?
        val point: String?
        val mode: Int?
        val templateConfiguration = chatRoom.templateConfiguration
        if (templateConfiguration != null) {
            templateId = templateConfiguration.template.templateId.value
            point = when (val templateMode = templateConfiguration.templateMode) {
                is TemplateMode.Order -> templateMode.position.toString()
                is TemplateMode.Randam -> templateMode.position.joinToString(",")
            }
            mode = templateConfiguration.templateMode.getInt()
        } else {
            templateId = null
            point = null
            mode = null
        }
        val commentLast: String?
        val commentLastTime: LocalDateTime?
        if (chatRoom.commentList.size != 0) {
            commentLast = chatRoom.commentList.last().message
            commentLastTime = chatRoom.commentList.last().time.date
        } else {
            commentLast = null
            commentLastTime = null
        }
        return ChatRoomEntity(id, title, templateId, mode, point, commentLast, commentLastTime)
    }

    /**
     * チャットルーム・コメント・テンプレートタイトル・テンプレート文の各Entityからチャットルームオブジェクトへ変換
     *
     * @param chatRoomEntity チャットルームEntity
     * @param commentEntityList コメントリスト
     * @param templateTitleEntity テンプレートタイトル
     * @param templateMessageList テンプレート文リスト
     * @return
     */
    fun chatFromBy(
        chatRoomEntity: ChatRoomEntity,
        commentEntityList: List<CommentEntity>,
        templateTitleEntity: TemplateTitleEntity?,
        templateMessageList: List<TemplateMessageEntity>?
    ): ChatRoom {
        if (chatRoomEntity.id == null) throw NullPointerException("登録していないEntityをコンバートしようとしています")
        val roomId = RoomId(chatRoomEntity.id)
        val roomTitle = chatRoomEntity.title
        val commentList: MutableList<Comment> =
            commentEntityList.map { commentEntity -> commentFromCommentEntity(commentEntity) }
                .toMutableList()

        val templateConfiguration =
            if (chatRoomEntity.templateId != null && templateTitleEntity != null && templateMessageList != null) {
                val template = templateFromTemplateEntityAndPhraseEntity(templateTitleEntity, templateMessageList)
                val templateMode = when (TemplateMode.toStatus(chatRoomEntity.templateMode!!)) {
                    is TemplateMode.Order -> {
                        TemplateMode.Order("順番", chatRoomEntity.phrasePoint?.toInt() ?: 0)
                    }
                    is TemplateMode.Randam -> {
                        if (!chatRoomEntity.phrasePoint.isNullOrEmpty()) {
                            val position = chatRoomEntity.phrasePoint!!.split(",")
                                .filter { it.isNotEmpty() }
                                .map { it.toInt() }
                                .toMutableList()
                            TemplateMode.Randam("ランダム", position)
                        } else {
                            TemplateMode.Randam()
                        }
                    }
                }
                TemplateConfiguration(template, templateMode)
            } else {
                null
            }
        return ChatRoom(roomId, roomTitle, templateConfiguration, commentList)
    }

    // endregion
}
