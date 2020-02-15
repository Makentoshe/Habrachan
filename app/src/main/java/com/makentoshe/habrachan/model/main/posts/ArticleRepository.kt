package com.makentoshe.habrachan.model.main.posts

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import io.reactivex.Single

class ArticleRepository(
    private val requestFactory: GetArticlesRequest.Builder,
    private val articleManager: HabrArticleManager
) {
    fun requestAll(page: Int): Single<List<Article>> {
        val request = requestFactory.all(page)
        return articleManager.getPosts(request).map { it.data }
    }
}
