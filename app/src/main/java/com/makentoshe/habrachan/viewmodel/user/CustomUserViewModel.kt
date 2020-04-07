package com.makentoshe.habrachan.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.database.UserDao
import com.makentoshe.habrachan.common.entity.user.UserResponse
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.UserRequest
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel

class CustomUserViewModel(
    private val usersManager: UsersManager,
    private val sessionDao: SessionDao,
    private val user: UserAccount.User,
    private val userAvatarViewModel: UserAvatarViewModel,
    private val userDao: UserDao
) : UserViewModel(userAvatarViewModel) {

    init {
        val session = sessionDao.get()!!
        val request = UserRequest(session.clientKey, session.apiKey, session.tokenKey, user.userName)

        usersManager.getUser(request).onErrorReturn { throwable ->
            val user = userDao.getByLogin(user.userName)
            if (user == null) {
                UserResponse.Error(throwable.toString())
            } else {
                UserResponse.Success(user, "")
            }
        }.subscribe(userSubject::onNext, userSubject::onError).let(disposables::add)
    }

    class Factory(
        private val usersManager: UsersManager,
        private val sessionDao: SessionDao,
        private val user: UserAccount.User,
        private val userAvatarViewModel: UserAvatarViewModel,
        private val userDao: UserDao
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CustomUserViewModel(usersManager, sessionDao, user, userAvatarViewModel, userDao) as T
        }
    }
}