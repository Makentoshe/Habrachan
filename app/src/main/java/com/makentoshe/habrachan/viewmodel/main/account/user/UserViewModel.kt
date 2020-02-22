package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.MeRequest
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class UserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val meSubject = PublishSubject.create<Unit>()
    private val userSubject = PublishSubject.create<String>()

    private val successSubject = BehaviorSubject.create<User>()
    val successObservable: Observable<User>
        get() = successSubject.observeOn(AndroidSchedulers.mainThread())

    private val errorSubject = BehaviorSubject.create<Throwable>()
    val errorObservable: Observable<Throwable>
        get() = errorSubject.observeOn(AndroidSchedulers.mainThread())

    init {
        meSubject.observeOn(Schedulers.io()).map {
            val session = sessionDao.get()!!
            val request = MeRequest(session.clientKey, session.tokenKey!!)
            return@map usersManager.getMe(request).blockingGet().user.also {
                sessionDao.insert(session.copy(me = it))
            }
        }.onErrorReturn {
            sessionDao.get()!!.me!!
        }.subscribe({ user ->
            successSubject.onNext(user)
            errorSubject.onComplete()
        }, { throwable ->
            errorSubject.onNext(throwable)
        }).let(disposables::add)

        userSubject.observeOn(Schedulers.io()).map { username ->
            val session = sessionDao.get()!!
            val request = UserRequest(session.clientKey, session.apiKey, session.tokenKey, username)
            usersManager.getUser(request).blockingGet().user
        }.subscribe({ user ->
            successSubject.onNext(user)
            errorSubject.onComplete()
        }, { throwable ->
            errorSubject.onNext(throwable)
        }).let(disposables::add)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val usersManager: UsersManager,
        private val sessionDao: SessionDao,
        private val userAccount: UserAccount
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(usersManager, sessionDao).also { userViewModel ->
                when (userAccount) {
                    is UserAccount.Me -> userViewModel.meSubject.onNext(Unit)
                    is UserAccount.User -> userViewModel.userSubject.onNext(userAccount.userName)
                }
            } as T
        }
    }
}