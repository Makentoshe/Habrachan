package com.makentoshe.habrachan.di.post

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostFragmentScope

class PostFragmentModule : Module() {

    private val cache by inject<Cache<GetPostsRequest, PostsResponse>>()

    init {
        val postFragmentUi = PostFragmentUi()
        bind<PostFragmentUi>().toInstance(postFragmentUi)
    }
}