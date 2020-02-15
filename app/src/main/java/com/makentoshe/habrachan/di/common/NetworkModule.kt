package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val client = OkHttpClient.Builder().addLoggingInterceptor().build()

    private val factory = GetArticlesRequest.Builder(
        client = "85cab69095196f3.89453480",
        api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        token = null
    )

    private val manager = HabrArticleManager.Builder(client).build("text_html")

    private val postFactory = GetArticleRequest.Builder(
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
        bind<GetArticlesRequest.Builder>().toInstance(factory)
        bind<GetArticleRequest.Builder>().toInstance(postFactory)
        bind<HabrArticleManager>().toInstance(manager)
        bind<OkHttpClient>().toInstance(client)

        bind<GetCommentsRequest.Factory>().toInstance(getCommentsRequestFactory)
        bind<HabrCommentsManager>().toInstance(commentsManager)
    }

    private fun OkHttpClient.Builder.addLoggingInterceptor(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BASIC
            addInterceptor(logging)
        }
        return this
    }
}