package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import javax.inject.Inject
import javax.inject.Provider

internal class ContentBodyBlockFactoryProvider: Provider<ContentBodyBlock.Factory> {

    @Inject
    internal lateinit var context : Context

    override fun get(): ContentBodyBlock.Factory {
        return ContentBodyBlock.Factory(context)
    }
}