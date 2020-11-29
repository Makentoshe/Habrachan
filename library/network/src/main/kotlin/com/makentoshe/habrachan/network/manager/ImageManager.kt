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
    override suspend fun getImage(request: ImageRequest): Result<ImageResponse> {
        val response = client.newCall(Request.Builder().url(request.imageUrl).build()).execute()
        return if (response.isSuccessful) {
            Result.success(ImageResponse(request, response.body!!.bytes(), false))
        } else {
            // TODO make new exception
            Result.failure(Exception(response.body!!.string()))
        }
    }
}
