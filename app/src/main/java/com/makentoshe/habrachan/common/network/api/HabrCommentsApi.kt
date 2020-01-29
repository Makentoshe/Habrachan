package com.makentoshe.habrachan.common.network.api

import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HabrCommentsApi {

    @GET("/api/v1/comments/{id}")
    fun getComments(
        @Header("client") clientKey: String,
        @Header("token") token: String?,
        @Header("apiKey") apiKey: String?,
        @Path("id") articleId: Int,
        @Query("since") since: Int = -1
    ): Single<CommentsResponse>
}