package com.makentoshe.habrachan.di.main.account.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.account.user.UserAccount
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.main.account.user.CustomUserViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.MeUserViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class UserViewModelProvider(private val fragment: UserFragment) : Provider<UserViewModel> {

    private val usersManager by inject<UsersManager>()
    private val sessionDao by inject<SessionDao>()
    private val repository by inject<InputStreamRepository>()
    private val avatarDao by inject<AvatarDao>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
    }

    override fun get() = when (val userAccount = fragment.arguments.userAccount) {
        is UserAccount.User -> getCustomUserViewModel(userAccount)
        is UserAccount.Me -> getMeUserViewModel()
    }

    private fun getMeUserViewModel(): MeUserViewModel {
        val factory = MeUserViewModel.Factory(
            usersManager, sessionDao, repository, avatarDao, fragment.requireActivity().application
        )
        return ViewModelProviders.of(fragment, factory)[MeUserViewModel::class.java]
    }

    private fun getCustomUserViewModel(user: UserAccount.User): CustomUserViewModel {
        val factory = CustomUserViewModel.Factory(
            usersManager, sessionDao, user, repository, avatarDao, fragment.requireActivity().application
        )
        return ViewModelProviders.of(fragment, factory)[CustomUserViewModel::class.java]
    }
}