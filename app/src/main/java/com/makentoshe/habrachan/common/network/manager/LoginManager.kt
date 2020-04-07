package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.login.LoginResponse
import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverter
import com.makentoshe.habrachan.common.network.request.LoginRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): LoginManager {
            val api = getRetrofit().create(LoginApi::class.java)
            return object : LoginManager {

                override fun login(request: LoginRequest): Single<LoginResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        val response = api.login(request.client, request.api, request.email, request.password).execute()
                        if (response.isSuccessful) {
                            LoginConverter().convertBody(response.body()!!)
                        } else {
                            LoginConverter().convertError(response.errorBody()!!)
                        }
                    }
                }
            }
        }
    }
}

