package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.deserializer.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.manager.NativeGetArticlesManager
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeGetArticlesManagerTest {

    @Test
    fun shouldFindAndReturnTopDailySpec() {
        val manager = NativeGetArticlesManager.Builder(OkHttpClient(), NativeGetArticlesDeserializer()).build()
        val spec = manager.spec(SpecType.Top(TopSpecType.Daily))

        assertEquals(spec?.type, SpecType.Top(TopSpecType.Daily))
    }
}