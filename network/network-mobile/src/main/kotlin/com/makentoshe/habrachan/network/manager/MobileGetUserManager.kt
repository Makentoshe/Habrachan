package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileUsersApi
import com.makentoshe.habrachan.network.deserializer.MobileGetUserDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetUserRequest
import com.makentoshe.habrachan.network.response.GetUserResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MobileGetUserManager(
    private val api: MobileUsersApi, private val deserializer: MobileGetUserDeserializer

) : GetUserManager<MobileGetUserRequest> {
    override fun request(userSession: UserSession, username: String): MobileGetUserRequest {
        return MobileGetUserRequest(userSession, username)
    }

    override suspend fun user(request: MobileGetUserRequest): Result<GetUserResponse> = try {
        api.getUser(request.username, request.userSession.filterLanguage, request.userSession.habrLanguage).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: MobileGetUserDeserializer) {

        private val baseUrl = "https://m.habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = MobileGetUserManager(getRetrofit().create(MobileUsersApi::class.java), deserializer)
    }
}