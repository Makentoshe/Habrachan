package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.response.ImageResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

interface ImageManager {

    fun getImage(request: ImageRequest): Single<ImageResponse>

    class Builder(private val client: OkHttpClient) {
        fun build() = object : ImageManager {
            override fun getImage(request: ImageRequest): Single<ImageResponse> {
                return Single.just(request.imageUrl).observeOn(Schedulers.io()).map { url ->
                    client.newCall(Request.Builder().url(url).build()).execute()
                }.map { response ->
                    if (response.isSuccessful) {
                        ImageResponse.Success(response.body!!.bytes(), false)
                    } else {
                        ImageResponse.Error(response.body!!.string())
                    }
                }
            }
        }
    }
}