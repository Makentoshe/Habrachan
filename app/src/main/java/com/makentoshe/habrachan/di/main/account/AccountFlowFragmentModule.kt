package com.makentoshe.habrachan.di.main.account

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class AccountFlowFragmentModule(fragment: AccountFlowFragment) : Module() {

    private val router by inject<Router>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<AccountFlowFragment.Navigator>().toInstance(AccountFlowFragment.Navigator(router))
    }

}