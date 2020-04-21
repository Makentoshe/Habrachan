package com.makentoshe.habrachan.viewmodel.article

import android.app.Application
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.model.BitmapController
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.response.ImageResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File

class UserAvatarViewModel(
    private val avatarDao: AvatarDao,
    private val application: Application,
    private val imageManager: ImageManager
) : ViewModel() {

    private val avatarSubject = BehaviorSubject.create<ImageRequest>()
    val avatarObserver: Observer<ImageRequest> = avatarSubject
    val avatarObservable: Observable<ImageResponse>
        get() = avatarSubject.observeOn(Schedulers.io())
            .map(::performAvatarRequest)
            .observeOn(AndroidSchedulers.mainThread())

    private fun performAvatarRequest(request: ImageRequest): ImageResponse {
        if (File(request.imageUrl).name == "stub-user-middle.gif") {
            return getStubAvatar()
        }
        try {
            val response = imageManager.getImage(request).blockingGet()
            saveByteArrayToDao(request, response)
            return response
        }  catch (e: RuntimeException) {
            val bitmap = avatarDao.get(request.imageUrl) ?: return getStubAvatar()
            return ImageResponse.Success(BitmapController(bitmap).toByteArray(), false)
        }
    }

    private fun getStubAvatar(): ImageResponse.Success {
        val drawable = application.resources.getDrawable(R.drawable.ic_account_stub, application.theme)
        return ImageResponse.Success(BitmapController(drawable.toBitmap()).toByteArray(), true)
    }

    private fun saveByteArrayToDao(request: ImageRequest, response: ImageResponse) {
        if (response is ImageResponse.Success) {
            val bitmap = BitmapFactory.decodeByteArray(response.bytes, 0, response.bytes.size)
            avatarDao.insert(request.imageUrl, bitmap)
        }
    }

    class Factory(
        private val avatarDao: AvatarDao,
        private val application: Application,
        private val imageManager: ImageManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserAvatarViewModel(avatarDao, application, imageManager) as T
        }
    }
}