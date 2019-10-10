package com.makentoshe.habrachan.model.posts

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.CacheStorage
import com.makentoshe.habrachan.common.model.network.postsalt.GetPostsRequest
import com.makentoshe.habrachan.common.entity.posts.PostsResponse

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
