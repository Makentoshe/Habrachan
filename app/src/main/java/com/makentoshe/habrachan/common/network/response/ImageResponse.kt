package com.makentoshe.habrachan.common.network.response

import com.makentoshe.habrachan.common.network.request.ImageRequest

sealed class ImageResponse {

    class Success(val request: ImageRequest, val bytes: ByteArray, val isStub: Boolean): ImageResponse()

    class Error(val request: ImageRequest, val message: String): ImageResponse()
}