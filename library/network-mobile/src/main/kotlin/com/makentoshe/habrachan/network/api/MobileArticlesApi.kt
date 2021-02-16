package com.makentoshe.habrachan.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MobileArticlesApi {

    @GET("kek/v2/articles/")
    fun getArticles(

        @Query("page") page: Int,

        @QueryMap queries: Map<String, String>,

        /** articles languages. Default: ru and en */
        @Query("fl") fl: String = "ru%2Cen",

        /** ui language. Default: en */
        @Query("hl") hl: String = "en"

    ): Call<ResponseBody>

    @GET("kek/v2/articles/{id}")
    fun getArticle(
        @Path("id") articleId: Int,

        /** articles languages. Default: ru and en */
        @Query("fl") fl: String = "ru%2Cen",

        /** ui language. Default: en */
        @Query("hl") hl: String = "en"
    ): Call<ResponseBody>
}