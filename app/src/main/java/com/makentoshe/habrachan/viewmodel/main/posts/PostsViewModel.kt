package com.makentoshe.habrachan.viewmodel.main.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.repository.Repository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class PostsViewModel(
    position: Int,
    private val postsRepository: Repository<Int, Single<List<Article>>>,
    private val postsDao: ArticleDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    /** Emitter for successful network request events */
    private val postsSubject = BehaviorSubject.create<List<Article>>()

    /** Observable for successful network request events */
    val postsObservable: Observable<List<Article>>
        get() = postsSubject.observeOn(AndroidSchedulers.mainThread())

    /** Emitter for unsuccessful network request events */
    private val errorSubject = BehaviorSubject.create<Throwable>()

    /** Observable for unsuccessful network request events */
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    /** Emitter for events requires progress state */
    private val progressSubject = PublishSubject.create<Unit>()

    /** Observable for progress state events */
    val progressObservable: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    /** Subject for requesting */
    private val requestSubject = PublishSubject.create<Int>()

    val requestObserver: Observer<Int>
        get() = requestSubject

    /** Subject for requesting with database cleaning */
    private val newRequestSubject = PublishSubject.create<Int>()

    val newRequestObserver: Observer<Int>
        get() = newRequestSubject

    init {
        requestSubject.observeOn(Schedulers.io()).subscribe {
            val single = postsRepository.get(it)
            if (single == null) {
                errorSubject.onNext(NullPointerException())
            } else {
                single.subscribe(postsSubject::onNext, errorSubject::onNext).let(disposables::add)
            }
        }.let(disposables::add)

        newRequestSubject.observeOn(Schedulers.io()).subscribe {
            progressSubject.onNext(Unit)
            postsDao.clear()
            requestSubject.onNext(it)
        }.let(disposables::add)

        newRequestObserver.onNext(position)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val position: Int,
        private val repository: Repository<Int, Single<List<Article>>>,
        private val postsDao: ArticleDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostsViewModel(position, repository, postsDao) as T
        }
    }
}
