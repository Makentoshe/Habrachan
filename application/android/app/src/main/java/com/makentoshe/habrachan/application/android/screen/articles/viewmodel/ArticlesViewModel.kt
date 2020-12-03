package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.pagination.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.pagination.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class ArticlesViewModel(
    private val disposables: CompositeDisposable,
    private val articlesArena: ArticlesArena,
    private val executorsProvider: ExecutorsProvider,
    private val schedulersProvider: SchedulersProvider,
    private val userSession: UserSession,
    private val articleController: ArticlesPagedListEpoxyController
) : ViewModel() {

    private val searchSubject = BehaviorSubject.create<GetArticlesRequest.Spec>()
    val searchObserver: Observer<GetArticlesRequest.Spec> = searchSubject

    private val adapterSubject = BehaviorSubject.create<EpoxyControllerAdapter>()
    val adapterObservable: Observable<EpoxyControllerAdapter> = adapterSubject

    init {
        searchObserver.onNext(userSession.articlesRequestSpec)

        searchSubject.observeOn(schedulersProvider.ioScheduler).map { spec ->
            buildPagedList(buildDataSource(spec))
        }.doOnNext { pagedList ->
            articleController.submitList(pagedList)
        }.subscribe {
            adapterSubject.onNext(articleController.adapter)
        }.let(disposables::add)
    }

    private fun buildDataSource(spec: GetArticlesRequest.Spec): ArticlesDataSource {
        return ArticlesDataSource(viewModelScope, userSession, spec, articlesArena)
    }

    private fun buildPagedList(dataSource: ArticlesDataSource): PagedList<Article> {
        return PagedList.Builder(dataSource, 20).setFetchExecutor(executorsProvider.fetchExecutor)
            .setNotifyExecutor(executorsProvider.notifyExecutor).build()
    }

    //    private val requestSubject = PublishSubject.create<Unit>()
    //    val requestObserver: Observer<Unit> = requestSubject
    //
    //    private val adapterSubject = BehaviorSubject.create<EpoxyControllerAdapter>()
    //    val adapterObservable: Observable<EpoxyControllerAdapter> = adapterSubject
    //
    //    val initialErrorObservable = articlesDataSource.initialErrorObservable
    //    val rangeErrorObservable = articlesDataSource.rangeErrorObservable
    //
    //    init {
    //        requestSubject.observeOn(schedulersProvider.ioScheduler)
    //            .map(::buildPagedListConfig)
    //            .map(::buildPagedList)
    //            .subscribe(controller::submitList)
    //            .let(disposables::add)
    //
    //        articlesDataSource.initialSuccessObservable.map { controller.adapter }.safeSubscribe(adapterSubject)
    //
    //        // initial request starts loading initial batch of articles
    //        requestObserver.onNext(Unit)
    //    }
    //
    //    private fun buildPagedListConfig(ignored: Unit) =
    //        PagedList.Config.Builder().setPageSize(controller.pageSize).setInitialLoadSizeHint(controller.pageSize).setEnablePlaceholders(false).build()
    //
    //    private fun buildPagedList(config: PagedList.Config): PagedList<Article> {
    //        return PagedList.Builder(articlesDataSource, config)
    //            .setFetchExecutor(executorsProvider.fetchExecutor)
    //            .setNotifyExecutor(executorsProvider.notifyExecutor)
    //            .build()
    //    }
    //
    //    fun updateUserSessionArticlesResponseSpec(spec: ArticlesRequestSpec) {
    //        val currentSession = sessionDao.get()
    //        val newSession = currentSession.copy(articlesRequestSpec = spec)
    //        sessionDao.insert(newSession)
    //    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val disposables: CompositeDisposable,
        private val userSession: UserSession,
        private val articlesArena: ArticlesArena,
        private val articleController: ArticlesPagedListEpoxyController,
        private val executorsProvider: ExecutorsProvider,
        private val schedulersProvider: SchedulersProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArticlesViewModel(
            disposables,
            articlesArena,
            executorsProvider,
            schedulersProvider,
            userSession,
            articleController
        ) as T
    }
}
