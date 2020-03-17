package com.makentoshe.habrachan.viewmodel.main.account.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.entity.user.UserResponse
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
        val me = session.me

        usersManager.getMe(request).map { response ->
            // try to return me from cache if not fatal network error occurred
            if (response is UserResponse.Error && me != null) {
                UserResponse.Success(me, "")
            } else response
        }.doOnSuccess{ response ->
            // try to safe success result to cache
            if (response is UserResponse.Success) {
                sessionDao.insert(session.copy(me = response.user))
            }
        }.onErrorReturn { throwable ->
            // if fatal error occurred tries to return me from cache or return an error
            if (me == null) {
                UserResponse.Error(throwable.toString())
            } else {
                UserResponse.Success(me, "")
            }
        }.subscribe(userSubject::onNext, userSubject::onError).let(disposables::add)
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