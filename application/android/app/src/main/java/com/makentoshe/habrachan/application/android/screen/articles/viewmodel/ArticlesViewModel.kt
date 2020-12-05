package com.makentoshe.habrachan.application.android.screen.articles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyControllerAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesDataSource
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

// TODO compose articlesArena and articleController to separate class
// for avoiding lite memory leak when viewmodel disposable use in incorrect scope
class ArticlesViewModel(
    private val disposables: CompositeDisposable,
    private val articlesArena: ArticlesArena,
    private val executorsProvider: ExecutorsProvider,
    private val schedulersProvider: SchedulersProvider,
    private val userSession: UserSession,
    private val articleController: ArticlesPagedListEpoxyController
) : ViewModel() {

    /** May contain last search data */
    val searchSubject = BehaviorSubject.create<GetArticlesRequest.Spec>()

    private val adapterSubject = BehaviorSubject.create<EpoxyControllerAdapter>()
    val adapterObservable: Observable<EpoxyControllerAdapter> = adapterSubject

    private val initialSubject = BehaviorSubject.create<Result<*>>()
    val initialObservable: Observable<Result<*>> = initialSubject

    private val afterSubject = BehaviorSubject.create<Result<*>>()
    val afterObservable: Observable<Result<*>> = afterSubject

    init {
        searchSubject.onNext(userSession.articlesRequestSpec)

        // TODO optimize adapter delivery. May be use AsyncSubject
        searchSubject.observeOn(schedulersProvider.ioScheduler).map { spec ->
            buildPagedList(buildDataSource(spec))
        }.doOnNext { pagedList ->
            articleController.submitList(pagedList)
        }.subscribe {
            adapterSubject.onNext(articleController.adapter)
        }.let(disposables::add)
    }

    private fun buildDataSource(spec: GetArticlesRequest.Spec): ArticlesDataSource {
        val source = ArticlesDataSource(viewModelScope, userSession, spec, articlesArena, disposables)
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
        private val userSession: UserSession,
        private val articlesArena: ArticlesArena,
        private val articleController: ArticlesPagedListEpoxyController,
        private val executorsProvider: ExecutorsProvider,
        private val schedulersProvider: SchedulersProvider
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ArticlesViewModel(
            disposables, articlesArena, executorsProvider, schedulersProvider, userSession, articleController
        ) as T
    }
}
