package com.makentoshe.habrachan.viewmodel.post.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/* ViewModel for downloading image in view mode for PostImageFragmentPage */
class PostImageFragmentViewModel(
    private val repository: InputStreamRepository, private val imageSource: String
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val successSubject = BehaviorSubject.create<Bitmap>()
    val successObserver: Observable<Bitmap>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    private val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObserver: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    private val progressSubject = PublishSubject.create<Unit>()
    val progressObserver: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        Single.just(imageSource).observeOn(Schedulers.io())
            .map(repository::get)
            .map(BitmapFactory::decodeStream)
            .subscribe(::onSuccess, ::onError)
            .let(disposables::add)
    }

    private fun onSuccess(bitmap: Bitmap) {
        errorSubject.onComplete()
        progressSubject.onComplete()
        successSubject.onNext(bitmap)
    }

    private fun onError(throwable: Throwable) {
        errorSubject.onError(throwable)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val imageSource: String, private val repository: InputStreamRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostImageFragmentViewModel(repository, imageSource) as T
        }
    }
}