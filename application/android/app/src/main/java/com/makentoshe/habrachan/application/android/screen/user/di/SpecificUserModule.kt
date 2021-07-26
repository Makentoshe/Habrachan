package com.makentoshe.habrachan.application.android.screen.user.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.application.core.arena.users.GetUserArena
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecificUserModule(injectorScope: FragmentInjector.FragmentInjectorScope<UserFragment>) : Module() {

    private val androidUserSession by inject<AndroidUserSession>()
    private val getUserArena by inject<GetUserArena>()
    private val getAvatarArena by inject<ContentArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, UserScope::class).inject(this)

        val viewModelFactory = UserViewModel.Factory(getUserArena, getAvatarArena, androidUserSession)
        val viewModel = ViewModelProviders.of(injectorScope.fragment, viewModelFactory)[UserViewModel::class.java]
        bind<UserViewModel>().toInstance(viewModel)
    }
}