package com.makentoshe.habrachan.network.exception

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.GetUserRequest

class NativeGetUserDeserializerException(
    override val request: GetUserRequest,
    override val raw: String,
    val additional: List<String>,
    val code: Int, // 401
    override val message: String // Authorization required
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
        fun build(request: GetUserRequest, json: String): NativeGetUserDeserializerException {
            return NativeGetUserDeserializerException(request, json, additional, code, message)
        }
    }
}