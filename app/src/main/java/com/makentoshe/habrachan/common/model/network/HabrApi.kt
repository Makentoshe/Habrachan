package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.login.LoginResult
import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import retrofit2.Call
import retrofit2.http.*

interface HabrApi {

    @GET("https://m.habr.com/kek/v1/articles/")
    fun getPostsBySearch(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null
    ): Call<GetPostsResult>

    @GET("https://m.habr.com/kek/v1/articles")
    fun getPosts(
        @Query("page") page: Int,
        @Query("sort") sort: String? = null,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null,
        @Query("date") date: String? = null,
        @Query("custom") custom: Boolean? = null
    ): Call<GetPostsResult>

    @FormUrlEncoded
    @POST("https://habr.com/auth/o/access-token")
    fun loginThroughApi(
        @Header("client") clientKey: String,
        @Header("apikey") apiKey: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: String = clientKey
    ) : Call<LoginResult>

}
