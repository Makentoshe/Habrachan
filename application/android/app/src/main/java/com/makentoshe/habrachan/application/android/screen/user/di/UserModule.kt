package com.makentoshe.habrachan.application.android.screen.user.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class UserScope

class UserModule(fragment: UserFragment): Module() {

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)

        val viewModelFactory = UserViewModel.Factory()
        val viewModel = ViewModelProviders.of(fragment, viewModelFactory)[UserViewModel::class.java]
        bind<UserViewModel>().toInstance(viewModel)
    }
}
