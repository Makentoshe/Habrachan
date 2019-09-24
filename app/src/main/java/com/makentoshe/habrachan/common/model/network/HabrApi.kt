package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.entity.GetPostsBySearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HabrApi {

    @GET("kek/v1/articles/")
    fun getPostsBySearch(
        @Query("query") query: String, @Query("page") page: Int, @Query("hl") hl: String? = null, @Query("fl") fl: String? = null
    ): Call<GetPostsBySearchResult>

}
