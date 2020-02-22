package com.makentoshe.habrachan.viewmodel.main.account.user

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.model.post.comment.BitmapController
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.io.File

abstract class UserViewModel(
    private val repository: InputStreamRepository,
    private val avatarDao: AvatarDao,
    private val application: Application
) : ViewModel() {

    protected val disposables = CompositeDisposable()

    protected val successSubject = BehaviorSubject.create<User>()
    val successObservable: Observable<User>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    protected val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    private val avatarSubject = BehaviorSubject.create<Bitmap>()
    val avatarObservable: Observable<Bitmap>
        get() = avatarSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        successSubject.map { user ->
            if (File(user.avatar).name == "stub-user-middle.gif") {
                return@map application.resources.getDrawable(R.drawable.ic_account_stub, application.theme)!!.toBitmap()
            }
            val bitmap = avatarDao.get(user.avatar) ?: BitmapFactory.decodeStream(repository.get(user.avatar))
            avatarDao.insert(user.avatar, bitmap)
            return@map bitmap
        }.map { bitmap ->
            BitmapController(bitmap).roundCornersPx(application, 10)
        }.safeSubscribe(avatarSubject)
    }

    override fun onCleared() {
        disposables.clear()
    }
}