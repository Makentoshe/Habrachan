package com.makentoshe.habrachan.viewmodel.article

import android.app.Application
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.entity.user.AvatarResponse
import com.makentoshe.habrachan.common.model.BitmapController
import com.makentoshe.habrachan.common.network.manager.AvatarManager
import com.makentoshe.habrachan.common.network.request.AvatarRequest
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.io.File

class UserAvatarViewModel(
    private val avatarDao: AvatarDao,
    private val application: Application,
    private val avatarManager: AvatarManager
) : ViewModel() {

    private val avatarSubject = BehaviorSubject.create<AvatarRequest>()
    val avatarObserver: Observer<AvatarRequest> = avatarSubject
    val avatarObservable: Observable<AvatarResponse>
        get() = avatarSubject.observeOn(Schedulers.io())
            .map(::performAvatarRequest)
            .observeOn(AndroidSchedulers.mainThread())

    private fun performAvatarRequest(request: AvatarRequest): AvatarResponse {
        if (File(request.avatarUrl).name == "stub-user-middle.gif") {
            val drawable = application.resources.getDrawable(R.drawable.ic_account_stub, application.theme)
            return AvatarResponse.Success(BitmapController(drawable.toBitmap()).toByteArray(), true)
        }
        val bitmap = avatarDao.get(request.avatarUrl)
        return if (bitmap == null) {
            avatarManager.getAvatar(request).blockingGet()
        } else {
            AvatarResponse.Success(BitmapController(bitmap).toByteArray(), false)
        }
    }

    class Factory(
        private val avatarDao: AvatarDao,
        private val application: Application,
        private val avatarManager: AvatarManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserAvatarViewModel(avatarDao, application, avatarManager) as T
        }
    }
}