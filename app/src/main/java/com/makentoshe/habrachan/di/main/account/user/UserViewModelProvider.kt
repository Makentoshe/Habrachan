package com.makentoshe.habrachan.di.main.account.user

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import javax.inject.Provider

class UserViewModelProvider(private val fragment: UserFragment) : Provider<UserViewModel> {

    override fun get(): UserViewModel {
        val factory = UserViewModel.Factory()
        return ViewModelProviders.of(fragment, factory)[UserViewModel::class.java]
    }
}
