package com.makentoshe.habrachan.viewmodel.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.model.network.postsalt.GetInterestingRequest
import com.makentoshe.habrachan.common.model.network.postsalt.HabrPostsManager
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostsPageViewModel(
    private val position: Int, private val manager: HabrPostsManager
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
        val request = GetInterestingRequest(
            client = "85cab69095196f3.89453480",
            api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
            token = "ee828f6b64a066b352dc18e3034038c905c4d8ca",
            page = position + 1
        )
        manager.getInteresting(request).subscribe { p, t -> onRequestComplete(p, t) }.let(disposables::add)
    }

    private fun onRequestComplete(posts: PostsResponse?, error: Throwable?) {
        if (error != null) return errorSubject.onNext(error)
        if (posts != null) return postsSubject.onNext(posts)
        errorSubject.onNext(Exception("wtf"))
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(private val position: Int) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val manager = HabrPostsManager.build()
            return PostsPageViewModel(position, manager) as T
        }
    }
}