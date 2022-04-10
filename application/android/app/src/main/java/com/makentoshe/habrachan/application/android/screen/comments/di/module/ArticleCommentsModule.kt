package com.makentoshe.habrachan.application.android.screen.comments.di.module

import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
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

    private val userSession by inject<UserSession>()

    // From CommentsScope
    private val articleCommentsArenaFactory by inject<ArticleCommentsArena.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)

        val articleCommentsArena = articleCommentsArenaFactory.sourceFirstArena()
        bind<ArticleCommentsArena>().toInstance(articleCommentsArena)

        val factory = GetArticleCommentsViewModel.Factory(userSession, articleCommentsArena)
        bind<GetArticleCommentsViewModel.Factory>().toInstance(factory)
    }
}
