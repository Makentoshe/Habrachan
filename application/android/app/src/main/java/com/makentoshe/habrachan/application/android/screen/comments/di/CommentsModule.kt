package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.network.UserSession
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.delegate.inject

abstract class CommentsModule(fragment: Fragment): Module() {

    protected val router by inject<StackRouter>()
    protected val client by inject<OkHttpClient>()
    protected val session by inject<UserSession>()
    protected val database by inject<AndroidCacheDatabase>()

    protected val commentContentFactory = CommentViewController.CommentContent.Factory(fragment.requireContext())
    protected val blockContentFactory = BlockViewController.BlockContent.Factory(fragment.requireContext())


    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
    }
}