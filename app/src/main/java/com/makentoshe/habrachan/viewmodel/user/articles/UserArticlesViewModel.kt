package com.makentoshe.habrachan.viewmodel.user.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.makentoshe.habrachan.model.user.articles.UserArticlesDataSource
import com.makentoshe.habrachan.model.user.articles.UserArticlesLoadInitialErrorContainer
import com.makentoshe.habrachan.model.user.articles.UserArticlesLoadInitialSuccessContainer
import com.makentoshe.habrachan.model.user.articles.UserArticlesPagedListEpoxyController
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class UserArticlesViewModel(
    private val articlesDataSourceFactory: UserArticlesDataSource.Factory,
    val controller: UserArticlesPagedListEpoxyController,
    private val executorsProvider: UserArticlesViewModelExecutorsProvider,
    schedulersProvider: UserArticlesViewModelSchedulersProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val requestSubject = PublishSubject.create<String>()
    val requestObserver: Observer<String> = requestSubject

    private val initialSuccessSubject = PublishSubject.create<UserArticlesLoadInitialSuccessContainer>()
    val initialSuccessObservable: Observable<UserArticlesLoadInitialSuccessContainer> = initialSuccessSubject

    private val initialErrorSubject = PublishSubject.create<UserArticlesLoadInitialErrorContainer>()
    val initialErrorObservable: Observable<UserArticlesLoadInitialErrorContainer> = initialErrorSubject

    init {
        requestSubject.observeOn(schedulersProvider.ioScheduler).map { username ->
            articlesDataSourceFactory.build(username)
        }.doOnNext {
            it.initialSuccessObservable.safeSubscribe(initialSuccessSubject)
            it.initialErrorObservable.safeSubscribe(initialErrorSubject)
        }.map { dataSource ->
            val config = PagedList.Config.Builder()
                .setPageSize(controller.pageSize)
                .setInitialLoadSizeHint(0)
                .setEnablePlaceholders(false)
                .build()
            return@map PagedList.Builder(dataSource, config)
                .setFetchExecutor(executorsProvider.fetchExecutor)
                .setNotifyExecutor(executorsProvider.notifyExecutor)
                .build()
        }.subscribe(controller::submitList).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val articlesDataSourceFactory: UserArticlesDataSource.Factory,
        private val controller: UserArticlesPagedListEpoxyController,
        private val executorsProvider: UserArticlesViewModelExecutorsProvider,
        private val schedulersProvider: UserArticlesViewModelSchedulersProvider
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserArticlesViewModel(
                articlesDataSourceFactory,
                controller,
                executorsProvider,
                schedulersProvider
            ) as T
        }
    }
}