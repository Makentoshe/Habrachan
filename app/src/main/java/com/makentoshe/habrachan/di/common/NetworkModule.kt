package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.RequestStorage
import com.makentoshe.habrachan.common.database.SharedRequestStorage
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val requestStorage = SharedRequestStorage.Builder().build(context)

    private val factory = GetPostsRequestFactory(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null,
        requestStorage = requestStorage
    )

    private val client = OkHttpClient.Builder().addLoggingInterceptor().build()

    private val manager = HabrPostsManager.Builder(client).build()

    init {
        bind<RequestStorage>().toInstance(requestStorage)
        bind<GetPostsRequestFactory>().toInstance(factory)
        bind<HabrPostsManager>().toInstance(manager)
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            addInterceptor(logging)
        }
        return this
    }
}