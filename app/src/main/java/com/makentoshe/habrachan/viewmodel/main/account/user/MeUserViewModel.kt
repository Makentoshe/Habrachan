package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.MeRequest
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel

class MeUserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao,
    private val userAvatarViewModel: UserAvatarViewModel
) : UserViewModel(userAvatarViewModel) {

    init {
        val session = sessionDao.get()!!
        val request = MeRequest(session.clientKey, session.tokenKey)
        usersManager.getMe(request).map { it.user }.onErrorReturn { session.me!! }.subscribe({ user ->
            sessionDao.insert(session.copy(me = user))
            successSubject.onNext(user)
            errorSubject.onComplete()
        }, { throwable ->
            errorSubject.onNext(throwable)
        }).let(disposables::add)
    }

    class Factory(
        private val usersManager: UsersManager,
        private val sessionDao: SessionDao,
        private val userAvatarViewModel: UserAvatarViewModel
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MeUserViewModel(usersManager, sessionDao, userAvatarViewModel) as T
        }
    }
}