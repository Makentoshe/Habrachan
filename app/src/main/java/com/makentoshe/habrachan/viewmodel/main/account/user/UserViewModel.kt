package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import com.makentoshe.habrachan.common.entity.user.UserResponse
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

    protected val userSubject = BehaviorSubject.create<UserResponse>()
    val userObservable: Observable<UserResponse>
        get() = userSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        userSubject.subscribe { response ->
            if (response is UserResponse.Success) {
                userAvatarViewModel.avatarObserver.onNext(ImageRequest(response.user.avatar))
            }
        }.let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }
}