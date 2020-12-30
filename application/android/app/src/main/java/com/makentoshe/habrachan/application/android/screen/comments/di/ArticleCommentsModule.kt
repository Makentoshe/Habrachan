package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.network.UserSession
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticleCommentsScope

class ArticleCommentsModule(fragment: ArticleCommentsFragment): Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val exceptionHandler by inject<ExceptionHandler>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

    }
}

