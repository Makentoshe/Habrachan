package com.makentoshe.habrachan.model.main.posts

import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory

class PostsInteractor(private val factory: GetPostsRequestFactory, private val manager: HabrPostsManager) {

    fun interesting(page: Int) = manager.getPosts(factory.interesting(page))

    fun all(page: Int) = manager.getPosts(factory.all(page))

    fun feed(page: Int) = manager.getPosts(factory.feed(page))

    fun query(page: Int, query: String, sort: String? = null) = manager.getPosts(factory.query(page, query, sort))
}