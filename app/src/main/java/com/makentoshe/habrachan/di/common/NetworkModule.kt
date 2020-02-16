package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.database.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val sessionDatabase = SessionDatabase(context)

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
    private val loginManager = LoginManager.Builder(client).build()

    init {
        bind<OkHttpClient>().toInstance(client)
        bind<SessionDatabase>().toInstance(sessionDatabase)

        bind<GetArticlesRequest.Builder>().toInstance(factory)
        bind<GetArticleRequest.Builder>().toInstance(postFactory)

        bind<HabrArticleManager>().toInstance(manager)
        bind<HabrCommentsManager>().toInstance(commentsManager)
        bind<LoginManager>().toInstance(loginManager)
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