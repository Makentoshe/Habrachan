package com.makentoshe.habrachan.application.android.screen.article.di.module

import com.makentoshe.habrachan.application.android.common.article.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class GetArticleNetworkModule: BaseNetworkModule() {

    private val database by inject<AndroidCacheDatabase>()
    private val getArticleManager by inject<GetArticleManager<GetArticleRequest2>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<ArticleArena>().toInstance(ArticleArena.Factory(getArticleManager, ArticleArenaCache(database)).sourceFirstArena())
    }
}