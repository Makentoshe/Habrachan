package com.makentoshe.habrachan.application.android.screen.articles.di.module

import com.makentoshe.habrachan.application.android.common.articles.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.common.articles.arena.ArticlesArenaCache3
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.SourceFirstArticlesArenaProvider
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.SourceFirstArticlesArenaProvider3
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena3
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class CommonArticlesPageModule : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class).inject(this)

        bind<ArenaCache3<GetArticlesRequest, GetArticlesResponse>>().toClass<ArticlesArenaCache3>()
        bind<ArticlesArena3.Factory>().toClass<ArticlesArena3.Factory>().singleton()
        bind<ArticlesArena3>().toProvider(SourceFirstArticlesArenaProvider3::class).singleton()

        bind<ArenaCache<in GetArticlesRequest2, GetArticlesResponse2>>().toClass<ArticlesArenaCache>().singleton()
        bind<ArticlesArena.Factory>().toClass<ArticlesArena.Factory>().singleton()
        bind<ArticlesArena>().toProvider(SourceFirstArticlesArenaProvider::class).singleton()
    }
}