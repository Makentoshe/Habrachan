package com.makentoshe.habrachan.di.main.account.login

import com.makentoshe.habrachan.di.main.account.AccountFlowFragmentScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import com.makentoshe.habrachan.view.main.account.login.LoginFragment
import com.makentoshe.habrachan.viewmodel.main.account.login.LoginViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class LoginFragmentModule(fragment: LoginFragment) : Module() {

    private val navigator by inject<AccountFlowFragment.Navigator>()

    init {
        Toothpick.openScopes(AccountFlowFragmentScope::class.java).inject(this)

        bind<LoginViewModel>().toProviderInstance(LoginViewModelProvider(fragment))
        bind<AccountFlowFragment.Navigator>().toInstance(navigator)
    }
}
