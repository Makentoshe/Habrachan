package com.makentoshe.habrachan.view.comments.controller

import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController

interface CommentController {

    fun messageFactory(): NativeCommentTextController.Factory

    fun scoreFactory(): NativeCommentScoreController.Factory

    fun levelFactory(): NativeCommentLevelController.Factory

    fun behaviorFactory(): NativeCommentBehaviorController.Factory

    fun avatarFactory(): NativeCommentAvatarController.Factory

    class Factory(
        private val commentPopupFactory: CommentPopupFactory,
        private val avatarControllerFactory: NativeCommentAvatarController.Factory
    ) {
        fun buildNative() = NativeCommentController(commentPopupFactory, avatarControllerFactory)
    }
}