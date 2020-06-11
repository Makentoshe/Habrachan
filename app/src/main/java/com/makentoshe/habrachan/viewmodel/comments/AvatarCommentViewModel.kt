package com.makentoshe.habrachan.viewmodel.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.response.ImageResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.ConcurrentHashMap

interface AvatarCommentViewModel {

    /**
     * Performs an avatar image request.
     * Returns an observable to image response by url.
     */
    fun getAvatarObservable(url: String): Observable<ImageResponse>

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val imageManager: ImageManager,
        private val cacheDatabase: CacheDatabase
    ) {
        fun buildViewModel(fragment: Fragment): AvatarCommentViewModel {
            val factory = object : ViewModelProvider.NewInstanceFactory() {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return AvatarCommentViewModelImpl(cacheDatabase, imageManager) as T
                }
            }
            return ViewModelProviders.of(fragment, factory)[AvatarCommentViewModelImpl::class.java]
        }
    }

    private class AvatarCommentViewModelImpl(
        private val cacheDatabase: CacheDatabase,
        private val imageManager: ImageManager
    ) : ViewModel(), AvatarCommentViewModel {

        private val observablesContainer = ConcurrentHashMap<String, Observable<ImageResponse>>()

        override fun getAvatarObservable(url: String): Observable<ImageResponse> {
            val cached = observablesContainer[url]
            if (cached != null) return cached
            val subject = BehaviorSubject.create<ImageResponse>()
            observablesContainer[url] = subject

            BehaviorSubject.create<String>().also { request ->
                request.observeOn(Schedulers.io())
                    .map(::ImageRequest)
                    .map(::performAvatarRequest)
                    .safeSubscribe(subject)
            }.onNext(url)

            return subject
        }

        private fun performAvatarRequest(request: ImageRequest): ImageResponse {
            try {
                // if file is stub - just set avatar from resources
                if (File(request.imageUrl).name.contains("stub-user")) {
                    return ImageResponse.Success(request, byteArrayOf(), isStub = true)
                }

                val cached = cacheDatabase.avatars().get(request.imageUrl)
                if (cached != null) {
                    val byteArray = ByteArrayOutputStream().let {
                        cached.compress(Bitmap.CompressFormat.PNG, 100, it)
                        it.toByteArray()
                    }
                    cached.recycle()
                    return ImageResponse.Success(request, byteArray, false)
                }

                return imageManager.getImage(request).doOnSuccess { response ->
                    if (response is ImageResponse.Success) {
                        val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
                        cacheDatabase.avatars().insert(request.imageUrl, bitmap)
                    }
                }.onErrorReturn {
                    ImageResponse.Error(request, it.toString())
                }.blockingGet()
            } catch (e: Exception) {
                return ImageResponse.Error(request, e.toString())
            }
        }
    }
}