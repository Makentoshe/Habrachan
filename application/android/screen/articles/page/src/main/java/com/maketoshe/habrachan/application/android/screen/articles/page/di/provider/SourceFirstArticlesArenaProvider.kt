package com.maketoshe.habrachan.application.android.screen.articles.page.di.provider

import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import javax.inject.Inject
import javax.inject.Provider

class SourceFirstArticlesArenaProvider @Inject constructor(
    private val articlesArenaFactory: ArticlesArena.Factory,
) : Provider<ArticlesArena> {
    override fun get() = articlesArenaFactory.sourceFirstArena()
}