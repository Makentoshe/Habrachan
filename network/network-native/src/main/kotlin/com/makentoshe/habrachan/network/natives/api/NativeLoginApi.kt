package com.makentoshe.habrachan.network.natives.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NativeLoginApi {

    @POST("auth/o/access-token")
    @FormUrlEncoded
    fun login(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("client_secret") clientSecret: String = "41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3",
        @Field("client_id") clientId: String = clientKey,
        @Field("grant_type") grantType: String = "password"
    ): Call<ResponseBody>

    @GET("auth/o/social-login")
    fun oauth(
        @Query("client_id") clientId: String,
        @Query("social_type") socialType: String,
        @Query("response_type") responseType: String,
        @Query("redirect_uri") redirectUri: String
    ): Call<ResponseBody>
}