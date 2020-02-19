package com.makentoshe.habrachan.di.main.account.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class UserViewModelProvider(private val fragment: UserFragment) : Provider<UserViewModel> {

    private val usersManager by inject<UsersManager>()
    private val sessionDao by inject<SessionDao>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
    }

    override fun get(): UserViewModel {
        val factory = UserViewModel.Factory(usersManager, sessionDao)
        return ViewModelProviders.of(fragment, factory)[UserViewModel::class.java]
    }
}
