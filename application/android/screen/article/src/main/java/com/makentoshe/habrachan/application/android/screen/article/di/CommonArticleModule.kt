package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.article.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.common.avatar.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.common.avatar.di.CacheFirstAvatarArenaProvider
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import java.io.File

/**
 * Module for any ArticleFragment.
 *
 * This module created and injects once the first [ArticleFragment] instance creates and
 * lives til the last [ArticleFragment] instance keeps alive.
 * */
class CommonArticleModule(fragment: Fragment) : Module() {
    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        bind<File>().toInstance(fragment.requireActivity().cacheDir)
        bind<AvatarArenaCache>().toClass<AvatarArenaCache>().singleton()
        bind<ContentArena>().toProvider(CacheFirstAvatarArenaProvider::class).singleton()
        bind<GetAvatarViewModelProvider>().toClass<GetAvatarViewModelProvider>().singleton()

        bind<ArenaCache<in GetArticleRequest2, GetArticleResponse2>>().toClass<ArticleArenaCache>().singleton()
        bind<ArticleArena.Factory>().toClass<ArticleArena.Factory>().singleton()

        bind<VoteArticleArena.Factory>().toClass<VoteArticleArena.Factory>()
    }
}