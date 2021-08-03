package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import android.content.Context
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import javax.inject.Provider

internal class BlockContentFactoryProvider(
    private val context: Context,
): Provider<BlockViewController.BlockContent.Factory> {
    override fun get(): BlockViewController.BlockContent.Factory {
        return BlockViewController.BlockContent.Factory(context)
    }
}