package com.makentoshe.habrachan.application.android.common.di.module

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.di.BuildConfig
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module

abstract class BaseNetworkModule: Module() {

    protected val okHttpClient = OkHttpClient.Builder().followRedirects(true).addLoggingInterceptor().build()

    protected val ktorHttpClient = HttpClient(OkHttp) {
        engine { preconfigured = okHttpClient }
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor {
                capture(analyticEvent("OkHttpClient", it))
            }
            logging.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(logging)
        }
        return this
    }

    companion object : Analytics(LogAnalytic())
}
