package com.makentoshe.habrachan.viewmodel.post.images

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

/* ViewModel for downloading image in view mode for PostImageFragmentPage */
class PostImageFragmentViewModel(private val repository: InputStreamRepository) : ViewModel() {

    private val bitmapSubject = BehaviorSubject.create<Bitmap>()
    private val disposables = CompositeDisposable()

    val bitmapObserver: Observable<Bitmap>
        get() = bitmapSubject.observeOn(AndroidSchedulers.mainThread())

    private fun loadImage(imageSource: String) {
        println(imageSource)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val imageSource: String, private val repository: InputStreamRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return PostImageFragmentViewModel(repository).apply { loadImage(imageSource) } as T
        }
    }
}