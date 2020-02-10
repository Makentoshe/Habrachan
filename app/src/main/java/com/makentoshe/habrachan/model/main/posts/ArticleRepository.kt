package com.makentoshe.habrachan.model.main.posts

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Single

class ArticleRepository(
    private val requestFactory: GetPostsRequestFactory,
    private val articleManager: HabrPostsManager
) {
    fun requestAll(page: Int): Single<List<Article>> {
        val request = requestFactory.all(page)
        return articleManager.getPosts(request).map { it.data }
    }
}
