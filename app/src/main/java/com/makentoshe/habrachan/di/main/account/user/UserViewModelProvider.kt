package com.makentoshe.habrachan.di.main.account.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.database.UserDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.CustomUserViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.MeUserViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
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