package com.makentoshe.habrachan.application.android.screen.article.di.module

import com.makentoshe.habrachan.application.android.common.article.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArena
import com.makentoshe.habrachan.network.article.get.mobile.GetArticleManagerImpl
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class GetArticleNetworkModule: BaseNetworkModule() {

    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<GetArticleArena>().toInstance(GetArticleArena(GetArticleManagerImpl(ktorHttpClient), ArticleArenaCache(database)))
    }
}