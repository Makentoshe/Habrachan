package com.makentoshe.habrachan.application.android.screen.login.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import com.makentoshe.habrachan.application.android.screen.login.viewmodel.LoginViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class LoginScope

class LoginModule(fragment: LoginFragment): Module() {
    
    
    
    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)
        
        val factory = LoginViewModel.Factory()
        val viewModel = ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
        bind<LoginViewModel>().toInstance(viewModel)
    }
}