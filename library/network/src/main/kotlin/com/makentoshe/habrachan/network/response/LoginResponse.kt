package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.network.request.LoginRequest
import okhttp3.Cookie

data class LoginResponse(
    val request: LoginRequest,
    /** If login was performed through native manager */
    val nativeResponse: NativeResponse? = null,
    /** If login was performed through mobile manager */
    val mobileResponse: MobileResponse? = null,
    /** A logged in user info. It may be null when a UserManager was not specified in the LoginManager */
    val user: User?
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