package com.makentoshe.habrachan.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.model.cache.Cache
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.HabrPostsManager
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostsPageViewModel(
    private val position: Int,
    private val manager: HabrPostsManager,
    private val cache: Cache<GetRawRequest, PostsResponse>
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
        requestPostsResponse()
    }

    private fun requestPostsResponse() {
        val request = GetRawRequest(
            client = "85cab69095196f3.89453480",
            api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
            token = null,
            page = position + 1,
            path1 = "posts",
            path2 = "interesting"
        )
        val success = cache.get(request)
        if (success == null) {
            manager.getRaw(request).subscribe({
                cache.set(request, it)
                postsSubject.onNext(it)
            },errorSubject::onNext).let(disposables::add)
        } else {
            postsSubject.onNext(success)
        }
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val manager: HabrPostsManager,
        private val cache: Cache<GetRawRequest, PostsResponse>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsPageViewModel(position, manager, cache) as T
        }
    }
}
