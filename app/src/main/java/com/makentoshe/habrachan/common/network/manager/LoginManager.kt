package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverter
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.common.network.response.OAuthResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Retrofit
import java.util.*

interface LoginManager {

    fun login(loginRequest: LoginRequest): Single<LoginResponse>

    fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse>

    class Builder(private val client: OkHttpClient) {

        private val cookieContainer = LinkedList<Cookie>()

        private val cookieJar = object : CookieJar {

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return cookieContainer
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookies.forEach { cookie ->
                    val storedCookie = cookieContainer.find { it.name == cookie.name }
                    if (storedCookie == null) {
                        cookieContainer.add(cookie)
                    } else {
                        cookieContainer.remove(storedCookie)
                        cookieContainer.add(cookie)
                    }
                }
            }
        }

        fun build(): LoginManager {
            val client = client.newBuilder().cookieJar(cookieJar).followRedirects(true).build()
            return NativeLoginManager(client, cookieContainer)
        }
    }
}

class NativeLoginManager(client: OkHttpClient, private val cookieContainer: LinkedList<Cookie>) : LoginManager {

    private val baseUrl = "https://habr.com/"
    private val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl).build()
    private val api = retrofit.create(LoginApi::class.java)

    override fun login(loginRequest: LoginRequest): Single<LoginResponse> {
        return Single.just(loginRequest).observeOn(Schedulers.io()).map { request ->
            val response = api.login(request.client, request.api, request.email, request.password).execute()
            if (response.isSuccessful) {
                LoginConverter().convertBody(response.body()!!)
            } else {
                LoginConverter().convertError(response.errorBody()!!)
            }
        }
    }

    override fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse> {
        return requestOauthAction(oAuthRequest).map { response ->
            OAuthResponse.Interim(oAuthRequest, response.request.url.toString(), cookieContainer.toSet())
        }
    }

    private fun requestOauthAction(oAuthRequest: OAuthRequest): Single<Response> {
        return Single.just(oAuthRequest).observeOn(Schedulers.io()).map { request ->
            api.oauth(request.clientId, request.socialType, request.responseType, request.redirectUri).execute().raw()
        }
    }
}