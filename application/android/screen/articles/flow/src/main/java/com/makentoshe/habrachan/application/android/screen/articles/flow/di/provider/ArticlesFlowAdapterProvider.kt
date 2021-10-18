package com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider

import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import javax.inject.Provider

class ArticlesFlowAdapterProvider(
    private val fragment: ArticlesFlowFragment,
    private val searchesCount: Int,
) : Provider<ArticlesFlowAdapter> {
    override fun get() = ArticlesFlowAdapter(fragment, searchesCount)
}