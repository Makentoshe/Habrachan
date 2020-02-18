package com.makentoshe.habrachan.di.main.account.login

import com.makentoshe.habrachan.view.main.account.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import toothpick.config.Module
import toothpick.ktp.binding.bind

class LoginFragmentModule(fragment: LoginFragment) : Module() {
    init {
        bind<LoginViewModel>().toProviderInstance(LoginViewModelProvider(fragment))
    }
}
