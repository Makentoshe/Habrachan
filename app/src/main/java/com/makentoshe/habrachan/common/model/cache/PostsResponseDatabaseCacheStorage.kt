package com.makentoshe.habrachan.common.model.cache

import com.makentoshe.habrachan.common.model.database.DatabasePosts
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse

class PostsResponseDatabaseCacheStorage(
    private val database: DatabasePosts
) : CacheStorage<GetRawRequest, PostsResponse> {

    override fun get(k: GetRawRequest) = database.get(k)

    override fun set(k: GetRawRequest, v: PostsResponse) = database.set(k, v)

    override fun clear() = database.clear()
}