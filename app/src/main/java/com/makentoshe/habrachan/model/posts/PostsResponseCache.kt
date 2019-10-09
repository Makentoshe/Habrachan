package com.makentoshe.habrachan.model.posts

import com.makentoshe.habrachan.common.model.cache.Cache
import com.makentoshe.habrachan.common.model.cache.CacheStorage
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse

class PostsResponseCache(
    private val storage: CacheStorage<GetRawRequest, PostsResponse>
) : Cache<GetRawRequest, PostsResponse> {

    override fun get(k: GetRawRequest): PostsResponse? {
        return storage.get(k)
    }

    override fun set(k: GetRawRequest, v: PostsResponse) {
        storage.set(k, v)
    }
}
