package com.makentoshe.habrachan.application.core.arena.image

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.CacheFirstArena
import com.makentoshe.habrachan.network.manager.ImageManager
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse

class AvatarArena(
    private val imageManager: ImageManager,
    avatarsCache: ArenaCache<ImageRequest, ImageResponse>
): CacheFirstArena<ImageRequest, ImageResponse>(avatarsCache) {
    override suspend fun internalSuspendFetch(key: ImageRequest): Result<ImageResponse> {
        return imageManager.getImage(key)
    }
}