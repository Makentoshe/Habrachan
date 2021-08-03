package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import javax.inject.Provider

internal class CommentContentFactoryProvider(
    private val context: Context,
): Provider<CommentBodyController.CommentContent.Factory> {
    override fun get(): CommentBodyController.CommentContent.Factory {
        return CommentBodyController.CommentContent.Factory(context)
    }
}