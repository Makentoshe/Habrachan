package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.LoginRequest

data class NativeLoginResponseException(
    val request: LoginRequest,
    val raw: String,
    val email: String?, // MISSING or INVALID
    val password: String?, // MISSING
    val other: String?,
    val code: Int,
    val data: Any?,
    override val message: String
) : Throwable(){

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