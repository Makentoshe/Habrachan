package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import retrofit2.Call
import retrofit2.http.*
import kotlin.random.Random

interface HabrApi {

    @GET("kek/v1/articles/")
    fun getPostsBySearch(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null
    ): Call<GetPostsResult>

    @GET("kek/v1/articles")
    fun getPosts(
        @Query("page") page: Int,
        @Query("sort") sort: String? = null,
        @Query("hl") hl: String? = null,
        @Query("fl") fl: String? = null,
        @Query("date") date: String? = null,
        @Query("custom") custom: Boolean? = null
    ): Call<GetPostsResult>

    @GET("kek/v1/auth/{path}")
    fun getState(@Path("path") path: String = "habrahabr"): Call<String>

    @GET("https://account.habr.com/captcha/")
    fun captcha(
        @Query("random") random: Double = Random.nextDouble()
    ): Call<ByteArray>

    @FormUrlEncoded
    @POST("https://account.habr.com/ajax/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("state") state: String? = null,
        @Field("consumer") consumer: String? = null,
        @Field("captcha") captchaAlt: String = "",
        @Field("captcha_type") captchaType: String? = null,
        @Field("captcha") captcha: String? = null
    ): Call<String>
}
