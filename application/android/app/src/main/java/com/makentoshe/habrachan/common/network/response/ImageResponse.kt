package com.makentoshe.habrachan.common.network.response

import com.makentoshe.habrachan.common.network.request.ImageRequest

sealed class ImageResponse {

    abstract val request: ImageRequest

    class Success(override val request: ImageRequest, val bytes: ByteArray, val isStub: Boolean) : ImageResponse()

    class Error(override val request: ImageRequest, val message: String) : ImageResponse()
}