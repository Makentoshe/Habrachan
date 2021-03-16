package com.makentoshe.habrachan.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MobileLoginApi {

    @GET("https://m.habr.com/kek/v1/auth/habrahabr")
    fun getLoginCookies(
        @Query("back")
        back: String = "/en/all/",
        @Query("hl")
        habrLanguage: String = "en"
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("ajax/login")
    fun login(
        @Header("Referer")
        referer: String,
        @Field("email")
        email: String,
        @Field("password")
        password: String,
        @Field("state")
        state: String,
        @Field("consumer")
        consumer: String,
        @Field("captcha")
        captcha: String = "",
        @Field("g-recaptcha-response")
        captchaResponse: String = "",
        @Field("captcha_type")
        captchaType: String = "recaptcha"
    ): Call<ResponseBody>
}