package com.makentoshe.habrachan.api.login.api

import com.makentoshe.habrachan.api.HabrApiPath

data class HabrLoginApiBuilder(override val path: String) : HabrApiPath

//@POST("https://habr.com/auth/o/access-token")
//@FormUrlEncoded
//fun login(
//    @Header("client") clientKey: String,
//    @Header("apiKey") apiKey: String,
//    @Field("email") email: String,
//    @Field("password") password: String,
//    @Field("client_secret") clientSecret: String = "41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3",
//    @Field("client_id") clientId: String = clientKey,
//    @Field("grant_type") grantType: String = "password"
//): Call<ResponseBody>
