package com.makentoshe.habrachan.application.android.arena

import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.network.request.GetUserRequest
import com.makentoshe.habrachan.network.response.GetUserResponse

class GetUserArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetUserRequest, GetUserResponse> {

    override fun fetch(key: GetUserRequest): Result<GetUserResponse> {
//        TODO("not implemented")
        return Result.failure(Exception())
    }

    override fun carry(key: GetUserRequest, value: GetUserResponse) {
//        TODO("not implemented")
    }

}