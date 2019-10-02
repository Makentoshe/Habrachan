package com.makentoshe.habrachan.common.model.network

import retrofit2.Call
import retrofit2.http.*

interface HabrApi {

    @GET("https://m.habr.com/kek/v1/articles/")
    fun getPostsByQuery(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null
    ): Call<Result.GetPostsByQueryResponse>

    @GET("https://m.habr.com/kek/v1/articles/")
    fun getPostsBySort(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null
    ): Call<Result.GetPostsBySortResponse>

    @GET("https://m.habr.com/kek/v1/articles")
    fun getPosts(
        @Query("page") page: Int,
        @Query("sort") sort: String? = null,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null,
        @Query("date") date: String? = null,
        @Query("custom") custom: Boolean? = null
    ): Call<Result.GetPostsResponse>

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
    ) : Call<Result.LoginResponse>

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
    ): Call<Result.GetUsersBySearchResponse>

    @GET("https://habr.com/api/v1/users/{login}")
    fun getUserByLogin(
        @Header("client") clientKey: String,
        @Header("apikey") apiKey: String? = null,
        @Header("token") accessToken: String? = null,
        @Path("login") login: String
    ): Call<Result.GetUserByLoginResponse>

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
