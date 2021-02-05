package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse

class ContentImageArenaCache: ArenaCache<ImageRequest, ImageResponse> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "ContentImageArenaCache", message())
        }
    }

    private val directory = "content"
    private val limit = 50
    private val clearStep = 10

    override fun fetch(key: ImageRequest): Result<ImageResponse> {
        capture(Log.INFO) { "Fetch" }
        return Result.failure(Exception())
    }

    override fun carry(key: ImageRequest, value: ImageResponse) {
        capture(Log.INFO) { "Store image with path" }
    }
}