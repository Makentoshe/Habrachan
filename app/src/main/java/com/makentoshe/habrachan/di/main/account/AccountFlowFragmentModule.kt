package com.makentoshe.habrachan.di.main.account

import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.HabrDatabase
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.account.AccountFlowFragment
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class AccountFlowFragmentModule(fragment: AccountFlowFragment) : Module() {

    private val database by inject<HabrDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)

        val navigator = Navigator(fragment.requireActivity(), R.id.account_container, fragment.childFragmentManager)
        val (router, navigatorHolder) = Cicerone.create(Router()).let { it.router to it.navigatorHolder }
        bind<AccountFlowFragment.Navigator>().toInstance(
            AccountFlowFragment.Navigator(router, navigatorHolder, navigator)
        )

        bind<SessionDao>().toInstance(database.session())
    }

}