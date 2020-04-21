package com.makentoshe.habrachan.viewmodel.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.response.ImageResponse
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
    private val imageSource: String,
    private val imageManager: ImageManager
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
        startLoad()
    }

    fun startLoad() {
        progressSubject.onNext(Unit)
        bitmap()
    }

    /** Downloads a bitmap image */
    private fun bitmap() = Single.just(imageSource).observeOn(Schedulers.io()).subscribe({
        val isGif = File(imageSource).extension.toLowerCase(Locale.ENGLISH) == "gif"
        when (val response = imageManager.getImage(ImageRequest(imageSource)).blockingGet()) {
            is ImageResponse.Success -> {
                if (isGif) {
                    val gifDrawable = GifDrawable(response.bytes)
                    onGifDrawableSuccess(gifDrawable)
                } else {
                    val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
                    onBitmapSuccess(bitmap)
                }
            }
            is ImageResponse.Error -> {
                onError(Exception(response.message))
            }
        }
    }, {
        onError(it)
    }).let(disposables::add)

    private fun onBitmapSuccess(bitmap: Bitmap) {
        errorSubject.onComplete()
        progressSubject.onComplete()
        gifDrawableSubject.onComplete()
        bitmapSubject.onNext(bitmap)
    }

    private fun onGifDrawableSuccess(gifDrawable: GifDrawable) {
        errorSubject.onComplete()
        progressSubject.onComplete()
        bitmapSubject.onComplete()
        gifDrawableSubject.onNext(gifDrawable)
    }

    private fun onError(throwable: Throwable) {
        errorSubject.onNext(throwable)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val imageSource: String, private val imageManager: ImageManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostImageFragmentViewModel(imageSource, imageManager) as T
        }
    }
}