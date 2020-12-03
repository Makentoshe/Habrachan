package com.makentoshe.habrachan.application.android.screen.articles.di

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesFragmentScope

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val articlesManager: ArticlesManager

    private val client by inject<OkHttpClient>()
    private val database by inject<CacheDatabase>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val router by inject<Router>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        articlesManager = ArticlesManager.Builder(client).build()

//        val articlesViewModel2 = getArticlesViewModel(fragment)
//        bind<ArticlesViewModel>().toInstance(articlesViewModel2)
    }

//    private fun getArticlesViewModel(fragment: ArticlesFragment): ArticlesViewModel {
//        val source = ArticlesDataSource(articlesManager, database, sessionDatabase)
//        val articleModelFactory = ArticleEpoxyModel.Factory(router)
//        val divideModelFactory = ArticlesPageDivideEpoxyModel.Factory()
//        val controller = ArticlesPagedListEpoxyController(articleModelFactory, divideModelFactory)
//        val executorsProvider = object :
//            ExecutorsProvider {
//            override val fetchExecutor = Executors.newSingleThreadExecutor()
//            override val notifyExecutor = Executor { Handler(Looper.getMainLooper()).post(it) }
//        }
//        val schedulersProvider = object :
//            SchedulersProvider {
//            override val ioScheduler = Schedulers.io()
//        }
//        val factory = ArticlesViewModel.Factory(source, controller, executorsProvider, schedulersProvider, sessionDatabase.session())
//        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
//    }
}
