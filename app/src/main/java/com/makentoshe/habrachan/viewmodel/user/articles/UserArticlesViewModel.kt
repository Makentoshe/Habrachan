package com.makentoshe.habrachan.viewmodel.user.articles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.network.response.ArticlesResponse
import com.makentoshe.habrachan.model.user.articles.UserArticlesDataSource
import com.makentoshe.habrachan.model.user.articles.UserArticlesLoadInitialErrorContainer
import com.makentoshe.habrachan.model.user.articles.UserArticlesLoadInitialSuccessContainer
import com.makentoshe.habrachan.model.user.articles.UserArticlesPagedListEpoxyController
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class UserArticlesViewModel(
    articlesDataSourceFactory: UserArticlesDataSource.Factory,
    private val controller: UserArticlesPagedListEpoxyController,
    private val executorsProvider: UserArticlesViewModelExecutorsProvider,
    schedulersProvider: UserArticlesViewModelSchedulersProvider
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val requestSubject = PublishSubject.create<String>()
    val requestObserver: Observer<String> = requestSubject

    // returns adapter on request subject call
    private val adapterSubject = BehaviorSubject.create<RecyclerView.Adapter<*>>()
    val adapterObservable: Observable<RecyclerView.Adapter<*>> = adapterSubject

    // when first articles was loaded successful
    private val initialSuccessSubject = BehaviorSubject.create<UserArticlesLoadInitialSuccessContainer>()
    val initialSuccessObservable: Observable<UserArticlesLoadInitialSuccessContainer> = initialSuccessSubject

    // when first articles was loaded with error
    private val initialErrorSubject = BehaviorSubject.create<UserArticlesLoadInitialErrorContainer>()
    val initialErrorObservable: Observable<UserArticlesLoadInitialErrorContainer> = initialErrorSubject

    init {
        requestSubject.observeOn(schedulersProvider.ioScheduler)
            .doOnNext { adapterSubject.onNext(controller.adapter) }
            .map(articlesDataSourceFactory::build)
            .doOnNext(::setNewSubjects)
            .map(::buildPagedList)
            .subscribe(controller::submitList)
            .let(disposables::add)

        initialSuccessObservable.subscribe {
            initialErrorSubject.onComplete()
            controller.requestModelBuild()
        }.let(disposables::add)
    }

    private fun setNewSubjects(dataSource: UserArticlesDataSource) {
        dataSource.initialSuccessObservable.safeSubscribe(initialSuccessSubject)
        dataSource.initialErrorObservable.safeSubscribe(initialErrorSubject)
    }

    private fun buildPagedList(dataSource: UserArticlesDataSource): PagedList<Article> {
        val builder = PagedList.Builder(dataSource, buildConfig())
        builder.setFetchExecutor(executorsProvider.fetchExecutor)
        builder.setNotifyExecutor(executorsProvider.notifyExecutor)
        return builder.build()
    }

    private fun buildConfig(): PagedList.Config {
        val builder = PagedList.Config.Builder()
        builder.setPageSize(ArticlesResponse.DEFAULT_SIZE)
        builder.setInitialLoadSizeHint(0)
        builder.setEnablePlaceholders(false)
        return builder.build()
    }

    override fun onCleared() = disposables.clear()

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