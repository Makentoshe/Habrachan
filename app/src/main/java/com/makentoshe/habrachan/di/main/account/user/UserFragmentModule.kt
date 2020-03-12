package com.makentoshe.habrachan.di.main.account.user

import com.makentoshe.habrachan.di.article.UserAvatarViewModelProvider
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind

class UserFragmentModule(fragment: UserFragment) : Module() {

    init {
        val userAvatarViewModelProvider = UserAvatarViewModelProvider(fragment)
        val userViewModelProvider = UserViewModelProvider(fragment, fragment.arguments.userAccount, userAvatarViewModelProvider.get())
        bind<UserViewModel>().toProviderInstance(userViewModelProvider)
        bind<UserAvatarViewModel>().toProviderInstance(userAvatarViewModelProvider)
    }
}
