package com.makentoshe.habrachan.common.database

import com.makentoshe.habrachan.common.network.request.GetPostsRequest

interface RequestStorage {
    fun getOrDefault(page: Int, default: GetPostsRequest): GetPostsRequest
    fun get(page: Int): GetPostsRequest?
    fun set(request: GetPostsRequest)
}