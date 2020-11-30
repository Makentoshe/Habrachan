package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.pagination.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.pagination.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

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

        // initial request starts loading initial batch of articles
        requestObserver.onNext(Unit)
    }

    private fun buildPagedListConfig(ignored: Unit) =
        PagedList.Config.Builder().setPageSize(controller.pageSize).setInitialLoadSizeHint(controller.pageSize).setEnablePlaceholders(false).build()

    private fun buildPagedList(config: PagedList.Config): PagedList<Article> {
        return PagedList.Builder(articlesDataSource, config)
            .setFetchExecutor(executorsProvider.fetchExecutor)
            .setNotifyExecutor(executorsProvider.notifyExecutor)
            .build()
    }

    fun updateUserSessionArticlesResponseSpec(spec: ArticlesRequestSpec) {
        val currentSession = sessionDao.get()
        val newSession = currentSession.copy(articlesRequestSpec = spec)
        sessionDao.insert(newSession)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val articlesDataSource: ArticlesDataSource,
        private val controller: ArticlesPagedListEpoxyController,
        private val executorsProvider: ArticlesViewModelExecutorsProvider,
        private val schedulersProvider: ArticlesViewModelSchedulersProvider,
        private val sessionDao: SessionDao
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(
                articlesDataSource, controller, executorsProvider, schedulersProvider, sessionDao
            ) as T
        }
    }
}
