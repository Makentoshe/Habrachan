package com.makentoshe.habrachan.viewmodel.main.articles

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.model.main.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.model.main.articles.model.ArticlesPageDivideEpoxyModel
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesDataSource
import com.makentoshe.habrachan.model.main.articles.pagination.ArticlesPagedListEpoxyController
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ArticlesViewModel(
    private val articlesDataSource: ArticlesDataSource,
    private val controller: ArticlesPagedListEpoxyController,
    private val executorsProvider: ArticlesViewModelExecutorsProvider,
    private val schedulersProvider: ArticlesViewModelSchedulersProvider,
    private val sessionDao: SessionDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val requestSubject = PublishSubject.create<Unit>()
    val requestObserver: Observer<Unit> = requestSubject

    private val adapterSubject = BehaviorSubject.create<EpoxyControllerAdapter>()
    val adapterObservable: Observable<EpoxyControllerAdapter> = adapterSubject

    val initialErrorObservable = articlesDataSource.initialErrorObservable
    val rangeErrorObservable = articlesDataSource.rangeErrorObservable

    init {
        requestSubject.observeOn(schedulersProvider.ioScheduler)
            .map(::buildPagedListConfig)
            .map(::buildPagedList)
            .subscribe(controller::submitList)
            .let(disposables::add)

        articlesDataSource.initialSuccessObservable.map { controller.adapter }.safeSubscribe(adapterSubject)
    }

    private fun buildPagedListConfig(ignored: Unit) = PagedList.Config.Builder()
        .setPageSize(controller.pageSize)
        .setInitialLoadSizeHint(0)
        .setEnablePlaceholders(false)
        .build()

    private fun buildPagedList(config: PagedList.Config): PagedList<Article> {
        return PagedList.Builder(articlesDataSource, config)
            .setFetchExecutor(executorsProvider.fetchExecutor)
            .setNotifyExecutor(executorsProvider.notifyExecutor)
            .build()
    }

    fun updateUserSessionArticlesResponseSpec(spec: ArticlesRequestSpec) {
        val currentSession = sessionDao.get()!!
        val newSession = currentSession.copy(articlesRequestSpec = spec)
        sessionDao.insert(newSession)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val articlesManager: ArticlesManager,
        private val cacheDatabase: CacheDatabase,
        private val sessionDatabase: SessionDatabase,
        private val router: Router
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val source = ArticlesDataSource(articlesManager, cacheDatabase, sessionDatabase)
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
            return ArticlesViewModel(
                source,
                controller,
                executorsProvider,
                schedulersProvider,
                sessionDatabase.session()
            ) as T
        }
    }
}
