package com.makentoshe.habrachan.common.model.cache

import com.makentoshe.habrachan.common.model.database.DatabasePostsConfiguration
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest

class PostsRequestCache(val database: DatabasePostsConfiguration): Cache<Int, GetRawRequest> {

    override fun get(k: Int): GetRawRequest? {
        return database.get(k)
    }

    override fun set(k: Int, v: GetRawRequest) {
        database.put(v)
    }
}