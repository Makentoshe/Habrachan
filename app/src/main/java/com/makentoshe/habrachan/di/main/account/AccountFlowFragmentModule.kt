package com.makentoshe.habrachan.di.main.account

import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class AccountFlowFragmentModule(fragment: AccountFlowFragment) : Module() {

    init {
        val navigator = Navigator(fragment.requireActivity(), R.id.account_container, fragment.childFragmentManager)
        val (router, navigatorHolder) = Cicerone.create().let { it.router to it.navigatorHolder }
        bind<AccountFlowFragment.Navigator>().toInstance(AccountFlowFragment.Navigator(router, navigatorHolder, navigator))
    }

}