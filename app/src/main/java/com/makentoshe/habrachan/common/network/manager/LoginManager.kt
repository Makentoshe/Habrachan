package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverter
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    fun oauth(request: OAuthRequest): Single<OAuthResponse>

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

                override fun oauth(request: OAuthRequest): Single<OAuthResponse> {
                    var url =
                        "https://habr.com/auth/o/social-login/?client_id=85cab69095196f3.89453480&response_type=token&redirect_uri=http://cleverpumpkin.ru&social_type=github"
                    return Observable.fromCallable {
                        val response = client.newCall(Request.Builder().url(url).build()).execute()
                        url = response.headers["Location"]
                            ?: throw IllegalStateException("No 'Location' header found")
                        println("got url: $url")
                        url
                    }.repeatUntil {
                        url.startsWith("https://github.com")
                    }.last("")
                        .map {
                            println(it)
                            OAuthResponse.Success(url)
                        }
                }
            }
        }
    }
}

