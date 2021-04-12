package com.makentoshe.habrachan.application.android.screen.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import com.makentoshe.habrachan.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow

class UserViewModel(
    val arena: GetUserArena,
    val userSession: AndroidUserSession
) : ViewModel() {

    val userAccountChannel = Channel<UserAccount>()

    val user = userAccountChannel.receiveAsFlow().map(::request).flowOn(Dispatchers.IO)

    private fun request(account: UserAccount) = when(account) {
        is UserAccount.Me -> requestMe(account)
    }

    private fun requestMe(account: UserAccount.Me) : User {
        if (account.user != null) return account.user

        TODO("Request user from server")
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val arena: GetUserArena, private val androidUserSession: AndroidUserSession
    ): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return UserViewModel(arena, androidUserSession) as T
        }
    }
}