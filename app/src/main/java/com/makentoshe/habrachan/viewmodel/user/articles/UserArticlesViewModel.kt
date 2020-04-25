package com.makentoshe.habrachan.viewmodel.user.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.model.user.articles.UserArticlesDataSource
import com.makentoshe.habrachan.model.user.articles.UserArticlesPagedListEpoxyController
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModelExecutorsProvider
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModelSchedulersProvider
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

//    val initialErrorObservable = articlesDataSource.initialErrorObservable
//    val rangeErrorObservable = articlesDataSource.rangeErrorObservable

    init {
        requestSubject.observeOn(schedulersProvider.ioScheduler).map { username ->
            val config = PagedList.Config.Builder().setPageSize(controller.pageSize)
                .setInitialLoadSizeHint(0).setEnablePlaceholders(false).build()
            return@map PagedList.Builder(articlesDataSourceFactory.build(username), config)
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
            return UserArticlesViewModel(articlesDataSourceFactory, controller, executorsProvider, schedulersProvider) as T
        }
    }
}