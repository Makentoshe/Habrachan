package com.makentoshe.habrachan.application.android.screen.comments.di.module

import com.makentoshe.habrachan.application.android.common.arena.ArticleCommentsArenaCache
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class GetArticleCommentsNetworkModule: BaseNetworkModule() {

    private val database by inject<AndroidCacheDatabase>()
    private val getArticleCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val commentsArenaStorage = ArticleCommentsArenaCache(database)
        val commentsArena = ArticleCommentsArena.Factory(getArticleCommentsManager, commentsArenaStorage).cacheFirstArena()
        bind<ArticleCommentsArena>().toInstance(commentsArena)
    }
}