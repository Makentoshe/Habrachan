package com.makentoshe.habrachan.application.android.screen.login.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginNavigation
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import com.makentoshe.habrachan.network.manager.LoginManager
import com.makentoshe.habrachan.network.request.LoginRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class LoginScope

class LoginModule(fragment: LoginFragment): Module() {
    
    private val loginManager by inject<LoginManager<LoginRequest>>()
    private val userSession by inject<AndroidUserSession>()
    private val router by inject<StackRouter>()
    
    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)

        bind<LoginNavigation>().toInstance(LoginNavigation(router))

        val factory = LoginViewModel.Factory(userSession, loginManager)
        val viewModel = ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
        bind<LoginViewModel>().toInstance(viewModel)
    }
}