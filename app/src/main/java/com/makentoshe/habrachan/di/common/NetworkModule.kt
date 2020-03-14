package com.makentoshe.habrachan.di.common

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.network.manager.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NetworkScope

class NetworkModule(context: Context) : Module() {

    private val client = OkHttpClient.Builder().addLoggingInterceptor().build()

    private val articleManager = HabrArticleManager.Builder(client).build("text_html")
    private val commentsManager = HabrCommentsManager.Factory(client).build()
    private val loginManager = LoginManager.Builder(client).build()
    private val usersManager = UsersManager.Builder(client).build()
    private val avatarManager = ImageManager.Builder(client).build()

    init {
        bind<OkHttpClient>().toInstance(client)

        bind<HabrArticleManager>().toInstance(articleManager)
        bind<HabrCommentsManager>().toInstance(commentsManager)
        bind<LoginManager>().toInstance(loginManager)
        bind<UsersManager>().toInstance(usersManager)
        bind<ImageManager>().toInstance(avatarManager)
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