package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.model.main.account.user.UserAccount

class CustomUserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao,
    private val user: UserAccount.User
) : UserViewModel() {

    init {
        val session = sessionDao.get()!!
        val request = UserRequest(session.clientKey, session.apiKey, session.tokenKey, user.userName)
        usersManager.getUser(request).map { it.user }.subscribe({ user ->
            successSubject.onNext(user)
            errorSubject.onComplete()
        }, { throwable ->
            errorSubject.onNext(throwable)
        }).let(disposables::add)
    }

    class Factory(
        private val usersManager: UsersManager,
        private val sessionDao: SessionDao,
        private val user: UserAccount.User
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomUserViewModel(usersManager, sessionDao, user) as T
        }
    }
}