package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.login.LoginResponse
import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverterFactory
import com.makentoshe.habrachan.common.network.request.LoginRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(LoginConverterFactory())
            .build()

        fun build(): LoginManager {
            val api = getRetrofit().create(LoginApi::class.java)
            return object : LoginManager {
                override fun login(request: LoginRequest): Single<LoginResponse> {
                    return api.login(request.client, request.api, request.email, request.password)
                }
            }
        }
    }
}

