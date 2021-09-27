package com.maketoshe.habrachan.application.android.screen.articles.page.di

import com.makentoshe.habrachan.application.android.common.articles.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import com.maketoshe.habrachan.application.android.screen.articles.page.di.provider.SourceFirstArticlesArenaProvider
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ArticlesPageModule : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class).inject(this)

        bind<ArenaCache<in GetArticlesRequest2, GetArticlesResponse2>>().toClass<ArticlesArenaCache>().singleton()
        bind<ArticlesArena.Factory>().toClass<ArticlesArena.Factory>().singleton()
        bind<ArticlesArena>().toProvider(SourceFirstArticlesArenaProvider::class).singleton()
    }
}