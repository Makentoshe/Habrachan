package com.makentoshe.habrachan.application.android.screen.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.functional.Either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUserArena: GetUserArena,
    private val getContentArena: ContentArena,
    private val userSession: AndroidUserSession
) : ViewModel() {

    /** Send a UserAccount to receive a user from [userFlow] */
    val userAccountChannel = Channel<UserAccount>()

    /** Internal proxy for [userFlow] */
    private val userChannel = Channel<Either<User, Throwable>>()

    /** Receives a [User] instance from [userChannel] */
    val userFlow: Flow<Either<User, Throwable>> = userChannel.receiveAsFlow()

    /** Send a [User] to receive a user avatar from [avatarFlow] */
    private val avatarChannel = Channel<User>()

    /** Receives an [GetContentResponse] instance */
    val avatarFlow = avatarChannel.receiveAsFlow().map { user ->
        val request = getContentArena.manager.request(userSession, user.avatar)
        getContentArena.suspendFetch(request).fold({ Either.Left(it) }, { Either.Right(it) })
    }.flowOn(Dispatchers.IO)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            userAccountChannel.receiveAsFlow().collectLatest(::request)
        }
    }

    private suspend fun request(account: UserAccount) = when (account) {
        is UserAccount.Me -> requestMe(account)
    }

    private suspend fun requestMe(account: UserAccount.Me) {
        // pre show a user
        val user = userSession.user
        if (user != null) {
            userChannel.send(Either.Left(user))
            avatarChannel.send(user)
        }

        // if could not pre show and couldn't make request
        val login = account.login ?: user?.login
            ?: return userChannel.send(Either.Right(IllegalStateException("There is no stored user, and login is null")))

        getUserArena.suspendFetch(getUserArena.manager.request(userSession, login)).fold({
            userChannel.send(Either.Left(it.user))
            // optimize avatar loading - if already was loaded in pre show and not changed yet
            if (user?.avatar != it.user.avatar) avatarChannel.send(it.user)
        }, {
            if (user == null) {
                userChannel.send(Either.Right(it))
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val getUserArena: GetUserArena,
        private val getContentArena: ContentArena,
        private val userSession: AndroidUserSession
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(getUserArena, getContentArena, userSession) as T
        }
    }
}