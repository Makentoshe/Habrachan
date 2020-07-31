package com.makentoshe.habrachan

import okhttp3.*
import org.junit.Assert

class UrlInterceptor(private val url: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Assert.assertEquals(url, chain.request().url.toUri().toString())
        return chain.proceed(chain.request())
    }
}

open class ResponseInterceptor(private val response: Response) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return response
    }

    companion object {
        fun responseBody(body: String, mediaType: MediaType? = null) = ResponseBody.run {
            body.toResponseBody(mediaType)
        }
    }
}

class JsonResponseInterceptor(code: Int, json: String) : ResponseInterceptor(
    Response.Builder().code(code).protocol(Protocol.HTTP_2).message(json)
        .body(responseBody(json, createJsonMediaType()))
        .request(Request.Builder().url("https://json.response.interceptor").build())
        .addHeader("content-type", createJsonMediaType().toString()).build()
) {

    companion object {

        private fun createJsonMediaType() = MediaType.run {
            "application/json".toMediaType()
        }
    }

}

/**
 * Intercepts and checks request after response received.
 * Can be helpful for requests with redirects.
 */
class ResponseRequestInterceptor(request: Request) : ResponseInterceptor(
    Response.Builder().code(200).protocol(Protocol.HTTP_2).message("").body(responseBody("")).request(request).build()
)
