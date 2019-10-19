package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostsPageViewModel(
    private val position: Int,
    private val manager: HabrPostsManager,
    requestFactory: GetPostsRequestFactory,
    private val postsCache: Cache<Int, Data>
) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** Emitter for successful network request events */
    private val postsSubject = BehaviorSubject.create<List<Data>>()

    /** Observable for successful network request events */
    val postsObservable: Observable<List<Data>>
        get() = postsSubject

    /** Emitter for unsuccessful network request events */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    /** Observable for unsuccessful network request events */
    val errorObservable: Observable<Throwable>
        get() = errorSubject

    init {
        val request = requestFactory.stored(position + 1)
        requestPostsResponse(request)
    }

    private fun requestPostsResponse(request: GetPostsRequest) {
        manager.getPostsWithBody(request).subscribe({
            postsSubject.onNext(it.data)
            it.data.forEachIndexed { index, data ->
                postsCache.set(position * 20 + index, data)
            }
        }, {
            val cachedSuccess = Array(20) { i -> postsCache.get((position + 1) + i) }.filterNotNull().toList()
            postsSubject.onNext(cachedSuccess)
        }).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val manager: HabrPostsManager,
        private val factory: GetPostsRequestFactory,
        private val postsCache: Cache<Int, Data>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsPageViewModel(position, manager, factory, postsCache) as T
        }
    }
}
