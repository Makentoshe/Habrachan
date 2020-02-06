package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.HabrPostManager
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.GetPostRequestFactory
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val client = OkHttpClient.Builder().addLoggingInterceptor().build()

    private val factory = GetPostsRequestFactory(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null
    )

    private val manager = HabrPostsManager.Builder(client).build("text_html")

    private val postManager = HabrPostManager.Factory(client).build()

    private val postFactory = GetPostRequestFactory(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null
    )


    private val commentsManager = HabrCommentsManager.Factory(client).build()
    private val getCommentsRequestFactory = GetCommentsRequest.Factory(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null
    )

    init {
        bind<GetPostsRequestFactory>().toInstance(factory)
        bind<GetPostRequestFactory>().toInstance(postFactory)
        bind<HabrPostsManager>().toInstance(manager)
        bind<OkHttpClient>().toInstance(client)
        bind<HabrPostManager>().toInstance(postManager)

        bind<GetCommentsRequest.Factory>().toInstance(getCommentsRequestFactory)
        bind<HabrCommentsManager>().toInstance(commentsManager)
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