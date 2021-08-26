package com.makentoshe.habrachan.application.android.screen.article.di

import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.GetArticleArenaCache
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.core.arena.articles.GetArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

/** This module uses for in the ArticleScope and specifies all objects uses in SpecifiedArticleModule */
class CommonArticleModule(injectorScope: FragmentInjector.FragmentInjectorScope<*>) : Module() {

    private val router by inject<StackRouter>()
    private val cacheDatabase by inject<AndroidCacheDatabase>()
    private val getContentManager by inject<GetContentManager>()
    private val getArticleManager by inject<GetArticleManager<GetArticleRequest2>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<ArticleNavigation>().toInstance(ArticleNavigation(router))
        bind<GetArticleArena>().toInstance(GetArticleArena(getArticleManager, GetArticleArenaCache(cacheDatabase)))
        bind<ArticleHtmlController>().toInstance(ArticleHtmlController(injectorScope.context.resources))

        val avatarCache = AvatarArenaCache(cacheDatabase.avatarDao(), injectorScope.context.cacheDir)
        bind<ContentArena>().toInstance(ContentArena(getContentManager, avatarCache))
    }
}