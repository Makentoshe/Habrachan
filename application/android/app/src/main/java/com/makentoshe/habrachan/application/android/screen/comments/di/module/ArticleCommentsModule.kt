package com.makentoshe.habrachan.application.android.screen.comments.di.module

import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.network.UserSession
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleCommentsModule(private val fragment: ArticleCommentsFragment) : Module() {

    // From ApplicationScope
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    // From CommentsScope
    private val articleCommentsArenaFactory by inject<ArticleCommentsArena.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)
        bind<ArticleCommentsArena>().toInstance(articleCommentsArenaFactory.sourceFirstArena())
    }
}
