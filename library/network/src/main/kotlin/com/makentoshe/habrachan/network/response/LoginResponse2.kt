package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.LoginRequest2
import okhttp3.Cookie

data class LoginResponse2(
    val request: LoginRequest2,
    /** If login was performed through native manager */
    val nativeResponse: NativeResponse? = null,
    /** If login was performed through mobile manager */
    val mobileResponse: MobileResponse? = null
) {

    /**
     * Native manager main response.
     * This response will be useful only for native managers.
     */
    data class NativeResponse(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("server_time")
        val serverTime: String
    )

    /**
     * Mobile manager main response.
     * This login response will be useful only for mobile managers.
     */
    data class MobileResponse(
        val cookies: List<Cookie>
    )
}