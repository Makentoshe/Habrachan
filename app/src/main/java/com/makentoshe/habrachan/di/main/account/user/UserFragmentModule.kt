package com.makentoshe.habrachan.di.main.account.user

import com.makentoshe.habrachan.di.article.UserAvatarViewModelProvider
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.user.UserFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.main.account.user.UserViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class UserFragmentModule(fragment: UserFragment) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val userAvatarViewModelProvider = UserAvatarViewModelProvider(fragment)
        val userViewModelProvider = UserViewModelProvider(fragment, fragment.arguments.userAccount, userAvatarViewModelProvider.get())
        bind<UserViewModel>().toProviderInstance(userViewModelProvider)
        bind<UserAvatarViewModel>().toProviderInstance(userAvatarViewModelProvider)
        bind<UserFragment.Navigator>().toInstance(UserFragment.Navigator(router))
    }
}
