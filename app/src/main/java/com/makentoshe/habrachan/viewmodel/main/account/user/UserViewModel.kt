package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.MeRequest
import io.reactivex.Observable
import io.reactivex.Observer
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
    val meObserver: Observer<Unit>
        get() = meSubject

    private val userSubject = PublishSubject.create<String>()
    val userObserver: Observer<String>
        get() = userSubject

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

        // request me
        meSubject.onNext(Unit)
    }

    override fun onCleared() {
        disposables.clear()
    }

    class Factory(
        private val usersManager: UsersManager,
        private val sessionDao: SessionDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(usersManager, sessionDao) as T
        }
    }
}