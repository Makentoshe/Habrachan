package com.makentoshe.habrachan.common.network.api

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HabrArticlesApi {

    @GET("https://habr.com/api/v1/{type1}/{type2}")
    fun getPosts(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("type1") type1: String,
        @Path("type2") type2: String,
        @Query("page") page: Int,
        @Query("include") include: String? = null,
        @Query("get_article") getArticle: Boolean? = null,
        @Query("exclude") exclude: String? = null,
        @Query("sort") sort: String? = null
    ): Single<PostsResponse>

    @GET("https://habr.com/api/v1/{spec}")
    fun getArticles(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String,
        @Header("token") token: String?,
        @Path("spec") spec: String,
        @Query("page") page: Int,
        @Query("include") include: String? = "text_html",
//        @Query("get_article") getArticle: Boolean? = null,
//        @Query("exclude") exclude: String? = null,
        @Query("sort") sort: String? = null
    ): Call<ResponseBody>

    @GET("https://habr.com/api/v1/post/{id}")
    fun getPost(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("id") id: Int,
        @Query("include") include: String?,
        @Query("get_article") getArticle: Boolean?,
        @Query("exclude") exclude: String?
    ): Single<PostResponse>

    @GET("https://habr.com/api/v1/post/{id}")
    fun getArticle(
        @Header("client") clientKey: String,
        @Header("apiKey") apiKey: String,
        @Header("token") token: String?,
        @Path("id") id: Int,
        @Query("include") include: String? = "text_html"
//        @Query("get_article") getArticle: Boolean? = null,
//        @Query("exclude") exclude: String? = null
    ): Call<ResponseBody>

    @PUT("https://habr.com/api/v1/post/{id}/vote?vote=-1")
    fun voteDown(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>

    @PUT("https://habr.com/api/v1/post/{id}/vote?vote=1")
    fun voteUp(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") id: Int
    ): Call<ResponseBody>
}