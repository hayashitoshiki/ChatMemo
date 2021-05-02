package com.example.chatmemo.data.repository

import com.example.chatmemo.data.local.database.entity.ChatRoomEntity
import com.example.chatmemo.data.local.database.entity.CommentEntity
import com.example.chatmemo.data.local.database.entity.PhraseEntity
import com.example.chatmemo.data.local.database.entity.TemplateEntity
import com.example.chatmemo.domain.model.entity.ChatRoom
import com.example.chatmemo.domain.model.entity.Template
import com.example.chatmemo.domain.model.value.*
import com.example.chatmemo.ui.utils.expansion.toLocalDateTime

object Converter {

    // region テンプレート関連

    // テンプレート文Entityからテンプレート文オブジェクトへ変換
    fun templateMessageFromPharaseEntity(phrase: PhraseEntity): TemplateMessage {
        val message = phrase.text
        return TemplateMessage(message)
    }

    // テンプレートオブジェクトからテンプレートEntityへ変換
    fun templateEntityFromTemplate(template: Template): TemplateEntity {
        val templateId = template.templateId.value.toLong()
        val templateTitle = template.title
        return TemplateEntity(templateId, templateTitle)
    }

    // テンプレート文Entityからテンプレートオブジェクトへ変換
    fun praseEntityFromTemplateAndMessage(template: Template, message: TemplateMessage): PhraseEntity {
        val templateId = template.templateId.value.toLong()
        val phraseTitle = message.massage
        return PhraseEntity(null, phraseTitle, templateId)
    }

    // テンプレートEntityとテンプレート文Entityからテンプレートオブジェクトへ変換
    private fun templateFromTemplateEntityAndPhraseEntity(
        templateEntity: TemplateEntity,
        phraseEntityList: List<PhraseEntity>
    ): Template {
        val templateId = TemplateId(templateEntity.id!!.toInt())
        val tempalteTitle = templateEntity.title
        val templateMessageList = phraseEntityList.map { templateMessageFromPharaseEntity(it) }
        return Template(templateId, tempalteTitle, templateMessageList)
    }

    // endregion

    // region チャットルーム関連

    // コメントオブジェクトからコメントEntityへ変換
    fun commentEntityFromComment(comment: Comment, roomId: RoomId): CommentEntity {
        val message = comment.message
        val user = comment.user.chageInt()
        val date = comment.time.toDataBaseDate()
        val roomIdLong = roomId.value.toLong()
        return CommentEntity(null, message, user, date, roomIdLong)
    }

    // コメントEntityからコメントオブジェクトへ変換
    private fun commentFromCommentEntity(commentEntity: CommentEntity): Comment {
        val message = commentEntity.text
        val user = User.getUser(commentEntity.user)
        val commentDate = CommentDateTime(commentEntity.createdAt.toLocalDateTime())
        return Comment(message, user, commentDate)
    }

    // ChatRoomモデルからChatRoomEntityへ変換
    fun chatEntityFromChat(chatRoom: ChatRoom): ChatRoomEntity {
        val id = chatRoom.roomId.value.toLong()
        val title = chatRoom.title
        val templateId: Long?
        val point: String?
        val mode: Int?
        val templateConfiguration = chatRoom.templateConfiguration
        if (templateConfiguration != null) {
            templateId = templateConfiguration.template.templateId.value.toLong()
            point = when (val templateMode = templateConfiguration.templateMode) {
                is TemplateMode.Order -> {
                    templateMode.position.toString()
                }
                is TemplateMode.Randam -> {
                    templateMode.position.joinToString()
                }
            }
            mode = templateConfiguration.templateMode.getInt()
        } else {
            templateId = null
            point = null
            mode = null
        }
        val commentLast: String?
        val commentLastTime: String?
        if (chatRoom.commentList.size != 0) {
            commentLast = chatRoom.commentList.last().message
            commentLastTime = chatRoom.commentList.last().time.toDataBaseDate()
        } else {
            commentLast = null
            commentLastTime = null
        }
        return ChatRoomEntity(id, title, templateId, mode, point, commentLast, commentLastTime)
    }

    // チャットルーム・コメント・テンプレートタイトル・テンプレート文の各Entitykaraチャットルームオブジェクトへ変換
    fun chatFromBy(
        chatRoomEntity: ChatRoomEntity,
        commentEntityList: List<CommentEntity>,
        templateEntity: TemplateEntity?,
        phraseList: List<PhraseEntity>?
    ): ChatRoom {
        val roomId = RoomId(chatRoomEntity.id!!.toInt())
        val roomTitle = chatRoomEntity.title
        val commentList: MutableList<Comment> = commentEntityList.map { commentEntity ->
            commentFromCommentEntity(commentEntity)
        }.toMutableList()

        val templateConfiguration = if (chatRoomEntity.templateId != null && templateEntity != null && phraseList != null) {
            val template = templateFromTemplateEntityAndPhraseEntity(templateEntity, phraseList)
            val templateMode = TemplateMode.toStatus(chatRoomEntity.templateMode!!)
            TemplateConfiguration(template, templateMode)
        } else {
            null
        }
        return ChatRoom(roomId, roomTitle, templateConfiguration, commentList)
    }

    // endregion
}
