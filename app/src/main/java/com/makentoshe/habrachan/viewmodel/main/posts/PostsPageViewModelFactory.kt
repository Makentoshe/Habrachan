package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory

class PostsPageViewModelFactory(
    private val manager: HabrPostsManager,
    private val factory: GetPostsRequestFactory,
    private val cache: Cache<GetPostsRequest, PostsResponse>
) {

    fun build(fragment: Fragment, page: Int): PostsPageViewModel {
        val factory = PostsPageViewModel.Factory(page, manager, cache, factory)
        return ViewModelProviders.of(fragment, factory)[PostsPageViewModel::class.java]
    }

}