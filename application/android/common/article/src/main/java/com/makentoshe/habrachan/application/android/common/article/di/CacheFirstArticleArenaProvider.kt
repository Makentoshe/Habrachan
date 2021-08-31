package com.makentoshe.habrachan.application.android.common.article.di

import com.makentoshe.habrachan.application.android.common.article.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import javax.inject.Inject
import javax.inject.Provider

class CacheFirstArticleArenaProvider @Inject constructor(
    private val manager: GetArticleManager<GetArticleRequest2>,
    private val arenaCache: ArticleArenaCache
) : Provider<ArticleArena> {
    override fun get(): ArticleArena {
        return ArticleArena.Factory(manager, arenaCache).cacheFirstArena()
    }
}
