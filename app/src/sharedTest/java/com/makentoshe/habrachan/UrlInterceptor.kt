package com.makentoshe.habrachan

import okhttp3.Interceptor
import okhttp3.Response
import org.junit.Assert.assertEquals

class UrlInterceptor(private val url: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        assertEquals(url, chain.request().url.toUri().toString())
        return chain.proceed(chain.request())
    }
}