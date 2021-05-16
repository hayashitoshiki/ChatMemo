package com.myapp.chatmemo.presentation.chat

import com.myapp.chatmemo.domain.model.value.Comment

/**
 * コメントリストの表示制御
 * @param beforeComment １つ前のリストに表示されるコメント
 * @param comment 現在のリストに表示されるコメント
 */
class ChatRecyclerItemState(
    beforeComment: Comment?,
    comment: Comment
) {

    var isHeader: Boolean
        private set

    init {
        val commentDate = comment.time.toSectionDate()
        val beforCcommentDate = beforeComment?.time?.toSectionDate()
        isHeader = beforCcommentDate == null || beforCcommentDate != commentDate
    }
}
