package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.request.ImageRequest
import com.makentoshe.habrachan.network.response.ImageResponse
import okhttp3.OkHttpClient
import okhttp3.Request

interface ImageManager {

    suspend fun getImage(request: ImageRequest): Result<ImageResponse>

    class Builder(private val client: OkHttpClient) {
        fun build(): ImageManager = ImageManagerImpl(client)
    }
}

internal class ImageManagerImpl(private val client: OkHttpClient) : ImageManager {
    override suspend fun getImage(request: ImageRequest): Result<ImageResponse> = try {
        val response = client.newCall(Request.Builder().url(request.imageUrl).build()).execute()
        if (response.isSuccessful) {
            Result.success(ImageResponse(request, response.body!!.bytes()))
        } else {
            Result.failure(ImageManagerException(response.body!!.string(), request))
        }
    } catch (exception: Exception) {
        Result.failure(ImageManagerException(exception.toString(), request))
    }
}

// TODO make special exception for whole manager exceptions
data class ImageManagerException(override val message: String, val request: ImageRequest): Throwable()
