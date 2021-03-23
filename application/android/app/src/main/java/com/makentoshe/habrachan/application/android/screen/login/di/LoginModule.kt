package com.makentoshe.habrachan.application.android.screen.login.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import com.makentoshe.habrachan.network.manager.LoginManager
import com.makentoshe.habrachan.network.request.LoginRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class LoginScope

class LoginModule(fragment: LoginFragment): Module() {
    
    private val loginManager by inject<LoginManager<out LoginRequest>>()
    
    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)



        val factory = LoginViewModel.Factory()
        val viewModel = ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
        bind<LoginViewModel>().toInstance(viewModel)
    }
}