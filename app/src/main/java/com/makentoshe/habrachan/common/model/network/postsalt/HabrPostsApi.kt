package com.makentoshe.habrachan.common.model.network.postsalt

import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HabrPostsApi {

    // Type can be interesting, all
    @GET("https://habr.com/api/v1/posts/{type}")
    fun getPosts(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("type") type: String,
        @Query("page") page: Int,
        @Query("include") include: String?,
        @Query("get_article") getArticle: Boolean?,
        @Query("exclude") exclude: String?
    ): Single<PostsResponse>

    // Type can be daily, weekly, monthly, allTime
    @GET("https://habr.com/api/v1/top/{type}")
    fun getTopPosts(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("type") type: String,
        @Query("page") page: Int,
        @Query("include") include: String?,
        @Query("get_article") getArticle: Boolean?,
        @Query("exclude") exclude: String?
    ): Single<PostsResponse>

    @GET("https://habr.com/api/v1/feed/all")
    fun getFeed(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Query("page") page: Int,
        @Query("include") include: String?,
        @Query("get_article") getArticle: Boolean?,
        @Query("exclude") exclude: String?
    ): Single<PostsResponse>
}