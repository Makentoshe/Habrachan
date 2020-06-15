package com.makentoshe.habrachan.view.comments.controller

import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController

class NativeCommentController(
    private val avatarControllerFactory: NativeCommentAvatarController.Factory,
    private val commentPopupFactory: CommentPopupFactory?
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

    override fun behaviorFactory(): NativeCommentBehaviorController.Factory {
        return NativeCommentBehaviorController.Factory(commentPopupFactory)
    }

    override fun avatarFactory(): NativeCommentAvatarController.Factory {
        return avatarControllerFactory
    }
}