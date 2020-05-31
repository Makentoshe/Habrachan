package com.makentoshe.habrachan.view.comments.controller

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController
import com.makentoshe.habrachan.model.comments.tree.Tree

class NativeCommentController(
    private val commentPopupFactory: CommentPopupFactory,
    private val avatarControllerFactory: NativeCommentAvatarController.Factory
) : CommentController {

    override fun messageFactory(): NativeCommentTextController.Factory {
        return NativeCommentTextController.Factory()
    }

    override fun scoreFactory(): NativeCommentScoreController.Factory {
        return NativeCommentScoreController.Factory()
    }

    override fun levelFactory(): NativeCommentLevelController.Factory {
        return NativeCommentLevelController.Factory()
    }

    override fun behaviorFactory(commentsTree: Tree<Comment>): NativeCommentBehaviorController.Factory {
        return NativeCommentBehaviorController.Factory(commentPopupFactory, commentsTree)
    }

    override fun avatarFactory(): NativeCommentAvatarController.Factory {
        return avatarControllerFactory
    }
}