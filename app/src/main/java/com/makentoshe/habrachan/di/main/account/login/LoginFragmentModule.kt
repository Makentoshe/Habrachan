package com.makentoshe.habrachan.di.main.account.login

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.view.main.account.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Provider

class LoginFragmentModule(fragment: LoginFragment) : Module() {
    init {
        bind<LoginViewModel>().toProviderInstance(LoginViewModelProvider(fragment))
    }
}

class LoginViewModelProvider(private val fragment: LoginFragment) : Provider<LoginViewModel> {

    override fun get(): LoginViewModel {
        val factory = LoginViewModel.Factory()
        return ViewModelProviders.of(fragment, factory)[LoginViewModel::class.java]
    }
}