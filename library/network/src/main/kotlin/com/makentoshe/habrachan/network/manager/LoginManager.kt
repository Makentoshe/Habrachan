package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.common.network.response.LoginResponse
import com.makentoshe.habrachan.network.request.LoginRequest
import okhttp3.OkHttpClient

interface LoginManager {

    suspend fun login(request: LoginRequest): Result<LoginResponse>

//    fun oauth(oAuthRequest: OAuthRequest): Result<OAuthResponse>

    class Builder(private val client: OkHttpClient) {

//        private val cookieContainer = LinkedList<Cookie>()
//
//        private val cookieJar = object : CookieJar {
//
//            override fun loadForRequest(url: HttpUrl): List<Cookie> {
//                return cookieContainer
//            }
//
//            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//                cookies.forEach { cookie ->
//                    val storedCookie = cookieContainer.find { it.name == cookie.name }
//                    if (storedCookie == null) {
//                        cookieContainer.add(cookie)
//                    } else {
//                        cookieContainer.remove(storedCookie)
//                        cookieContainer.add(cookie)
//                    }
//                }
//            }
//        }
    }
}

//
//    override fun oauth(oAuthRequest: OAuthRequest): Single<OAuthResponse> {
//        return requestOauthAction(oAuthRequest).map { response ->
//            OAuthResponse.Interim(oAuthRequest, response.request.url.toString(), cookieContainer.toSet())
//        }
//    }
//
//    private fun requestOauthAction(oAuthRequest: OAuthRequest): Single<Response> {
//        return Single.just(oAuthRequest).observeOn(Schedulers.io()).map { request ->
//            api.oauth(request.clientId, request.socialType, request.responseType, request.redirectUri).execute().raw()
//        }
//    }
//}