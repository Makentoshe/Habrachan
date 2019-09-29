package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.login.LoginResult
import com.makentoshe.habrachan.common.model.network.posts.GetPostsBySearchResult
import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import com.makentoshe.habrachan.common.model.network.users.GetUserByLoginResult
import com.makentoshe.habrachan.common.model.network.users.GetUsersBySearchResult
import com.makentoshe.habrachan.common.model.network.votepost.VotePostResult
import retrofit2.Call
import retrofit2.http.*

interface HabrApi {

    @GET("https://m.habr.com/kek/v1/articles/")
    fun getPostsBySearch(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null
    ): Call<GetPostsBySearchResult>

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

    @PUT("https://habr.com/api/v1/post/{postId}/vote")
    fun votePostThroughApi(
        @Header("client") clientKey: String,
        @Header("token") accessToken: String,
        @Path("postId") postId: Int,
        @Query("vote") score: Int
    ): Call<Result.VotePostResponse>

    @GET("https://m.habr.com/kek/v1/search/users/")
    fun getUsersBySearch(
        @Query("page") page: Int,
        @Query("query") query: String
    ): Call<GetUsersBySearchResult>

    @GET("https://habr.com/api/v1/users/{login}")
    fun getUserByLogin(
        @Header("client") clientKey: String,
        @Header("apikey") apiKey: String? = null,
        @Header("token") accessToken: String? = null,
        @Path("login") login: String
    ): Call<GetUserByLoginResult>

    @GET("https://habr.com/api/v1/flows")
    fun getFlows(
        @Header("client") clientKey: String,
        @Header("apikey") apiKey: String? = null,
        @Header("token") accessToken: String? = null
    ): Call<Result.GetFlowsResponse>

    @GET("https://habr.com/api/v1/flows/{hub}/hubs")
    fun getHubs(
        @Header("client") clientKey: String,
        @Header("apikey") apiKey: String? = null,
        @Header("token") accessToken: String? = null,
        @Path("hub") hubAlias: String,
        @Query("page") page: Int
    ): Call<Result.GetHubsResponse>

}
