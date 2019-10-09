package com.makentoshe.habrachan.common.model.database

import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.realm.RealmConfiguration

interface DatabasePosts: Database  {

    fun get(key: GetRawRequest): PostsResponse?

    fun set(key: GetRawRequest, value: PostsResponse)

    class Builder(private val title: String) {
        fun build(): DatabasePosts {
            val configuration = RealmConfiguration.Builder().name(title).build()
            return RealmDatabasePosts(configuration)
        }
    }
}