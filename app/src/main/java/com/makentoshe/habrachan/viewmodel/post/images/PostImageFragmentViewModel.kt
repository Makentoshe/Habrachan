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
import pl.droidsonroids.gif.GifDrawable
import java.io.File
import java.util.*

/* ViewModel for downloading image or gif animation for view mode in PostImageFragmentPage */
class PostImageFragmentViewModel(
    private val repository: InputStreamRepository, private val imageSource: String
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val bitmapSubject = BehaviorSubject.create<Bitmap>()
    val bitmapObserver: Observable<Bitmap>
        get() = bitmapSubject.observeOn(AndroidSchedulers.mainThread())

    private val gifDrawableSubject = BehaviorSubject.create<GifDrawable>()
    val gifDrawableObserver: Observable<GifDrawable>
        get() = gifDrawableSubject.observeOn(AndroidSchedulers.mainThread())

    private val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObserver: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    private val progressSubject = PublishSubject.create<Unit>()
    val progressObserver: Observable<Unit>
        get() = progressSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        if (File(imageSource).extension.toLowerCase(Locale.ENGLISH) == "gif") gifDrawable() else bitmap()
    }

    /** Downloads a bitmap image */
    private fun bitmap() = Single.just(imageSource).observeOn(Schedulers.io())
        .map(repository::get)
        .map(BitmapFactory::decodeStream)
        .subscribe(::onBitmapSuccess, ::onError)
        .let(disposables::add)

    private fun onBitmapSuccess(bitmap: Bitmap) {
        errorSubject.onComplete()
        progressSubject.onComplete()
        gifDrawableSubject.onComplete()
        bitmapSubject.onNext(bitmap)
        bitmapSubject.onComplete()
    }

    /** Downloads a gif drawable */
    private fun gifDrawable() = Single.just(imageSource).observeOn(Schedulers.io())
        .map(repository::get)
        .map { GifDrawable(it.readBytes()) }
        .subscribe(::onGifDrawableSuccess, ::onError)
        .let(disposables::add)

    private fun onGifDrawableSuccess(gifDrawable: GifDrawable) {
        errorSubject.onComplete()
        progressSubject.onComplete()
        bitmapSubject.onComplete()
        gifDrawableSubject.onNext(gifDrawable)
        gifDrawableSubject.onComplete()
    }

    private fun onError(throwable: Throwable) {
        errorSubject.onNext(throwable)
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