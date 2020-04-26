package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.api.UsersApi
import com.makentoshe.habrachan.common.network.converter.UsersConverter
import com.makentoshe.habrachan.common.network.request.MeRequest
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.common.network.response.UserResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface UsersManager {

    fun getMe(request: MeRequest): Single<UserResponse>

    fun getUser(request: UserRequest): Single<UserResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): UsersManager {
            val api = getRetrofit().create(UsersApi::class.java)
            return object : UsersManager {

                override fun getMe(request: MeRequest): Single<UserResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.getMe(request.client, request.token, request.include, request.exclude).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            UsersConverter().convertBody(response.body()!!)
                        } else {
                            UsersConverter().convertError(response.errorBody()!!)
                        }
                    }
                }

                override fun getUser(request: UserRequest): Single<UserResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        api.getUser(
                            request.client, request.token, request.name, request.include, request.exclude
                        ).execute()
                    }.map { response ->
                        if (response.isSuccessful) {
                            UsersConverter().convertBody(response.body()!!)
                        } else {
                            UsersConverter().convertError(response.errorBody()!!)
                        }
                    }
                }
            }
        }
    }
}