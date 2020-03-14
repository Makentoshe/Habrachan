package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class UserViewModel(
    private val userAvatarViewModel: UserAvatarViewModel
) : ViewModel() {

    protected val disposables = CompositeDisposable()

    protected val successSubject = BehaviorSubject.create<User>()
    val successObservable: Observable<User>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    protected val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        successSubject.subscribe { user ->
            userAvatarViewModel.avatarObserver.onNext(ImageRequest(user.avatar))
        }.let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }
}