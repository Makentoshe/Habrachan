package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import javax.inject.Inject
import javax.inject.Provider

internal class CommentContentFactoryProvider: Provider<CommentBodyController.CommentContent.Factory> {

    @Inject
    internal lateinit var context : Context

    override fun get(): CommentBodyController.CommentContent.Factory {
        return CommentBodyController.CommentContent.Factory(context)
    }
}