package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverter
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse>

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

                override fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse> {
                    val redirectResponseSubject = PublishSubject.create<okhttp3.Response>()

                    Single.just(oAuthRequest).observeOn(Schedulers.io()).map { request ->
                        return@map api.oauth(request.clientId, request.socialType, request.responseType, request.redirectUri).execute().raw()
                    }.toObservable().safeSubscribe(redirectResponseSubject)

                    val oauthResponseSubject = PublishSubject.create<okhttp3.Response>()

                    //todo if response is not success - check and return error
                    redirectResponseSubject.filter { response ->
                        val location = response.headers["Location"]
                        val condition = location?.startsWith(oAuthRequest.hostUrl)
                        // todo return error response
                            ?: throw IllegalStateException("wtf sas")
                        if (!condition) {
                            val newResponse = client.newCall(Request.Builder().get().url(location).build()).execute()
                            redirectResponseSubject.onNext(newResponse)
                        }
                        return@filter condition
                    }.safeSubscribe(oauthResponseSubject)

                    return oauthResponseSubject.singleOrError().map { response ->
                        // todo if state or location is null or something else - return OAuthResponse.Error
                        val locationUrl = response.headers["Location"]?.toHttpUrlOrNull()!!
                        val state = locationUrl.queryParameter("state") ?: ""
                        return@map OAuthResponse.Success(locationUrl.toString(), state)
                    }
                }
            }
        }
    }
}

