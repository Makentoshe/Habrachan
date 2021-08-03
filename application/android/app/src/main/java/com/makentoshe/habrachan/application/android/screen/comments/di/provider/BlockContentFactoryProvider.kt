package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import javax.inject.Inject
import javax.inject.Provider

internal class BlockContentFactoryProvider: Provider<BlockViewController.BlockContent.Factory> {

    @Inject
    internal lateinit var context : Context

    override fun get(): BlockViewController.BlockContent.Factory {
        return BlockViewController.BlockContent.Factory(context)
    }
}