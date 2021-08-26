package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import com.makentoshe.habrachan.application.android.common.arena.ArticleCommentsArenaCache
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import javax.inject.Inject
import javax.inject.Provider

class SourceFirstArticleCommentsArenaProvider @Inject constructor(
    private val manager: GetArticleCommentsManager<GetArticleCommentsRequest>,
    private val arenaCache: ArticleCommentsArenaCache,
) : Provider<ArticleCommentsArena> {
    override fun get(): ArticleCommentsArena {
        return ArticleCommentsArena.Factory(manager, arenaCache).sourceFirstArena()
    }
}