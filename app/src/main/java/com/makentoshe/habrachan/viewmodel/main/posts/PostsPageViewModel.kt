package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.database.PostsDao
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
    private val postsDao: PostsDao
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
        val request = requestFactory.interesting(position + 1)
        requestPostsResponse(request)
    }

    private fun requestPostsResponse(request: GetPostsRequest) {
        manager.getPosts(request).subscribe({
            updateDatabase(it.data)
            postsSubject.onNext(it.data)
        }, {
            val cachedSuccess = Array(20) { index ->
                postsDao.getByIndex(position * 20 + index)
            }.filterNotNull().toList()
            postsSubject.onNext(cachedSuccess)
        }).let(disposables::add)
    }

    private fun updateDatabase(posts: List<Data>) {
        postsDao.clear()
        posts.forEachIndexed { index, data ->
            data.index = position * 20 + index
            postsDao.insert(data)
        }
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val manager: HabrPostsManager,
        private val factory: GetPostsRequestFactory,
        private val postsDao: PostsDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsPageViewModel(position, manager, factory, postsDao) as T
        }
    }
}
