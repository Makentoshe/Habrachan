package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.network.api.LoginApi
import com.makentoshe.habrachan.common.network.converter.LoginConverter
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.OAuthRequest
import com.makentoshe.habrachan.common.network.response.LoginResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import retrofit2.Retrofit
import java.util.*

interface LoginManager {

    fun login(request: LoginRequest): Single<LoginResponse>

    fun oauth(oAuthRequest: OAuthRequest)/*: Single<OAuthResponse>*/

    class Builder(private val client: OkHttpClient) {

        fun build(): NativeLoginManager {
            val cookieJar = object : CookieJar {

                private val cookieContainer = LinkedList<Cookie>()

                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    println("Apply cookies : $cookieContainer for url $url")
                    return cookieContainer
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
//                    println("Append cookies: $cookies from $url")
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
            val client = client.newBuilder().cookieJar(cookieJar).build()
            return NativeLoginManager(client)
        }
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

    override fun oauth(oAuthRequest: OAuthRequest)/*: Single<OAuthResponse> */ {
        requestOauthAction(oAuthRequest).doOnSuccess { response ->
            oAuthRequest.requestSubject.onNext(response.request.url.toString())
        }.map {
            val url = oAuthRequest.responseSubject.blockingLast()
            println(url)
            val request = Request.Builder().url(url).build()
            return@map client.newCall(request).execute()
        }.map { response ->
            println(response)
//            val b = response.body?.string()
//            println("b: $b")
        }.subscribe()
/*
        // oauth callback
        val oauthInterimSubject = PublishSubject.create<Response>()
        // after oauth
        val redirectRequestSubject2 = PublishSubject.create<Request>()

        redirectResponseSubject1.map { response ->
        }.subscribe()*/
/*.filter { response ->
            Cookie.parseAll(response.request.url, response.headers).let(allTimeReceivedCookies::addAll)
            val filterCondition = response.isRedirect
            if (filterCondition) {
                // todo return error response
                val location = response.headers["Location"] ?: throw IllegalStateException("redirect error")
                val newResponse = client.newCall(Request.Builder().head().url(location).build()).execute()
                redirectResponseSubject1.onNext(newResponse)
            }
            return@filter !filterCondition
        }.safeSubscribe(oauthInterimSubject)
*//*

        oauthInterimSubject.doOnNext { response ->
            oAuthRequest.requestSubject.onNext(response.request.url.toString())
        }.map {
            val url = oAuthRequest.responseSubject.blockingLast()
            return@map Request.Builder().head().headers(fromCookies(allTimeReceivedCookies)).url(url).build()
        }.safeSubscribe(redirectRequestSubject2)

        redirectRequestSubject2.map { request ->
            return@map client.newCall(request).execute()
        }.filter { response ->
            val location = response.headers["Location"]
            println(response)
            val request = Request.Builder().head().headers(fromCookies(allTimeReceivedCookies)).url(location!!).build()
            redirectRequestSubject2.onNext(request)
            true
        }.subscribe()
//        redirectResponseSubject2.filter { response ->
//            if (response.isRedirect) {
//                val location = response.headers["Location"] ?: throw IllegalStateException("redirect error")
//                val request = Request.Builder().head().headers(fromCookies(allTimeReceivedCookies)).url(location).build()
//                val newResponse = client.newCall(request).execute()
//                redirectResponseSubject2.onNext(newResponse)
//            }
//            return@filter response.isRedirect
//        }.subscribe()
*/
    }

    private fun requestOauthAction(oAuthRequest: OAuthRequest): Single<Response> {
        return Single.just(oAuthRequest).observeOn(Schedulers.io()).map { request ->
            api.oauth(request.clientId, request.socialType, request.responseType, request.redirectUri).execute().raw()
        }
    }

    private fun fromCookies(cookies: HashSet<Cookie>): Headers {
        val cookieHeader = cookies.joinToString("; ") { "${it.name}=${it.value}" }
        return Headers.Builder().add("Cookie", cookieHeader).build()
    }
}