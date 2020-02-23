package com.makentoshe.habrachan.di.main.account.user

import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind

class UserFragmentModule(fragment: UserFragment) : Module() {

    init {
        bind<UserViewModel>().toProviderInstance(UserViewModelProvider(fragment))
    }
}
