package com.makentoshe.habrachan.di.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.view.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.user.CustomUserViewModel
import com.makentoshe.habrachan.viewmodel.user.MeUserViewModel
import com.makentoshe.habrachan.viewmodel.user.UserViewModel
import javax.inject.Provider

class UserViewModelProvider(
    private val fragment: UserFragment,
    private val userAccount: UserAccount,
    private val userAvatarViewModel: UserAvatarViewModel,
    private val usersManager: UsersManager,
    private val database: HabrDatabase
) : Provider<UserViewModel> {

    override fun get() = when (userAccount) {
        is UserAccount.User -> getCustomUserViewModel(userAccount)
        is UserAccount.Me -> getMeUserViewModel()
    }

    private fun getMeUserViewModel(): MeUserViewModel {
        val factory = MeUserViewModel.Factory(usersManager, database.session(), userAvatarViewModel)
        return ViewModelProviders.of(fragment, factory)[MeUserViewModel::class.java]
    }

    private fun getCustomUserViewModel(user: UserAccount.User): CustomUserViewModel {
    val factory = CustomUserViewModel.Factory(usersManager, database.session(), user, userAvatarViewModel, database.users())
        return ViewModelProviders.of(fragment, factory)[CustomUserViewModel::class.java]
    }
}