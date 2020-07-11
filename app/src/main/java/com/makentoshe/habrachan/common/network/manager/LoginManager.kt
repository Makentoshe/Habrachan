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
import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse>

    class Builder(private val client: OkHttpClient) {

        fun build() = NativeLoginManager(client)
    }
}

class NativeLoginManager(private val client: OkHttpClient) : LoginManager {

    private val baseUrl = "https://habr.com/"
    private val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).build()
    private val api = retrofit.create(LoginApi::class.java)

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
        val allTimeReceivedCookies = HashSet<Cookie>()
        val redirectResponseSubject = PublishSubject.create<okhttp3.Response>()

        Single.just(oAuthRequest).observeOn(Schedulers.io()).map { request ->
            return@map api.oauth(request.clientId, request.socialType, request.responseType, request.redirectUri)
                .execute().raw()
        }.toObservable().safeSubscribe(redirectResponseSubject)

        val oauthResponseSubject = PublishSubject.create<okhttp3.Response>()

        //todo if response is not success - check and return error
        redirectResponseSubject.filter { response ->
            val filterCondition = response.isRedirect
            Cookie.parseAll(response.request.url, response.headers).let(allTimeReceivedCookies::addAll)
            if (filterCondition) {
                // todo return error response
                val location = response.headers["Location"] ?: throw IllegalStateException("redirect error")

                val newResponse = client.newCall(Request.Builder().head().url(location).build()).execute()
                redirectResponseSubject.onNext(newResponse)
            }
            return@filter !filterCondition
        }.safeSubscribe(oauthResponseSubject)

        return oauthResponseSubject.singleOrError().map { response ->
            return@map OAuthResponse.Interim(oAuthRequest, response.request.url.toString(), allTimeReceivedCookies)
        }
    }
}