package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.application.android.viewmodel.ExecutorsProvider
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject

class ArticlesViewModel(
    private val disposables: CompositeDisposable,
    private val executorsProvider: ExecutorsProvider,
    private val schedulersProvider: SchedulersProvider,
    private val articleController: ArticlesPagedListEpoxyController,
    private val dataSourceFactory: ArticlesDataSource.Factory
) : ViewModel() {

    /** May contain last search data */
    val searchSubject = BehaviorSubject.create<GetArticlesRequest.Spec>()

    /** Adapter will not being changed for whole time */
    private val adapterSubject = AsyncSubject.create<EpoxyControllerAdapter>()
    val adapterObservable: Observable<EpoxyControllerAdapter> = adapterSubject

    private val initialSubject = BehaviorSubject.create<Result<*>>()
    val initialObservable: Observable<Result<*>> = initialSubject

    private val afterSubject = BehaviorSubject.create<Result<*>>()
    val afterObservable: Observable<Result<*>> = afterSubject

    init {
        searchSubject.observeOn(schedulersProvider.ioScheduler).map { spec ->
            buildPagedList(buildDataSource(spec))
        }.doOnNext { pagedList ->
            articleController.submitList(pagedList)
        }.subscribe {
            adapterSubject.onNext(articleController.adapter)
            adapterSubject.onComplete()
        }.let(disposables::add)
    }

    private fun buildDataSource(spec: GetArticlesRequest.Spec): ArticlesDataSource {
        val source = dataSourceFactory.build(viewModelScope, spec)
        source.initialObservable.safeSubscribe(initialSubject)
        source.afterSubject.safeSubscribe(afterSubject)
        source.afterSubject.safeSubscribe(articleController.afterSubject)
        articleController.retrySubject.safeSubscribe(source.retryAfterSubject)
        return source
    }

    private fun buildPagedList(dataSource: ArticlesDataSource): PagedList<Article> {
        return PagedList.Builder(dataSource, 20).setFetchExecutor(executorsProvider.fetchExecutor)
            .setNotifyExecutor(executorsProvider.notifyExecutor).build()
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val disposables: CompositeDisposable,
        private val articleController: ArticlesPagedListEpoxyController,
        private val executorsProvider: ExecutorsProvider,
        private val schedulersProvider: SchedulersProvider,
        private val dataSourceFactory: ArticlesDataSource.Factory
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArticlesViewModel(
            disposables, executorsProvider, schedulersProvider, articleController, dataSourceFactory
        ) as T
    }
}
