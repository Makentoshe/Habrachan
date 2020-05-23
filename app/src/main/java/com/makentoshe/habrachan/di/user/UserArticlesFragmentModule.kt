package com.makentoshe.habrachan.di.user

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.model.user.articles.UserArticlesDataSource
import com.makentoshe.habrachan.model.user.articles.UserArticlesPagedListEpoxyController
import com.makentoshe.habrachan.view.user.UserArticlesFragment
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModel
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModelExecutorsProvider
import com.makentoshe.habrachan.viewmodel.user.articles.UserArticlesViewModelSchedulersProvider
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class UserArticlesFragmentModule(fragment: UserArticlesFragment) : Module() {

    private val articlesManager: ArticlesManager

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val cacheDatabase by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        articlesManager = ArticlesManager.Builder(client).build()

        val userArticlesViewModel = getUserArticlesViewModel(fragment)
        bind<UserArticlesViewModel>().toInstance(userArticlesViewModel)
    }

    private fun getUserArticlesViewModel(fragment: UserArticlesFragment): UserArticlesViewModel {
        val userArticlesDataSourceFactory = UserArticlesDataSource.Factory(articlesManager, cacheDatabase, sessionDatabase)
        val controller = UserArticlesPagedListEpoxyController(ArticleEpoxyModel.Factory(router))
        val executorsProvider = object : UserArticlesViewModelExecutorsProvider {
            override val fetchExecutor = Executors.newSingleThreadExecutor()
            override val notifyExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }
        }
        val schedulersProvider = object : UserArticlesViewModelSchedulersProvider {
            override val ioScheduler = Schedulers.io()
        }
        val factory = UserArticlesViewModel.Factory(userArticlesDataSourceFactory, controller, executorsProvider, schedulersProvider)
        return ViewModelProviders.of(fragment, factory)[UserArticlesViewModel::class.java]
    }
}