package com.makentoshe.habrachan.network.exception

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.LoginRequest
import com.makentoshe.habrachan.network.request.NativeLoginRequest

data class NativeLoginResponseException(
    override val request: LoginRequest,
    override val raw: String,
    override val email: String?, // MISSING or INVALID
    override val password: String?, // MISSING
    override val other: String?,
    val code: Int,
    val data: Any?,
    override val message: String
) : LoginResponseException() {

    data class Factory(
        @SerializedName("additional")
        val additional: Any,
        @SerializedName("code")
        val code: Int, // 400
        @SerializedName("data")
        val `data`: Any?, // null
        @SerializedName("message")
        val message: String // Bad request
    ) {

        fun build(request: NativeLoginRequest, raw: String): NativeLoginResponseException {
            val errors = (additional as Map<String, Any>)["errors"]
            val other = if (errors is String) errors else null
            val list = if (errors is List<*>) errors as List<Map<String, String>> else null
            val email = list?.find { it["field"] == "email" }?.get("key")
            val password = list?.find { it["field"] == "password"}?.get("key")
            return NativeLoginResponseException(request, raw, email, password, other, code, data, message)
        }
    }
}