package com.makentoshe.habrachan.viewmodel.main.account.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.ImageDatabase
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class UserViewModel(
    private val repository: InputStreamRepository,
    private val avatarDao: AvatarDao
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
            BitmapFactory.decodeStream(repository.get(user.avatar)).also {
                avatarDao.insert(user.avatar, it)
            }
        }.safeSubscribe(avatarSubject)
    }

    override fun onCleared() {
        disposables.clear()
    }
}