package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.LoginBroadcastReceiver
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.MainFlowFragment
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    private val sessionDatabase by inject<SessionDatabase>()
    private val logoutBroadcastReceiver = LogoutBroadcastReceiver()
    private val loginBroadcastReceiver = LoginBroadcastReceiver()

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)

        val navigator = Navigator(fragment.requireActivity(), R.id.main_container, fragment.childFragmentManager)
        val (router, navigatorHolder) = Cicerone.create(Router()).let { it.router to it.navigatorHolder }
        bind<MainFlowFragment.Navigator>().toInstance(MainFlowFragment.Navigator(router, navigatorHolder, navigator))
        bind<SessionDao>().toInstance(sessionDatabase.session())
        bind<LogoutBroadcastReceiver>().toInstance(logoutBroadcastReceiver)
        bind<LoginBroadcastReceiver>().toInstance(loginBroadcastReceiver)
    }
}