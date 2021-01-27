package com.makentoshe.habrachan.network.request

/** Request image by [imageUrl] */
data class ImageRequest(val imageUrl: String) {
    companion object {
        const val stubMiddle = "https://habr.com/images/avatars/stub-user-middle.gif"
        const val stubSmall = "https://habr.com/images/avatars/stub-user-small.gif"
        val stubs = arrayOf(stubMiddle, stubSmall)
    }
}