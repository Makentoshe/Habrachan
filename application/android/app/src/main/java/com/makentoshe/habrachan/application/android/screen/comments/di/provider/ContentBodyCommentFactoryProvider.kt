package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import javax.inject.Inject
import javax.inject.Provider

internal class ContentBodyCommentFactoryProvider: Provider<ContentBodyComment.Factory> {

    @Inject
    internal lateinit var context : Context

    override fun get() = ContentBodyComment.Factory(context)
}