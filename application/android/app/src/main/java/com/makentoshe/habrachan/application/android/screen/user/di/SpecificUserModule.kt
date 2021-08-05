package com.makentoshe.habrachan.application.android.screen.user.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecificUserModule(injectorScope: FragmentInjector.FragmentInjectorScope<UserFragment>) : Module() {

    private val userViewModelFactory by inject<UserViewModel.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, UserScope::class).inject(this)

        val userViewModel = ViewModelProviders.of(injectorScope.fragment, userViewModelFactory)[UserViewModel::class.java]
        bind<UserViewModel>().toInstance(userViewModel)
    }
}