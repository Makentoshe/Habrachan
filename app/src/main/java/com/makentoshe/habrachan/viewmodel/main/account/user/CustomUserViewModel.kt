package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel

class CustomUserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao,
    private val user: UserAccount.User,
    private val userAvatarViewModel: UserAvatarViewModel
) : UserViewModel(userAvatarViewModel) {

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
        private val userAvatarViewModel: UserAvatarViewModel
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomUserViewModel(usersManager, sessionDao, user, userAvatarViewModel) as T
        }
    }
}