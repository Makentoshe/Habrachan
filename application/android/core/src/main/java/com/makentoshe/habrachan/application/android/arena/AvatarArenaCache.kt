package com.makentoshe.habrachan.application.android.arena

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse

// todo Make LRU cache
// todo implement
class AvatarArenaCache: ArenaCache<ImageRequest, ImageResponse> {

    override fun fetch(key: ImageRequest): Result<ImageResponse> {
        return Result.failure(ArenaStorageException("AvatarArenaCache"))
    }

    override fun carry(key: ImageRequest, value: ImageResponse) {

    }
}