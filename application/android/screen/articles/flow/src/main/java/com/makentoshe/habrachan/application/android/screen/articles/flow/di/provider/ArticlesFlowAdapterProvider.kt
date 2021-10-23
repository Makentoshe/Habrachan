package com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider

import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import javax.inject.Inject
import javax.inject.Provider

class ArticlesFlowAdapterProvider @Inject constructor(
    private val fragment: ArticlesFlowFragment
) : Provider<ArticlesFlowAdapter> {
    override fun get(): ArticlesFlowAdapter {
        return ArticlesFlowAdapter(fragment, fragment.arguments.userSearchesCount)
    }
}