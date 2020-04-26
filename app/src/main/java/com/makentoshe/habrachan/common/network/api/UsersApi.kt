package com.makentoshe.habrachan.common.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("/api/v1/users/me")
    fun getMe(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Query("include") include: String?,
        @Query("exclude") exclude: String?
    ): Call<ResponseBody>

    @GET("/api/v1/users/{name}")
    fun getUser(
        @Header("client") clientKey: String,
        @Header("token") token: String,
        @Path("name") name: String,
        @Query("include") include: String?,
        @Query("exclude") exclude: String?
    ): Call<ResponseBody>
}