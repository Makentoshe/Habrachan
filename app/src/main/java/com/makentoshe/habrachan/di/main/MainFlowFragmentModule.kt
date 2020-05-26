package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.common.broadcast.LoginBroadcastReceiver
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.MainFlowFragment
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

        bind<SessionDao>().toInstance(sessionDatabase.session())
        bind<LogoutBroadcastReceiver>().toInstance(logoutBroadcastReceiver)
        bind<LoginBroadcastReceiver>().toInstance(loginBroadcastReceiver)
    }
}