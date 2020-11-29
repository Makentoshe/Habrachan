package com.makentoshe.habrachan.di.main.articles

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.model.main.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.model.main.articles.model.ArticlesPageDivideEpoxyModel
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesDataSource
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModelExecutorsProvider
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModelSchedulersProvider
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val articlesManager: ArticlesManager

    private val client by inject<OkHttpClient>()
    private val database by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val router by inject<Router>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        articlesManager = ArticlesManager.Builder(client).build()

        val articlesViewModel2 = getArticlesViewModel(fragment)
        bind<ArticlesViewModel>().toInstance(articlesViewModel2)
    }

    private fun getArticlesViewModel(fragment: ArticlesFragment): ArticlesViewModel {
        val source = ArticlesDataSource(articlesManager, database, sessionDatabase)
        val articleModelFactory = ArticleEpoxyModel.Factory(router)
        val divideModelFactory = ArticlesPageDivideEpoxyModel.Factory()
        val controller = ArticlesPagedListEpoxyController(articleModelFactory, divideModelFactory)
        val executorsProvider = object : ArticlesViewModelExecutorsProvider {
            override val fetchExecutor = Executors.newSingleThreadExecutor()
            override val notifyExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }
        }
        val schedulersProvider = object : ArticlesViewModelSchedulersProvider {
            override val ioScheduler = Schedulers.io()
        }
        val factory = ArticlesViewModel.Factory(source, controller, executorsProvider, schedulersProvider, sessionDatabase.session())
        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
    }
}
