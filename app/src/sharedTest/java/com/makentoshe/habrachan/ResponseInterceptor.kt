package com.makentoshe.habrachan

import okhttp3.*

class ResponseInterceptor(
    private val code: Int,
    private val json: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val responseBody = createResponseBody(json, createJsonMediaType())
        return chain
            .proceed(chain.request())
            .newBuilder()
            .code(code)
            .protocol(Protocol.HTTP_2)
            .message(json)
            .body(responseBody)
            .addHeader("content-type", createJsonMediaType().toString())
            .build()
    }

    private fun createJsonMediaType() = MediaType.run {
        "application/json".toMediaType()
    }

    private fun createResponseBody(body: String, mediaType: MediaType? = null) = ResponseBody.run {
        body.toResponseBody(mediaType)
    }
}
