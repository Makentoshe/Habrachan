package com.makentoshe.habrachan.common.network.api

import com.makentoshe.habrachan.common.entity.login.LoginResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {

    @POST("https://habr.com/auth/o/access-token")
    @FormUrlEncoded
    fun login(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String?,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("client_secret") clientSecret: String = "41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3",
        @Field("client_id") clientId: String = clientKey,
        @Field("grant_type") grantType: String = "password"
    ): Single<LoginResponse>
}