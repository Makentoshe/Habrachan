package com.makentoshe.habrachan.common.network.response

sealed class ImageResponse {

    class Success(val bytes: ByteArray, val isStub: Boolean): ImageResponse()

    class Error(val message: String): ImageResponse()
}