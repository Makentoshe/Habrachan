package com.makentoshe.habrachan.model.main.posts

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.CacheStorage
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest

class PostsResponseCache(
    private val storage: CacheStorage<GetPostsRequest, PostsResponse>
) : Cache<GetPostsRequest, PostsResponse> {

    override fun get(k: GetPostsRequest): PostsResponse? {
        return storage.get(k)
    }

    override fun set(k: GetPostsRequest, v: PostsResponse) {
        storage.set(k, v)
    }
}