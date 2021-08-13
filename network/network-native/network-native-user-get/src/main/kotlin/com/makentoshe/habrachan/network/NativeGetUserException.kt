package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.exception.GetUserDeserializerException
import com.makentoshe.habrachan.network.request.GetUserRequest

class NativeGetUserException(
    override val request: GetUserRequest,
    override val message: String? = null,
    override val raw: String? = null,
    override val cause: Throwable? = null
) : GetUserDeserializerException() {

    data class Factory(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int, // 401
        @SerializedName("data")
        val `data`: List<Any>,
        @SerializedName("message")
        val message: String // Authorization required
    ) {
        fun build(request: GetUserRequest, json: String): NativeGetUserException {
            return NativeGetUserException(request, raw = json, message = message)
        }
    }
}