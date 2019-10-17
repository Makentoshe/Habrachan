package com.makentoshe.habrachan.common.repository

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class InputStreamRepository(private val client: OkHttpClient) : Repository<String, InputStream> {
    override fun get(k: String): InputStream? {
        val request = Request.Builder().url(k).build()
        val response = client.newCall(request).execute()
        return response.body?.byteStream()
    }
}