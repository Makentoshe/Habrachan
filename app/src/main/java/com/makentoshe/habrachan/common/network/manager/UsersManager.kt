package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.user.UserResponse
import com.makentoshe.habrachan.common.network.api.UsersApi
import com.makentoshe.habrachan.common.network.converter.UsersConverterFactory
import com.makentoshe.habrachan.common.network.request.MeRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface UsersManager {

    fun getMe(request: MeRequest) : Single<UserResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(UsersConverterFactory())
            .build()

        fun build(): UsersManager {
            val api = getRetrofit().create(UsersApi::class.java)
            return object : UsersManager {
                override fun getMe(request: MeRequest): Single<UserResponse> {
                    return api.getMe(request.clientKey, request.tokenKey)
                }
            }
        }
    }
}