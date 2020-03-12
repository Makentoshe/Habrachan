package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.user.AvatarResponse
import com.makentoshe.habrachan.common.network.request.AvatarRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

interface AvatarManager {

    fun getAvatar(request: AvatarRequest): Single<AvatarResponse>

    class Builder(private val client: OkHttpClient) {
        fun build() = object : AvatarManager {
            override fun getAvatar(request: AvatarRequest): Single<AvatarResponse> {
                return Single.just(request.avatarUrl).observeOn(Schedulers.io()).map { url ->
                    client.newCall(Request.Builder().url(url).build()).execute()
                }.map { response ->
                    if (response.isSuccessful) {
                        AvatarResponse.Success(response.body!!.bytes(), false)
                    } else {
                        AvatarResponse.Error(response.body!!.string())
                    }
                }
            }
        }
    }
}