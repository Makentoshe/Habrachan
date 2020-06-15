package com.makentoshe.habrachan.view.comments.controller

import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.NativeCommentAvatarController

/** Controls comments displaying and behaviour */
interface CommentController {

    /** How comment message should be displayed */
    fun messageFactory(): NativeCommentTextController.Factory

    /** How comment scores should be displayed */
    fun scoreFactory(): NativeCommentScoreController.Factory

    /** How comment relations should be displayed */
    fun levelFactory(): NativeCommentLevelController.Factory

    /** What comment should do on any gesture actions */
    fun behaviorFactory(): NativeCommentBehaviorController.Factory

    /** How comment should load and display author avatar */
    fun avatarFactory(): NativeCommentAvatarController.Factory

    class Factory(
        private val avatarControllerFactory: NativeCommentAvatarController.Factory,
        private val commentPopupFactory: CommentPopupFactory?
    ) {
        /** Returns controller which works with native android components */
        fun buildNative() = NativeCommentController(avatarControllerFactory, commentPopupFactory)
    }
}