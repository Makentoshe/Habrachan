package com.makentoshe.habrachan.network.request

/** Request image by [imageUrl] */
data class ImageRequest(val imageUrl: String) {
    companion object {
        const val stub = "https://habr.com/images/avatars/stub-user-middle.gif"
    }
}