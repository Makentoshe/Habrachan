package com.makentoshe.habrachan.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.UserDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.MeRequest
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.common.network.response.UserResponse
import com.makentoshe.habrachan.model.user.UserAccount
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class UserViewModel(
    private val usersManager: UsersManager,
    private val userDao: UserDao,
    private val sessionDatabase: SessionDatabase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val userRequestSubject = PublishSubject.create<UserAccount>()
    val userObserver: Observer<UserAccount> = userRequestSubject

    private val userResponseSubject = BehaviorSubject.create<UserResponse>()
    val userObservable: Observable<UserResponse>
        get() = userResponseSubject

    init {
        userRequestSubject.observeOn(Schedulers.io()).map(::onUserRequest)
            .subscribe(userResponseSubject::onNext).let(disposables::add)
    }

    private fun onUserRequest(userAccount: UserAccount) = when (userAccount) {
        is UserAccount.Me -> onUserRequestMe(userAccount)
        is UserAccount.User -> onUserRequestUser(userAccount)
    }

    private fun onUserRequestMe(userAccount: UserAccount.Me): UserResponse {
        val session = sessionDatabase.session().get()
        val meRequest = MeRequest(session)
        return usersManager.getMe(meRequest).onErrorReturn { throwable ->
            onUserRequestMeError(session, throwable)
        }.doOnSuccess { userResponse ->
            if (userResponse is UserResponse.Success) {
                sessionDatabase.me().insert(userResponse.user)
            }
        }.blockingGet()
    }

    private fun onUserRequestUser(userAccount: UserAccount.User): UserResponse {
        val session = sessionDatabase.session().get()
        val userRequest = UserRequest(session, userAccount.userName)
        return usersManager.getUser(userRequest).doOnSuccess { userResponse ->
            if (userResponse is UserResponse.Success) userDao.insert(userResponse.user)
        }.onErrorReturn {
            onUserRequestUserError(userAccount, it)
        }.blockingGet()
    }

    private fun onUserRequestUserError(userAccount: UserAccount.User, throwable: Throwable): UserResponse {
        val user = userDao.getByLogin(userAccount.userName)
        return if (user == null) {
            UserResponse.Error(400, throwable.toString(), listOf())
        } else {
            UserResponse.Success(user, "")
        }
    }

    private fun onUserRequestMeError(session: UserSession, throwable: Throwable): UserResponse {
        return if (sessionDatabase.me().isEmpty) {
            UserResponse.Error(400, throwable.toString(), listOf())
        } else {
            UserResponse.Success(sessionDatabase.me().get(), "")
        }
    }

    override fun onCleared() = disposables.clear()

    class Factory(
        private val database: CacheDatabase,
        private val sessionDatabase: SessionDatabase,
        private val usersManager: UsersManager
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(usersManager, database.users(), sessionDatabase) as T
        }
    }
}