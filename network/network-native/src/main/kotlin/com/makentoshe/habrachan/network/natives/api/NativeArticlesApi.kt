package com.makentoshe.habrachan.network.natives.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NativeArticlesApi {

    @GET("api/v1/{spec}")
    fun getArticles(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String,
        @Header("token") token: String?,
        @Path("spec") spec: String,
        @Query("page") page: Int,
        @Query("include") include: String? = "text_html",
//        @Query("get_article") getArticle: Boolean? = null,
        @Query("exclude") exclude: String? = null,
        @Query("sort") sort: String? = null
    ): Call<ResponseBody>

    @GET("api/v1/users/{username}/posts")
    fun getUserArticles(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("include") include: String? = "text_html",
        @Query("exclude") exclude: String? = null
    ): Call<ResponseBody>

    @GET("api/v1/post/{id}")
    fun getArticle(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String,
        @Header("token") token: String?,
        @Path("id") id: Int,
        @Query("include") include: String? = "text_html",
        @Query("get_article") getArticle: Boolean? = null,
        @Query("exclude") exclude: String? = null
    ): Call<ResponseBody>

    @PUT("api/v1/post/{id}/vote?vote=-1")
    fun voteArticleDown(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") id: Int,
        @Query("reason") reason: Int
    ): Call<ResponseBody>

    @PUT("api/v1/post/{id}/vote?vote=1")
    fun voteArticleUp(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>
}