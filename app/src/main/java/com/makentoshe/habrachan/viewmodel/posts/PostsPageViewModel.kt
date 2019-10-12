package com.makentoshe.habrachan.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostsPageViewModel(
    position: Int,
    private val manager: HabrPostsManager,
    private val cache: Cache<GetPostsRequest, PostsResponse>,
    requestFactory: GetPostsRequestFactory
) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** Emitter for successful network request events */
    private val postsSubject = BehaviorSubject.create<PostsResponse>()

    /** Observable for successful network request events */
    val postsObservable: Observable<PostsResponse>
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
        manager.getPosts(request).subscribe({
            postsSubject.onNext(it)
            cache.set(request, it)
        }, {
            val cachedSuccess = cache.get(request)
            if (cachedSuccess != null) {
                postsSubject.onNext(cachedSuccess)
            } else {
                errorSubject.onNext(it)
            }
        }).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val manager: HabrPostsManager,
        private val cache: Cache<GetPostsRequest, PostsResponse>,
        private val factory: GetPostsRequestFactory
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsPageViewModel(position, manager, cache, factory) as T
        }
    }
}
