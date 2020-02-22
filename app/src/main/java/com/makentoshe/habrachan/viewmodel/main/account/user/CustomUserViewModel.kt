package com.makentoshe.habrachan.viewmodel.main.account.user

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.model.main.account.user.UserAccount

class CustomUserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao,
    private val user: UserAccount.User,
    private val repository: InputStreamRepository,
    private val avatarDao: AvatarDao,
    private val application: Application
) : UserViewModel(repository, avatarDao, application) {

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
        private val user: UserAccount.User,
        private val repository: InputStreamRepository,
        private val avatarDao: AvatarDao,
        private val application: Application
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomUserViewModel(usersManager, sessionDao, user, repository, avatarDao, application) as T
        }
    }
}