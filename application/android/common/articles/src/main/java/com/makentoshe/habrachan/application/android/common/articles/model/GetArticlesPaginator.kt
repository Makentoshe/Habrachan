package com.makentoshe.habrachan.application.android.common.articles.model

import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.findFilter
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import com.makentoshe.habrachan.network.articles.get.entity.articles
import com.makentoshe.habrachan.network.articles.get.entity.totalPages

class GetArticlesPaginator {

    fun next(currentResponse: GetArticlesResponse): Option2<GetArticlesSpec> {
        if (currentResponse.articles.articles.value.isEmpty()) return Option2.None
        if (currentResponse.articles.totalPages.isEmpty) return Option2.None

        val currentPageArticlesFilter = getCurrentPageArticlesFilter(currentResponse)
        val nextPageArticlesFilter = buildNextPageArticlesFilter(currentPageArticlesFilter)
        return Option2.from(currentResponse.request.filters.map {
            if (it.key == "page") nextPageArticlesFilter else it
        }.toSet()).map(::GetArticlesSpec)
    }

    private fun getCurrentPageArticlesFilter(response: GetArticlesResponse): ArticlesFilter {
        return response.request.filters.findFilter("page").fold({
            ArticlesFilter.QueryArticlesFilter("page", "2")
        }, { it })
    }

    private fun buildNextPageArticlesFilter(currentPageArticlesFilter: ArticlesFilter): ArticlesFilter {
        return currentPageArticlesFilter.new { oldValue -> oldValue.toInt().plus(1).toString() }
    }
}