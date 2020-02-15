package com.makentoshe.habrachan.common.network.api

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

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
    ):Single<PostsResponse>

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

}