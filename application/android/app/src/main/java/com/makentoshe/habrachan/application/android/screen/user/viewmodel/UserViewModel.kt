package com.makentoshe.habrachan.application.android.screen.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.GetContentResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(
    private val getUserArena: GetUserArena,
    private val getContentArena: ContentArena,
    private val userSession: AndroidUserSession
) : ViewModel() {

    val logoffChannel = Channel<Unit>()

    val logoffFlow: Flow<Unit> = logoffChannel.receiveAsFlow().map {
        userSession.token = ""
        userSession.user = null
    }

    /** Send a UserAccount to receive a user from [userFlow] */
    val accountChannel = Channel<UserAccount>()

    /** Internal proxy for [userFlow] */
    private val userChannel = Channel<Either2<User, Throwable>>()

    /** Receives a [User] instance from [userChannel] */
    val userFlow: Flow<Either2<User, Throwable>> = userChannel.receiveAsFlow()

    /** Send a [User] to receive a user avatar from [avatarFlow] */
    private val avatarChannel = Channel<User>()

    /** Receives an [GetContentResponse] instance */
    val avatarFlow = avatarChannel.receiveAsFlow().map { user ->
        val request = getContentArena.manager.request(userSession, user.avatar)
        getContentArena.suspendFetch(request).fold({ Either2.Left(it) }, { Either2.Right(it) })
    }.flowOn(Dispatchers.IO)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accountChannel.receiveAsFlow().collectLatest(::request)
        }
    }

    private suspend fun request(account: UserAccount) = when (account) {
        is UserAccount.Me -> requestMe(account)
        is UserAccount.User -> requestUser(account)
    }

    private suspend fun requestMe(account: UserAccount.Me) {
        // pre show a user
        val user = userSession.user
        if (user != null) {
            userChannel.send(Either2.Left(user))
            avatarChannel.send(user)
        }

        // if could not pre show and couldn't make request
        val login = account.login ?: user?.login
            ?: return userChannel.send(Either2.Right(IllegalStateException("There is no stored user, and login is null")))

        requestUser(UserAccount.User(login))
    }

    private suspend fun requestUser(account: UserAccount.User) {
        getUserArena.suspendFetch(getUserArena.manager.request(userSession, account.login)).fold({
            userChannel.send(Either2.Left(it.user))
            avatarChannel.send(it.user)
        }, {
            userChannel.send(Either2.Right(it))
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