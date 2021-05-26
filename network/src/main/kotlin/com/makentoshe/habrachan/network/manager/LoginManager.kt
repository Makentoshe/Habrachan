package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.LoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse

interface LoginManager<Request: LoginRequest> {

    fun request(userSession: UserSession, email: String, password: String): Request

    suspend fun login(request: Request): Result<LoginResponse>
}

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
