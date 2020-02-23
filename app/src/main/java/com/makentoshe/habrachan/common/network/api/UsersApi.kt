package com.makentoshe.habrachan.common.network.api

import com.makentoshe.habrachan.common.entity.user.UserResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UsersApi {

    @GET("/api/v1/users/me")
    fun getMe(
        @Header("client") clientKey: String,
        @Header("token") token: String
    ): Single<UserResponse>

    @GET("/api/v1/users/{name}")
    fun getUser(
        @Header("client") clientKey: String,
        @Header("api") api: String,
        @Header("token") token: String?,
        @Path("name") name: String
    ): Single<UserResponse>

}