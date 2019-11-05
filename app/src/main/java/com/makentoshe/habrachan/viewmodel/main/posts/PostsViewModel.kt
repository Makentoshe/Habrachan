package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.PostsDao
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class PostsViewModel(
    position: Int,
    private val postsRepository: Repository<Int, Single<List<Data>>>
) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** Emitter for successful network request events */
    private val postsSubject = BehaviorSubject.create<List<Data>>()

    /** Observable for successful network request events */
    val postsObservable: Observable<List<Data>>
        get() = postsSubject.observeOn(AndroidSchedulers.mainThread())

    /** Emitter for unsuccessful network request events */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    /** Observable for unsuccessful network request events */
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        requestPosts(position)
    }

    fun requestPosts(position: Int) {
        val single = postsRepository.get(position)
        if (single == null) {
            errorSubject.onNext(NullPointerException())
        } else {
            single.subscribe({ postsSubject.onNext(it) }, errorSubject::onNext).let(disposables::add)
        }
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val repository: Repository<Int, Single<List<Data>>>
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsViewModel(position, repository) as T
        }
    }
}
