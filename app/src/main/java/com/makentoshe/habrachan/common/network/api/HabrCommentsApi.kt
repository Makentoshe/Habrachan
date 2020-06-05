package com.makentoshe.habrachan.common.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HabrCommentsApi {

    @GET("/api/v1/comments/{id}")
    fun getComments(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("id") articleId: Int,
        @Query("since") since: Int = -1
    ): Call<ResponseBody>

    @PUT("api/v1/comments/{id}")
    fun sendComment(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Header("apiKey") apiKey: String,
        @Path("id") articleId: Int,
        @Query("text") text: String,
        @Query("parent_id") parentId: Int
    ): Call<ResponseBody>

    @PUT("/api/v1/comments/{id}/vote?vote=1")
    fun voteUp(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") commentId: Int
    ): Call<ResponseBody>

    @PUT("/api/v1/comments/{id}/vote?vote=-1")
    fun voteDown(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("id") commentId: Int
    ): Call<ResponseBody>
}