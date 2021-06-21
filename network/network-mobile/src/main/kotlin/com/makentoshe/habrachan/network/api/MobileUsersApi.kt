package com.makentoshe.habrachan.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MobileUsersApi {

    @GET("kek/v2/users/{username}/card")
    fun getUser(
        @Path("username") username: String,
        /** articles languages. Default: ru and en */
        @Query("fl")
        fl: String = "ru%2Cen",
        /** ui language. Default: en */
        @Query("hl")
        hl: String = "en"
    ): Call<ResponseBody>
}